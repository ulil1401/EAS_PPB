package com.coffeebliss.app.data.repository

import com.coffeebliss.app.data.dao.MemberDao
import com.coffeebliss.app.data.dao.RedemptionDao
import com.coffeebliss.app.data.dao.TransactionDao
import com.coffeebliss.app.data.model.Member
import com.coffeebliss.app.data.model.RedeemResult
import com.coffeebliss.app.data.model.Redemption
import com.coffeebliss.app.data.model.Reward
import com.coffeebliss.app.data.model.Transaction
import com.coffeebliss.app.data.model.TransactionResult
import com.coffeebliss.app.util.PointCalculator
import kotlinx.coroutines.flow.Flow

class CoffeeBlissRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao,
    private val redemptionDao: RedemptionDao
) {

    fun getAllMembers(): Flow<List<Member>> = memberDao.getAllMembers()

    fun getMemberCount(): Flow<Int> = memberDao.getMemberCount()

    fun getMember(memberId: Long): Flow<Member?> = memberDao.getMemberById(memberId)

    fun getTransactions(memberId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByMember(memberId)

    fun getRedemptions(memberId: Long): Flow<List<Redemption>> =
        redemptionDao.getRedemptionsByMember(memberId)

    suspend fun addMember(name: String, email: String, phone: String): Result<Member> {
        if (name.isBlank() || email.isBlank() || phone.isBlank()) {
            return Result.failure(IllegalArgumentException("Semua field wajib diisi"))
        }

        if (memberDao.getMemberByEmail(email.trim()) != null) {
            return Result.failure(IllegalArgumentException("Email sudah terdaftar"))
        }

        val count = memberDao.getMemberCountOnce()
        val memberNumber = "MBR${String.format("%05d", count + 1)}"

        val member = Member(
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            memberNumber = memberNumber
        )

        val id = memberDao.insert(member)
        return Result.success(member.copy(id = id))
    }

    suspend fun updateMember(
        memberId: Long,
        name: String,
        email: String,
        phone: String,
        photoPath: String?
    ): Result<Member> {
        if (name.isBlank() || email.isBlank() || phone.isBlank()) {
            return Result.failure(IllegalArgumentException("Semua field wajib diisi"))
        }

        val member = memberDao.getMemberByIdOnce(memberId)
            ?: return Result.failure(IllegalArgumentException("Member tidak ditemukan"))

        if (memberDao.getMemberByEmailExcluding(email.trim(), memberId) != null) {
            return Result.failure(IllegalArgumentException("Email sudah digunakan member lain"))
        }

        val updated = member.copy(
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            photoPath = photoPath
        )
        memberDao.update(updated)
        return Result.success(updated)
    }

    suspend fun addTransaction(memberId: Long, amount: Long): Result<TransactionResult> {
        if (amount <= 0) {
            return Result.failure(IllegalArgumentException("Nominal harus lebih dari 0"))
        }

        val member = memberDao.getMemberByIdOnce(memberId)
            ?: return Result.failure(IllegalArgumentException("Member tidak ditemukan"))

        val pointsEarned = PointCalculator.calculatePoints(amount)
        val newTotalPoints = member.points + pointsEarned
        val transaction = Transaction(
            memberId = memberId,
            date = System.currentTimeMillis(),
            amount = amount,
            pointsEarned = pointsEarned
        )

        transactionDao.insert(transaction)
        memberDao.update(member.copy(points = newTotalPoints))

        return Result.success(
            TransactionResult(
                transaction = transaction,
                pointsEarned = pointsEarned,
                newTotalPoints = newTotalPoints
            )
        )
    }

    suspend fun redeemReward(memberId: Long, reward: Reward): Result<RedeemResult> {
        val member = memberDao.getMemberByIdOnce(memberId)
            ?: return Result.failure(IllegalArgumentException("Member tidak ditemukan"))

        if (member.points < reward.pointsRequired) {
            return Result.failure(
                IllegalArgumentException("Poin tidak cukup. Butuh ${reward.pointsRequired} poin")
            )
        }

        val remainingPoints = member.points - reward.pointsRequired
        val redemption = Redemption(
            memberId = memberId,
            date = System.currentTimeMillis(),
            rewardName = reward.name,
            pointsUsed = reward.pointsRequired
        )

        redemptionDao.insert(redemption)
        memberDao.update(member.copy(points = remainingPoints))

        return Result.success(
            RedeemResult(
                redemption = redemption,
                rewardName = reward.name,
                pointsUsed = reward.pointsRequired,
                remainingPoints = remainingPoints
            )
        )
    }
}
