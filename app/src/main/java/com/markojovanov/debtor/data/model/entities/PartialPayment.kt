package com.markojovanov.debtor.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partial_payment")
data class PartialPayment(
    @PrimaryKey(autoGenerate = true)
    val paymentId: Int?,
    val date: String,
    val amount: Double,
    val debtorId: Int

)
