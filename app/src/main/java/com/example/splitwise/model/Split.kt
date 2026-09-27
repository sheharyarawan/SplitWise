package com.example.splitwise.model

data class Split(
    val userId: String = "",
    val amount: Double = 0.0,
    val share: Double? = null
)