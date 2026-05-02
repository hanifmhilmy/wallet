package com.cystg.wallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
object WalletApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<WalletApplication>(*args)
    }
}