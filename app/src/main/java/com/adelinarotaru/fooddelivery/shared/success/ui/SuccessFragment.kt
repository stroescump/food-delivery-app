package com.adelinarotaru.fooddelivery.shared.success.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.adelinarotaru.fooddelivery.utils.Constants

class SuccessFragment : Fragment() {
    private var userType: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userType = it.getInt(Constants.USER_TYPE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUi(userType)
    }

    private fun configureUi(userType: Int?) {
        when (userType) {
            Constants.CUSTOMER -> {}
            Constants.COURIER -> {}
            else -> return
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userType: Int) = SuccessFragment().apply {
            arguments = Bundle().apply {
                putInt(Constants.USER_TYPE, userType)
            }
        }
    }
}