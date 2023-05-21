package com.markojovanov.debtor

object DebtorPaymentUtil {

    private val existingDebtors = listOf("Marko", "Stefan", "Kosta")

    /**
     * the input is not valid only if all fields are filled.
     * if amount money is zero return false
     * if debtor already exist return false
     */

    fun validateAddingPayment(
        name: String,
        amountMoney: Int?,
        reference: String,
        isRecurringPayment: Boolean?,
        dueDate: String
    ): Boolean {

        if(name.trim().isEmpty() || amountMoney == null || reference.trim().isEmpty() || isRecurringPayment == null || dueDate.trim().isEmpty()){
            return false
        }

        if (name in existingDebtors) {
            return false
        }

        return true
    }
}