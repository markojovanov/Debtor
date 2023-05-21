package com.markojovanov.debtor

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DebtorPaymentUtilTest {

    @Test
    fun `one of the field are empty returns false`() {
        val result = DebtorPaymentUtil.validateAddingPayment(
            "",
            250,
            "Something",
            false,
            "07.09.2021"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `all fields are filled returns true`() {
        val result = DebtorPaymentUtil.validateAddingPayment(
            "Ker",
            450,
            "Something",
            false,
            "07.10.2021"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `name already exist returns false`() {
        val result = DebtorPaymentUtil.validateAddingPayment(
            "Marko",
            850,
            "Something",
            false,
            "07.12.2021"
        )
        assertThat(result).isFalse()
    }


}