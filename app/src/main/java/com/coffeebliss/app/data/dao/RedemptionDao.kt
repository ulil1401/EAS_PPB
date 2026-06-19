package com.coffeebliss.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.coffeebliss.app.data.model.Redemption
import kotlinx.coroutines.flow.Flow

@Dao
interface RedemptionDao {

    @Insert
    suspend fun insert(redemption: Redemption): Long

    @Query("SELECT * FROM redemptions WHERE memberId = :memberId ORDER BY date DESC")
    fun getRedemptionsByMember(memberId: Long): Flow<List<Redemption>>
}
