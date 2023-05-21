package com.markojovanov.debtor.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.markojovanov.debtor.R
import com.markojovanov.debtor.data.model.entities.Debtor
import com.markojovanov.debtor.utils.Constants.SDF_PATTERN
import com.markojovanov.debtor.utils.classicSnackBar
import com.markojovanov.debtor.viewmodels.DebtorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_payment.*
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddPayment : Fragment(R.layout.fragment_add_payment) {

  private var dueDate: String? = null
  private val debtorViewModel: DebtorViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setInitialCheckBoxesState()
    handleCheckBoxClicks()
    listenForCalendarInput()
    listenToSaveButton()
  }

  private fun setInitialCheckBoxesState() {
    owedCheckBox.isChecked = false
    oweCheckBox.isChecked = true
  }

  private fun handleCheckBoxClicks() {
    oweCheckBox.setOnClickListener {
      owedCheckBox.isChecked = !oweCheckBox.isChecked
    }
    owedCheckBox.setOnClickListener {
      oweCheckBox.isChecked = !owedCheckBox.isChecked
    }
  }

  private fun listenToSaveButton() {
    saveButton.setOnClickListener {
      if (nameInputBox.text.isNullOrEmpty() || amountInputBox.text.isNullOrEmpty() || amountInputBox.text.toString()
          .toDouble() == 0.0 || referenceInputBox.text.isNullOrEmpty() || dueDate.isNullOrEmpty()
      ) {
        requireView().classicSnackBar("Please provide all information before saving.")
      } else if (debtorViewModel.isDebtorAlreadyExist(nameInputBox.text.toString().trim()) == 1) {
        requireView().classicSnackBar("This name already exist. Please provide different name.")
      } else if (amountInputBox.text.toString().toDouble() > 100000) {
        requireView().classicSnackBar("Amount limit is 100k.")
      } else {
        val debtor = Debtor(
          null,
          oweCheckBox.isChecked,
          nameInputBox.text.toString(),
          0.0,    //total paid up to date
          amountInputBox.text.toString().toDouble(),
          referenceInputBox.text.toString(),
          dueDate.toString(),
          null,
          false
        )
        debtorViewModel.addDebtor(debtor)
        Navigation.findNavController(requireView())
          .navigate(AddPaymentDirections.actionAddPaymentToPayments())
      }
    }
  }

  private fun listenForCalendarInput() {
    val datePicker = DatePickerDialog.OnDateSetListener { datePicker, YEAR, MONTH, DAY ->
      val calendar = Calendar.getInstance()
      calendar.set(YEAR, MONTH, DAY)
      dueDate = formatDate(calendar.time)
      dueDateButton.text = dueDate
    }

    dueDateButton.setOnClickListener {
      DatePickerDialog(
        requireContext(),
        AlertDialog.THEME_DEVICE_DEFAULT_DARK,
        datePicker,
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
      ).apply {
        this.datePicker.minDate = System.currentTimeMillis() - 1000
      }
        .show()
    }
  }

  private fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat(SDF_PATTERN, Locale.getDefault())
    return sdf.format(date)
  }
}