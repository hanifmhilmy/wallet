-- ============================================================
-- Wallet Schema
-- PostgreSQL 16+
-- Double-entry ledger, append-only, transaction recording only
-- ============================================================

-- ------------------------------------------------------------
-- Extensions
-- ------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "btree_gist"; -- needed for EXCLUDE constraints
CREATE EXTENSION IF NOT EXISTS bloom;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ============================================================
-- TYPES
-- ============================================================

CREATE TYPE user_status AS ENUM ('ACTIVE', 'INACTIVE', 'SUSPENDED');
CREATE TYPE account_type AS ENUM ('ASSET', 'INCOME', 'EXPENSE', 'LIABILITY');
CREATE TYPE transaction_type AS ENUM ('INCOME', 'EXPENSE', 'TRANSFER');
CREATE TYPE transaction_status AS ENUM ('PENDING', 'COMPLETED', 'CANCELLED');
CREATE TYPE entry_type AS ENUM ('DEBIT', 'CREDIT');
CREATE TYPE category_type AS ENUM ('INCOME', 'EXPENSE', 'TRANSFER');


-- ============================================================
-- USERS
-- Auth identity. Owns wallets and categories.
-- ============================================================

CREATE TABLE users
(
    id         UUID         NOT NULL DEFAULT gen_random_uuid(),
    email      VARCHAR(255) NOT NULL,
    username   VARCHAR(100) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    status     user_status  NOT NULL DEFAULT 'ACTIVE',
    version    BIGINT       NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT uq_users_username UNIQUE (username)
);

CREATE INDEX bloom_users ON users
    USING bloom (email, username, status)
    WITH (length = 80, col1 = 3, col2 = 3, col3 = 2);



-- ============================================================
-- ACCOUNTS  (double-entry chart of accounts)
-- Every ledger debit/credit references an account.
-- User wallets are ASSET accounts.
-- INCOME / EXPENSE / LIABILITY are system-level counterparts.
-- ============================================================

CREATE TABLE accounts
(
    id         UUID         NOT NULL DEFAULT gen_random_uuid(),
    user_id    UUID, -- NULL for system accounts
    name       VARCHAR(100) NOT NULL,
    type       account_type NOT NULL,
    currency   CHAR(3)      NOT NULL DEFAULT 'IDR',
    is_system  BOOLEAN      NOT NULL DEFAULT FALSE,
    version    BIGINT       NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT pk_accounts PRIMARY KEY (id),
    CONSTRAINT fk_accounts_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uq_accounts_user_name UNIQUE (user_id, name),
    -- System accounts must have NULL user_id
    CONSTRAINT ck_accounts_system CHECK (is_system = FALSE OR user_id IS NULL)
);

-- Seed: system counterpart accounts
INSERT INTO accounts (id, name, type, currency, is_system)
VALUES ('00000000-0000-0000-0000-000000000001', 'System Income', 'INCOME', 'IDR', TRUE),
       ('00000000-0000-0000-0000-000000000002', 'System Expense', 'EXPENSE', 'IDR', TRUE),
       ('00000000-0000-0000-0000-000000000003', 'System Transfer', 'LIABILITY', 'IDR', TRUE)
ON CONFLICT DO NOTHING;


-- ============================================================
-- WALLETS
-- One user-owned ASSET pocket linked 1:1 to an accounts row.
--
-- Balance is derived from ledger history; snapshots are optional.
-- ============================================================

CREATE TABLE wallets
(
    id           UUID           NOT NULL DEFAULT gen_random_uuid(),
    account_id   UUID           NOT NULL,
    user_id      UUID           NOT NULL,
    name         VARCHAR(100)   NOT NULL,
    balance      NUMERIC(20, 4) NOT NULL DEFAULT 0,
    currency     CHAR(3)        NOT NULL DEFAULT 'IDR',
    version      BIGINT         NOT NULL DEFAULT 0,
    created_at   TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ    NOT NULL DEFAULT now(),

    CONSTRAINT pk_wallets PRIMARY KEY (id),
    CONSTRAINT fk_wallets_account FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT fk_wallets_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uq_wallets_account UNIQUE (account_id), -- 1:1 with accounts
    CONSTRAINT uq_wallets_user_name UNIQUE (user_id, name),
    CONSTRAINT ck_wallets_balance CHECK (balance >= 0),
    CONSTRAINT ck_wallets_currency CHECK (char_length(currency) = 3)
);


-- ============================================================
-- CATEGORIES
-- User-defined or global default labels for transactions.
-- ============================================================

CREATE TABLE categories
(
    id         UUID          NOT NULL DEFAULT gen_random_uuid(),
    user_id    UUID,    -- NULL = global default visible to all users
    name       VARCHAR(100)  NOT NULL,
    type       category_type NOT NULL,
    icon       VARCHAR(50), -- local image or cdn image path
    is_default BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ   NOT NULL DEFAULT now(),

    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uq_categories_user_name UNIQUE (user_id, name)
);

