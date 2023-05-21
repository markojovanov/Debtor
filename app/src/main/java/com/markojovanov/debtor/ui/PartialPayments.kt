package com.markojovanov.debtor.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.markojovanov.debtor.R
import com.markojovanov.debtor.data.model.entities.Debtor
import com.markojovanov.debtor.data.model.entities.PartialPayment
import com.markojovanov.debtor.utils.DateConverter.convertDateToSimpleFormatString
import com.markojovanov.debtor.utils.classicSnackBar
import com.markojovanov.debtor.viewmodels.DebtorViewModel
import kotlinx.android.synthetic.main.fragment_partial_payments.*
import java.util.*

class PartialPayments : Fragment(R.layout.fragment_partial_payments) {

  var debtorId: Int? = null
  lateinit var debtor: Debtor
  private val debtorViewModel: DebtorViewModel by activityViewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    getDebtorNameFromArgs()
    getDebtorObject()

    savePartialBtn.setOnClickListener {
      if (partialAmountMoneyInput.text.isNullOrEmpty() || partialAmountMoneyInput.text.toString()
          .toDouble() == 0.0
      ) {
        requireView().classicSnackBar(getString(R.string.mandatory_amount_info_message))
      } else {
        val pPayment = PartialPayment(
          null,
          convertDateToSimpleFormatString(Calendar.getInstance().time),
          partialAmountMoneyInput.text.toString().toDouble(),
          debtorId!!
        )

        if (debtor.remainingAmountMoney < partialAmountMoneyInput.text.toString().toDouble()) {
          requireView().classicSnackBar(getString(R.string.exceeded_limit_info_message))
        } else {
          debtor.remainingAmountMoney -= partialAmountMoneyInput.text.toString().toDouble()
          debtor.totalAmountMoney += partialAmountMoneyInput.text.toString().toDouble()
          debtorViewModel.addPartialPayment(pPayment)
          debtorViewModel.updateDebtor(debtor)
          Navigation.findNavController(requireView())
            .navigate(PartialPaymentsDirections.actionPartialPaymentsToDebtorDetail(debtorId!!))
        }
      }


    }
  }

  private fun getDebtorObject() {
    debtorViewModel.getOneDebtor(debtorId!!).observe(viewLifecycleOwner) { deb ->
      deb?.let {
        debtor = it
      }
    }
  }

  private fun getDebtorNameFromArgs() {
    arguments?.let {
      val args = PartialPaymentsArgs.fromBundle(it)
      debtorId = args.debtorId
    }
  }
}