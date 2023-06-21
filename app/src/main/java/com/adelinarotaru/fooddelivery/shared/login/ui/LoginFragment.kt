package com.adelinarotaru.fooddelivery.shared.login.ui

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.color
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentLoginBinding
import com.adelinarotaru.fooddelivery.shared.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    override var binding: FragmentLoginBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpStringCustomization()
    }

    private fun signUpStringCustomization() {
        val signUp = getString(R.string.signUp)
        val dontHaveAnAccount = getString(R.string.dontHaveAnAccount)
        val result = SpannableStringBuilder().append(dontHaveAnAccount).append(STRING_SPACE)
            .color(resources.getColor(R.color.crazy_green, context?.theme)) { append(signUp) }
        binding?.register?.apply {
            text = result
            setOnClickListener { navigateToSignUp() }
        }
    }

    private fun navigateToSignUp() =
        findNavController().navigate(LoginFragmentDirections.goToRegister())

    companion object {
        private const val STRING_SPACE = " "
    }
}
