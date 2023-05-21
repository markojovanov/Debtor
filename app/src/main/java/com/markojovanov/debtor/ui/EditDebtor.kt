package com.markojovanov.debtor.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.markojovanov.debtor.R
import com.markojovanov.debtor.data.model.entities.Debtor
import com.markojovanov.debtor.utils.Constants
import com.markojovanov.debtor.utils.classicSnackBar
import com.markojovanov.debtor.viewmodels.DebtorViewModel
import kotlinx.android.synthetic.main.fragment_edit_debtor_detail.*
import java.text.SimpleDateFormat
import java.util.*


class EditDebtor : Fragment(R.layout.fragment_edit_debtor_detail) {

  private val debtorViewModel: DebtorViewModel by activityViewModels()
  private var debtorID: Int? = null
  private var dueDate: String? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    getDebtorIdFromArgs()
    getDebtorFromDb()
    handleCheckBoxClicks()
    listenForCalendarInput()
    updateDebtor()
  }

  private fun updateDebtor() {
    saveButtonEdit.setOnClickListener {
      if (notValidEntry()) {
        requireView().classicSnackBar("Please enter all information required.")
      } else {
        val debtor = Debtor(
          debtorID,
          owedCheckBoxEdit.isChecked,
          nameInputBoxEdit.text.toString(),
          0.0,
          amountInputBoxEdit.text.toString().toDouble(),
          referenceInputBoxEdit.text.toString(),
          dueDateButtonEdit.text.toString(),
          null,
          false
        )
        debtorViewModel.updateDebtor(debtor)
        navigateToDebtorDetail()
      }
    }
  }

  private fun navigateToDebtorDetail() {
    debtorID?.let {
      Navigation.findNavController(requireView())
        .navigate(EditDebtorDirections.actionEditDebtorToDebtorDetail(it))
    }
  }

  private fun getDebtorIdFromArgs() {
    arguments?.let {
      val args = EditDebtorArgs.fromBundle(it)
      debtorID = args.debtorId
    }
  }

  private fun getDebtorFromDb() {
    debtorID?.let {
      debtorViewModel.getOneDebtor(it).observe(viewLifecycleOwner) { debtor ->
        debtor?.let {
          owedCheckBoxEdit.isChecked = debtor.isOwned
          nameInputBoxEdit.setText(debtor.personName)
          referenceInputBoxEdit.setText(debtor.reference)
          amountInputBoxEdit.setText(debtor.remainingAmountMoney.toString())
          amountInputBoxEdit.apply {
            isFocusable = false
            isEnabled = false
            isCursorVisible = false
          }
          dueDateButtonEdit.text = debtor.dueDate
        }
      }
    }
  }

  private fun handleCheckBoxClicks() {
    oweCheckBoxEdit.setOnClickListener {
      owedCheckBoxEdit.isChecked = !oweCheckBoxEdit.isChecked
    }
    owedCheckBoxEdit.setOnClickListener {
      oweCheckBoxEdit.isChecked = !owedCheckBoxEdit.isChecked
    }
  }

  private fun listenForCalendarInput() {
    val datePicker = DatePickerDialog.OnDateSetListener { datePicker, YEAR, MONTH, DAY ->
      val calendar = Calendar.getInstance()
      calendar.set(YEAR, MONTH, DAY)
      dueDate = formatDate(calendar.time)
      dueDateButtonEdit.text = dueDate
    }

    dueDateButtonEdit.setOnClickListener {
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
    val sdf = SimpleDateFormat(Constants.SDF_PATTERN, Locale.getDefault())
    return sdf.format(date)
  }

  private fun notValidEntry() =
    nameInputBoxEdit.text.isNullOrEmpty() || amountInputBoxEdit.text.isNullOrEmpty() || referenceInputBoxEdit.text.isNullOrEmpty() || dueDateButtonEdit.text.isNullOrEmpty()

  private fun isPaid() = amountInputBoxEdit.text.toString().toDouble() == 0.0
}