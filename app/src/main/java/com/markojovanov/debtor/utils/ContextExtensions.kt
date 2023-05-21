package com.markojovanov.debtor.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.classicSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}
