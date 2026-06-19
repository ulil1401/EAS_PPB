package com.coffeebliss.app

import android.app.Application
import androidx.room.Room
import com.coffeebliss.app.data.database.CoffeeBlissDatabase
import com.coffeebliss.app.data.repository.CoffeeBlissRepository
import com.coffeebliss.app.util.SessionManager

class CoffeeBlissApplication : Application() {

    val database: CoffeeBlissDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            CoffeeBlissDatabase::class.java,
            "coffee_bliss.db"
        ).build()
    }

    val repository: CoffeeBlissRepository by lazy {
        CoffeeBlissRepository(
            memberDao = database.memberDao(),
            transactionDao = database.transactionDao(),
            redemptionDao = database.redemptionDao()
        )
    }

    val sessionManager: SessionManager by lazy {
        SessionManager(applicationContext)
    }
}
