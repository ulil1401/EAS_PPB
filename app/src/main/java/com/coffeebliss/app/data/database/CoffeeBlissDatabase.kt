package com.coffeebliss.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.coffeebliss.app.data.dao.MemberDao
import com.coffeebliss.app.data.dao.RedemptionDao
import com.coffeebliss.app.data.dao.TransactionDao
import com.coffeebliss.app.data.model.Member
import com.coffeebliss.app.data.model.Redemption
import com.coffeebliss.app.data.model.Transaction

@Database(
    entities = [Member::class, Transaction::class, Redemption::class],
    version = 3,
    exportSchema = false
)
abstract class CoffeeBlissDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun transactionDao(): TransactionDao
    abstract fun redemptionDao(): RedemptionDao
}
