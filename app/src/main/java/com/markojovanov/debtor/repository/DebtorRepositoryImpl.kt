package com.markojovanov.debtor.repository

import com.markojovanov.debtor.data.model.entities.Debtor

interface DebtorRepositoryImpl {
    fun getAllDebtors(): List<Debtor>
}