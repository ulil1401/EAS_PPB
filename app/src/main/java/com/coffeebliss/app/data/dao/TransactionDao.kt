package com.coffeebliss.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.coffeebliss.app.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: Transaction): Long

    @Query("SELECT * FROM transactions WHERE memberId = :memberId ORDER BY date DESC")
    fun getTransactionsByMember(memberId: Long): Flow<List<Transaction>>
}
