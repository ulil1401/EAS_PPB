package com.coffeebliss.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.coffeebliss.app.data.model.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert
    suspend fun insert(member: Member): Long

    @Update
    suspend fun update(member: Member)

    @Query("SELECT * FROM members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT COUNT(*) FROM members")
    fun getMemberCount(): Flow<Int>

    @Query("SELECT * FROM members WHERE id = :id")
    fun getMemberById(id: Long): Flow<Member?>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberByIdOnce(id: Long): Member?

    @Query("SELECT COUNT(*) FROM members")
    suspend fun getMemberCountOnce(): Int

    @Query("SELECT * FROM members WHERE email = :email LIMIT 1")
    suspend fun getMemberByEmail(email: String): Member?

    @Query("SELECT * FROM members WHERE email = :email AND id != :excludeId LIMIT 1")
    suspend fun getMemberByEmailExcluding(email: String, excludeId: Long): Member?
}
