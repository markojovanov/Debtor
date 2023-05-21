package com.markojovanov.debtor.data.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debtors")
data class Debtor(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "debtor_id")
    val debtorId: Int? = null,

    @ColumnInfo(name = "is_owned")
    val isOwned: Boolean,

    @ColumnInfo(name = "debtor_name")
    val personName: String,

    @ColumnInfo(name = "money_amount")
    var totalAmountMoney: Double,

    @ColumnInfo(name = "remaining_amount")
    var remainingAmountMoney: Double,

    @ColumnInfo(name = "reference")
    val reference: String,

    @ColumnInfo(name = "due_date")
    val dueDate: String,

    @ColumnInfo(name = "payment_date")
    var paymentDate: String? = null,

    @ColumnInfo(name = "is_payed")
    var isPayed: Boolean
)