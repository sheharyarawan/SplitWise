package com.example.splitwise.model

data class GroupWithBalance(
    val group: Group,
    val youGetBack: Double = 0.0,
    val youOwe: Double = 0.0,
    val balances: List<GroupBalance> = emptyList()
)