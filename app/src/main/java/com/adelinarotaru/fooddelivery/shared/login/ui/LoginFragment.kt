package com.adelinarotaru.fooddelivery.shared.login.ui

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.color
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentLoginBinding
import com.adelinarotaru.fooddelivery.driver.ui.dashboard.models.CourierDashboardArgs
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.login.data.LoginRepositoryImpl
import com.adelinarotaru.fooddelivery.utils.Constants
import com.adelinarotaru.fooddelivery.utils.showError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment :
    BaseFragment<FragmentLoginBinding, LoginViewModel>(FragmentLoginBinding::inflate) {
    override val viewModel by lazy { LoginViewModel(LoginRepositoryImpl(DependencyProvider.provideLoginApi())) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginSuccess.collectLatest { loginRes ->
                if (loginRes == null) return@collectLatest
                sharedViewModel.updateUserId(loginRes.userId.toString())
                when (loginRes.userType) {
                    Constants.CUSTOMER -> navigateToCustomerDashboard(
                        loginRes.userId,
                        loginRes.userName
                    )

                    Constants.COURIER -> navigateToCourierDashboard(
                        loginRes.userId,
                        loginRes.userName
                    )

                    Constants.ADMIN -> navigateToAdminDashboard(loginRes.userId)
                }
            }
        }
        signUpStringCustomization()
    }

    private fun signUpStringCustomization() {
        val signUpText = getString(R.string.signUp)
        val dontHaveAnAccount = getString(R.string.dontHaveAnAccount)
        val result = SpannableStringBuilder().append(dontHaveAnAccount).append(STRING_SPACE)
            .color(resources.getColor(R.color.crazy_green, context?.theme)) { append(signUpText) }
        binding?.apply {
            register.text = result
            register.setOnClickListener { navigateToSignUp() }

            login.setOnClickListener {
                try {
                    val email = email.text?.toString()?.trim()
                        ?: throw IllegalArgumentException("Email does not respect formatting.")
                    val password = password.text?.toString()?.trim()
                        ?: throw IllegalArgumentException("Please make sure to fill in the password.")
                    viewModel.login(email, password)
                } catch (e: IllegalArgumentException) {
                    showError(e)
                }
            }
        }
    }

    private fun navigateToSignUp() =
        findNavController().navigate(LoginFragmentDirections.goToRegister())

    private fun navigateToCustomerDashboard(userId: Int, userName: String) =
        findNavController().navigate(
            LoginFragmentDirections.goToCustomerDashboard(
                userId,
                userName
            )
        )

    private fun navigateToCourierDashboard(userId: Int, userName: String) =
        findNavController().navigate(
            LoginFragmentDirections.goToDriverDashboard(
                CourierDashboardArgs(userName, userId.toString())
            )
        )

    private fun navigateToAdminDashboard(userId: Int) =
        findNavController().navigate(LoginFragmentDirections.goToAdminDashboard(userId))

    companion object {
        private const val STRING_SPACE = " "
    }
}
