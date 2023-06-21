package com.adelinarotaru.fooddelivery.shared.login.ui

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentLoginBinding.inflate(layoutInflater).also { binding = it }.root

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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun navigateToSignUp() =
        findNavController().navigate(LoginFragmentDirections.goToRegister())

    companion object {
        private const val STRING_SPACE = " "
    }
}
