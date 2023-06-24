package com.adelinarotaru.fooddelivery.shared.base

import androidx.lifecycle.ViewModel
import com.adelinarotaru.fooddelivery.shared.ErrorHandlingDelegateImpl
import com.adelinarotaru.fooddelivery.shared.ErrorHandlinglDelegate

abstract class BaseViewModel : ViewModel(), ErrorHandlinglDelegate by ErrorHandlingDelegateImpl()