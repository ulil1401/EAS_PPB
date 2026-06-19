package com.coffeebliss.app.data.repository

import com.coffeebliss.app.data.dao.MemberDao
import com.coffeebliss.app.data.dao.RedemptionDao
import com.coffeebliss.app.data.dao.TransactionDao
import com.coffeebliss.app.data.model.Member
import com.coffeebliss.app.data.model.Redemption
import com.coffeebliss.app.data.model.Reward
import com.coffeebliss.app.data.model.Transaction
import com.coffeebliss.app.util.PointCalculator
import kotlinx.coroutines.flow.Flow

class CoffeeBlissRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao,
    private val redemptionDao: RedemptionDao
) {

    suspend fun registerMember(name: String, email: String, phone: String): Result<Member> {
        if (name.isBlank() || email.isBlank() || phone.isBlank()) {
            return Result.failure(IllegalArgumentException("Semua field wajib diisi"))
        }

        val existing = memberDao.getMemberByEmail(email.trim())
        if (existing != null) {
            return Result.failure(IllegalArgumentException("Email sudah terdaftar"))
        }

        val memberCount = memberDao.getMemberCount()
        val memberNumber = "CB${String.format("%04d", memberCount + 1)}"

        val member = Member(
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            memberNumber = memberNumber
        )

        val id = memberDao.insert(member)
        return Result.success(member.copy(id = id))
    }

    suspend fun login(email: String, phone: String): Result<Member> {
        val member = memberDao.login(email.trim(), phone.trim())
        return if (member != null) {
            Result.success(member)
        } else {
            Result.failure(IllegalArgumentException("Email atau nomor HP tidak ditemukan"))
        }
    }

    fun getMember(memberId: Long): Flow<Member?> = memberDao.getMemberById(memberId)

    fun getTransactions(memberId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByMember(memberId)

    fun getRedemptions(memberId: Long): Flow<List<Redemption>> =
        redemptionDao.getRedemptionsByMember(memberId)

    suspend fun addTransaction(memberId: Long, amount: Long): Result<Transaction> {
        if (amount <= 0) {
            return Result.failure(IllegalArgumentException("Nominal harus lebih dari 0"))
        }

        val member = memberDao.getMemberByIdOnce(memberId)
            ?: return Result.failure(IllegalArgumentException("Member tidak ditemukan"))

        val pointsEarned = PointCalculator.calculatePoints(amount)
        val transaction = Transaction(
            memberId = memberId,
            date = System.currentTimeMillis(),
            amount = amount,
            pointsEarned = pointsEarned
        )

        transactionDao.insert(transaction)
        memberDao.update(member.copy(totalPoints = member.totalPoints + pointsEarned))

        return Result.success(transaction)
    }

    suspend fun redeemReward(memberId: Long, reward: Reward): Result<Redemption> {
        val member = memberDao.getMemberByIdOnce(memberId)
            ?: return Result.failure(IllegalArgumentException("Member tidak ditemukan"))

        if (member.totalPoints < reward.pointsRequired) {
            return Result.failure(
                IllegalArgumentException("Poin tidak cukup. Butuh ${reward.pointsRequired} poin")
            )
        }

        val redemption = Redemption(
            memberId = memberId,
            date = System.currentTimeMillis(),
            rewardName = reward.name,
            pointsUsed = reward.pointsRequired
        )

        redemptionDao.insert(redemption)
        memberDao.update(
            member.copy(totalPoints = member.totalPoints - reward.pointsRequired)
        )

        return Result.success(redemption)
    }
}
