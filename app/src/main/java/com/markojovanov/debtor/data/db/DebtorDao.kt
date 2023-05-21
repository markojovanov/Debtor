package com.markojovanov.debtor.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.markojovanov.debtor.data.model.entities.Debtor
import com.markojovanov.debtor.data.model.entities.PartialPayment

@Dao
interface DebtorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDebtor(debtor: Debtor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPPayment(partialPayment: PartialPayment)

    @Query("SELECT * FROM debtors")
    suspend fun getAllDebtors(): List<Debtor>

    @Query("SELECT COUNT() FROM debtors WHERE debtor_name = :debtorName")
    suspend fun isDebtorExisting(debtorName: String): Int

    @Query("SELECT * FROM debtors WHERE is_payed = 0")
     fun getAllUnpaidDebtors(): LiveData<List<Debtor>>

    @Query("SELECT * FROM debtors WHERE is_payed = 1")
     fun getAllPaidDebtors(): LiveData<List<Debtor>>

    @Query("SELECT * FROM debtors WHERE debtor_id = :debtorId LIMIT 1")
     fun getSingleDebtor(debtorId: Int): LiveData<Debtor>

    @Query("SELECT remaining_amount FROM debtors WHERE is_owned = 1 AND  is_payed = 0")
    suspend fun getIncomeAmount(): List<Double>

    @Query("SELECT remaining_amount FROM debtors WHERE is_owned = 0 AND is_payed = 0")
    suspend fun getOutcomeAmount(): List<Double>

    @Transaction
    @Query("SELECT * FROM debtors d INNER JOIN partial_payment p ON d.debtor_id = p.debtorId WHERE d.debtor_id = :debtorId")
     fun getPPaymentsForDebtor(debtorId: Int): LiveData<List<PartialPayment>>

     @Query("DELETE FROM debtors WHERE debtor_id = :debtorId")
     fun deleteDebtor(debtorId: Int)

     @Query("DELETE FROM partial_payment WHERE debtorId = :debtorId")
     fun deletePPaymentsForDebtor(debtorId: Int)

    @Update
    suspend fun updateDebtor(debtor: Debtor)

    @Delete
    suspend fun deletePartialPayment(partialPay: PartialPayment)

}