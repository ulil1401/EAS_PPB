package com.coffeebliss.app.data.model

data class TransactionResult(
    val transaction: Transaction,
    val pointsEarned: Int,
    val newTotalPoints: Int
)

data class RedeemResult(
    val redemption: Redemption,
    val rewardName: String,
    val pointsUsed: Int,
    val remainingPoints: Int
)
