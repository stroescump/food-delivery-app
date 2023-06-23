package com.adelinarotaru.fooddelivery.utils

import androidx.fragment.app.Fragment
import com.adelinarotaru.fooddelivery.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.showGenericError() =
    Snackbar.make(requireView(), getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show()

fun Fragment.showError(error: Throwable?) {
    Snackbar.make(
        requireView(),
        error?.message ?: getString(R.string.generic_error),
        Snackbar.LENGTH_SHORT
    ).show()
}