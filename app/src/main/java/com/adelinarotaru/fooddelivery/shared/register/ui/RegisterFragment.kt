package com.adelinarotaru.fooddelivery.shared.register.ui

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.color
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentRegisterBinding
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment

class RegisterFragment :
    BaseFragment<FragmentRegisterBinding, RegisterViewModel>(FragmentRegisterBinding::inflate) {

    override var binding: FragmentRegisterBinding? = null
    override val viewModel = RegisterViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpStringCustomization()
    }

    private fun signUpStringCustomization() {
        val signUp = getString(R.string.login_text)
        val dontHaveAnAccount = getString(R.string.alreadyHaveAnAccount)
        val result = SpannableStringBuilder().append(dontHaveAnAccount).append(STRING_SPACE)
            .color(resources.getColor(R.color.crazy_green, context?.theme)) { append(signUp) }
        binding?.login?.apply {
            text = result
            setOnClickListener { navigateToLogin() }
        }
    }

    private fun navigateToLogin() = findNavController().popBackStack()

    companion object {
        private const val STRING_SPACE = " "
    }
}