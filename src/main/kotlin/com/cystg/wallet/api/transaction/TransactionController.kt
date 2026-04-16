package com.cystg.wallet.api.transaction

import com.cystg.wallet.application.ledger.RecordTransactionCommand
import com.cystg.wallet.application.ledger.RecordTransactionUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val recordTransactionUseCase: RecordTransactionUseCase,
) {
    @PostMapping
    fun record(@RequestBody request: RecordTransactionRequest): ResponseEntity<RecordTransactionResponse> {
        val result = recordTransactionUseCase.record(
            RecordTransactionCommand(
                userId = request.userId,
                walletId = request.walletId,
                accountId = request.accountId,
                categoryId = request.categoryId,
                amount = request.amount,
                currency = request.currency,
                note = request.note,
            )
        )

        return result.fold(
            ifLeft = { ResponseEntity.status(HttpStatus.BAD_REQUEST).build() },
            ifRight = { ResponseEntity.ok(RecordTransactionResponse(it.transactionId)) },
        )
    }
}

data class RecordTransactionRequest(
    val userId: UUID,
    val walletId: UUID,
    val accountId: UUID,
    val categoryId: UUID? = null,
    val amount: BigDecimal,
    val currency: String,
    val note: String? = null,
)

data class RecordTransactionResponse(
    val transactionId: UUID,
)
