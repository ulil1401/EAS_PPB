package com.coffeebliss.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val memberNumber: String,
    val status: String = "Active",
    val totalPoints: Int = 0
)