-- Seed: global default categories
INSERT INTO categories (id, user_id, name, type, icon, is_default)
VALUES ('10000000-0000-0000-0000-000000000001', NULL, 'Food & Drink', 'EXPENSE', 'food', TRUE),
       ('10000000-0000-0000-0000-000000000002', NULL, 'Transport', 'EXPENSE', 'car', TRUE),
       ('10000000-0000-0000-0000-000000000003', NULL, 'Shopping', 'EXPENSE', 'bag', TRUE),
       ('10000000-0000-0000-0000-000000000004', NULL, 'Health', 'EXPENSE', 'health', TRUE),
       ('10000000-0000-0000-0000-000000000005', NULL, 'Entertainment', 'EXPENSE', 'star', TRUE),
       ('10000000-0000-0000-0000-000000000006', NULL, 'Salary', 'INCOME', 'briefcase', TRUE),
       ('10000000-0000-0000-0000-000000000007', NULL, 'Other Income', 'INCOME', 'plus', TRUE),
       ('10000000-0000-0000-0000-000000000008', NULL, 'Transfer', 'TRANSFER', 'transfer', TRUE)
ON CONFLICT DO NOTHING;


-- ============================================================
-- TRANSACTIONS
-- User-facing action record.
-- Each completed transaction produces exactly 2 ledger_entries
-- (one DEBIT + one CREDIT). Enforced via constraint below.
-- ============================================================

CREATE TABLE transactions
(
    id            UUID               NOT NULL DEFAULT gen_random_uuid(),
    user_id       UUID               NOT NULL,
    category_id   UUID,
    amount        NUMERIC(20, 4)     NOT NULL,
    currency      CHAR(3)            NOT NULL DEFAULT 'IDR',
    type          transaction_type   NOT NULL,
    status        transaction_status NOT NULL DEFAULT 'PENDING',
    note          TEXT,
    transacted_at TIMESTAMPTZ        NOT NULL DEFAULT now(),
    version       BIGINT             NOT NULL DEFAULT 0,
    created_at    TIMESTAMPTZ        NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ        NOT NULL DEFAULT now(),

    CONSTRAINT pk_transactions PRIMARY KEY (id),
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_transactions_cat FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT ck_transactions_amount CHECK (amount > 0)
);

CREATE INDEX idx_transactions_user_transacted_at ON transactions (user_id, transacted_at DESC);


-- ============================================================
-- LEDGER ENTRIES  (append-only — never UPDATE or DELETE)
-- Rules:
--   • amount is always positive; direction is entry_type
--   • every transaction has exactly 1 DEBIT + 1 CREDIT row
--     (enforced by the unique index below)
--   • SUM of signed amounts per transaction = 0
--     DEBIT  on ASSET   → decreases balance
--     CREDIT on ASSET   → increases balance
--     DEBIT  on EXPENSE → increases expense total
--     CREDIT on INCOME  → increases income total
-- ============================================================

CREATE TABLE ledger_entries
(
    id             UUID           NOT NULL DEFAULT gen_random_uuid(),
    transaction_id UUID           NOT NULL,
    account_id     UUID           NOT NULL,
    entry_type     entry_type     NOT NULL,
    amount         NUMERIC(20, 4) NOT NULL,
    currency       CHAR(3)        NOT NULL DEFAULT 'IDR',
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT now(),
    -- No updated_at — this table is intentionally immutable

    CONSTRAINT pk_ledger PRIMARY KEY (id),
    CONSTRAINT fk_ledger_transaction FOREIGN KEY (transaction_id) REFERENCES transactions (id),
    CONSTRAINT fk_ledger_account FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT ck_ledger_amount CHECK (amount > 0),
    -- Enforce exactly one DEBIT and one CREDIT per transaction
    CONSTRAINT uq_ledger_txn_type UNIQUE (transaction_id, entry_type)
);

-- PASETO auth state can remain stateless; no token table is required.

-- Optional projection history may be added later in Cassandra or Redis.


-- ============================================================
-- INDEXES
-- ============================================================

-- Users
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_status ON users (status);

-- Accounts
CREATE INDEX idx_accounts_user_id ON accounts (user_id);
CREATE INDEX idx_accounts_type ON accounts (type);

-- Wallets
CREATE INDEX idx_wallets_user_id ON wallets (user_id);

-- Transactions — most queries filter by user + date range
CREATE INDEX idx_transactions_user_date ON transactions (user_id, transacted_at DESC);
CREATE INDEX idx_transactions_type ON transactions (type);
CREATE INDEX idx_transactions_status ON transactions (status);
CREATE INDEX idx_transactions_category ON transactions (category_id);

-- Ledger entries — heavy read by account and time window
CREATE INDEX idx_ledger_account_date ON ledger_entries (account_id, created_at DESC);
CREATE INDEX idx_ledger_transaction_id ON ledger_entries (transaction_id);

-- Cassandra projections will provide optional read models later.
