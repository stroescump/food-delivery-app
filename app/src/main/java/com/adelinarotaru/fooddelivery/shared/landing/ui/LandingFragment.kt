package com.adelinarotaru.fooddelivery.shared.landing.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.databinding.FragmentLandingBinding
import com.adelinarotaru.fooddelivery.shared.BaseFragment

class LandingFragment : BaseFragment<FragmentLandingBinding>(FragmentLandingBinding::inflate) {

    override var binding: FragmentLandingBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            login.setOnClickListener { navigateToLogin() }
        }
    }

    private fun navigateToLogin() =
        findNavController().navigate(LandingFragmentDirections.goToLogin())

}