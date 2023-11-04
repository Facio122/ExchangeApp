package com.example.exchangeapp

import java.util.Currency

data class Request (
    val table: String,
    val no: String,
    val effectiveDate: String,
    val rates: List<ExchangeRate>
)

data class ExchangeRate(
    val currency: String,
    val code: String,
    val mid: Double
)