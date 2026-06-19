package com.coffeebliss.app.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))

    fun formatCurrency(amount: Long): String = currencyFormat.format(amount)

    fun formatDate(timestamp: Long): String = dateFormat.format(Date(timestamp))
}
