package com.adelinarotaru.fooddelivery.shared.base

import androidx.lifecycle.ViewModel
import com.adelinarotaru.fooddelivery.shared.StatefulViewModelDelegateImpl
import com.adelinarotaru.fooddelivery.shared.StatefulViewModelDelegate

abstract class BaseViewModel : ViewModel(), StatefulViewModelDelegate by StatefulViewModelDelegateImpl()