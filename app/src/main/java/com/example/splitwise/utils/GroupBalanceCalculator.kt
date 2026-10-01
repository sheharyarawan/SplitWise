package com.example.splitwise.utils

import com.example.splitwise.data.model.Expense
import com.example.splitwise.data.model.GroupBalance
import com.example.splitwise.data.model.Settlement
import kotlin.math.abs

object GroupBalanceCalculator {

    fun calculate(
        expenses: List<Expense>,
        settlements: List<Settlement>
    ): List<GroupBalance> {

        // Pair-wise balance between users
        //
        // Key:
        // "userA_userB"
        //
        // Positive amount means:
        // userA owes userB
        val balances = mutableMapOf<String, Double>()

        // Keep names so we can display them later
        val userNames = mutableMapOf<String, String>()

        // --------------------------------------------------
        // 1. Process expenses
        // --------------------------------------------------

        expenses.forEach { expense ->

            val payerId = expense.paidBy ?: return@forEach

            if (payerId.isBlank()) {
                return@forEach
            }

            userNames[payerId] = expense.paidByName

            expense.splits.forEach { split ->

                val debtorId = split.userId

                if (debtorId.isBlank()) {
                    return@forEach
                }

                userNames[debtorId] = split.userName

                // Person cannot owe themselves
                if (debtorId == payerId) {
                    return@forEach
                }

                val amount = split.amount

                if (amount <= 0.0) {
                    return@forEach
                }

                val key = createKey(
                    debtorId,
                    payerId
                )

                balances[key] =
                    (balances[key] ?: 0.0) + amount
            }
        }

        // --------------------------------------------------
        // 2. Process settlements
        // --------------------------------------------------

        settlements.forEach { settlement ->

            val fromUserId = settlement.fromUserId
            val toUserId = settlement.toUserId

            if (
                fromUserId.isBlank() ||
                toUserId.isBlank()
            ) {
                return@forEach
            }

            if (fromUserId == toUserId) {
                return@forEach
            }

            userNames[fromUserId] = settlement.fromUserName
            userNames[toUserId] = settlement.toUserName

            val key = createKey(
                fromUserId,
                toUserId
            )

            // Settlement reduces the amount that
            // fromUser owes to toUser
            balances[key] =
                (balances[key] ?: 0.0) - settlement.amount
        }

        // --------------------------------------------------
        // 3. Convert balances into GroupBalance objects
        // --------------------------------------------------

        return createFinalBalances(
            balances = balances,
            userNames = userNames
        )
    }

    private fun createFinalBalances(
        balances: Map<String, Double>,
        userNames: Map<String, String>
    ): List<GroupBalance> {

        val result = mutableListOf<GroupBalance>()

        val processedPairs = mutableSetOf<String>()

        balances.forEach { (key, amount) ->

            if (abs(amount) < 0.01) {
                return@forEach
            }

            val parts = key.split("|")

            if (parts.size != 2) {
                return@forEach
            }

            val userA = parts[0]
            val userB = parts[1]

            val pairKey = createPairKey(userA, userB)

            if (processedPairs.contains(pairKey)) {
                return@forEach
            }

            val forwardAmount =
                balances[createKey(userA, userB)] ?: 0.0

            val reverseAmount =
                balances[createKey(userB, userA)] ?: 0.0

            val netAmount =
                forwardAmount - reverseAmount

            if (abs(netAmount) < 0.01) {
                processedPairs.add(pairKey)
                return@forEach
            }

            if (netAmount > 0) {

                result.add(
                    GroupBalance(
                        fromUserId = userA,
                        fromUserName = userNames[userA] ?: "",
                        toUserId = userB,
                        toUserName = userNames[userB] ?: "",
                        amount = netAmount
                    )
                )

            } else {

                result.add(
                    GroupBalance(
                        fromUserId = userB,
                        fromUserName = userNames[userB] ?: "",
                        toUserId = userA,
                        toUserName = userNames[userA] ?: "",
                        amount = abs(netAmount)
                    )
                )
            }

            processedPairs.add(pairKey)
        }

        return result
    }

    private fun createKey(
        fromUserId: String,
        toUserId: String
    ): String {
        return "$fromUserId|$toUserId"
    }

    private fun createPairKey(
        userA: String,
        userB: String
    ): String {

        return if (userA < userB) {
            "$userA|$userB"
        } else {
            "$userB|$userA"
        }
    }

    fun getYouOwe(
        balances: List<GroupBalance>,
        currentUserId: String
    ): Double {

        return balances
            .filter { balance ->
                balance.fromUserId == currentUserId
            }
            .sumOf { it.amount }
    }

    fun getYouGetBack(
        balances: List<GroupBalance>,
        currentUserId: String
    ): Double {

        return balances
            .filter { balance ->
                balance.toUserId == currentUserId
            }
            .sumOf { it.amount }
    }
}