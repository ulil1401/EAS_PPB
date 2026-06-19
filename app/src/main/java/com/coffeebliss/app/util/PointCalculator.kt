package com.coffeebliss.app.util

object PointCalculator {
    private const val POINTS_PER_RUPIAH = 10_000L

    fun calculatePoints(amount: Long): Int {
        if (amount <= 0) return 0
        return (amount / POINTS_PER_RUPIAH).toInt()
    }
}
