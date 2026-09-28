package com.example.splitwise.utils

import com.example.splitwise.model.Split
import com.example.splitwise.model.User
import kotlin.math.round

object SplitCalculator {

    fun calculateEqualSplit(
        amount: Double,
        users: List<User>
    ): List<Split> {

        if (users.isEmpty()) {
            return emptyList()
        }

        val totalCents =
            round(amount * 100).toLong()

        val baseCents =
            totalCents / users.size

        val remainder =
            totalCents % users.size

        return users.mapIndexed { index, user ->

            val cents =
                baseCents +
                        if (index < remainder) 1 else 0

            Split(
                userId = user.id,
                userName = user.name,
                amount = cents / 100.0
            )
        }
    }

    fun calculateUnequalSplit(
        amounts: Map<String, Double>
    ): List<Split> {

        return amounts.map { (userId, amount) ->

            Split(
                userId = userId,
                amount = amount
            )
        }
    }

    fun calculatePercentageSplit(
        amount: Double,
        percentages: Map<String, Double>
    ): List<Split> {

        return percentages.map { (userId, percentage) ->

            Split(
                userId = userId,
                amount = amount * percentage / 100.0,
                share = percentage
            )
        }
    }
}