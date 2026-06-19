package com.coffeebliss.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "redemptions",
    foreignKeys = [
        ForeignKey(
            entity = Member::class,
            parentColumns = ["id"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("memberId")]
)
data class Redemption(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val memberId: Long,
    val date: Long,
    val rewardName: String,
    val pointsUsed: Int
)
