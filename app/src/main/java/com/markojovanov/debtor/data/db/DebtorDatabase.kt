package com.markojovanov.debtor.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.markojovanov.debtor.data.model.entities.Debtor
import com.markojovanov.debtor.data.model.entities.PartialPayment

@Database(entities = [Debtor::class, PartialPayment::class], version = 1, exportSchema = false)
abstract class DebtorDatabase: RoomDatabase() {
    abstract fun debtorDao(): DebtorDao
}