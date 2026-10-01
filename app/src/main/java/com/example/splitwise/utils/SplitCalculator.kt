package com.example.splitwise.utils

import com.example.splitwise.data.model.Split
import com.example.splitwise.data.model.User

object SplitCalculator {

    fun calculateEqualSplit(
        amount: Double,
        users: List<User>
    ): List<Split> {

        if (users.isEmpty()) {
            return emptyList()
        }

        val equalAmount = amount / users.size

        return users.map { user ->

            Split(
                userId = user.id,
                userName = user.name,
                amount = equalAmount
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