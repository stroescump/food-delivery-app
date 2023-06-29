package com.adelinarotaru.fooddelivery.shared.landing.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.databinding.FragmentLandingBinding

class LandingFragment : Fragment() {

    private var binding: FragmentLandingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentLandingBinding.inflate(layoutInflater).also { binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            login.setOnClickListener { navigateToLogin() }
            register.setOnClickListener { navigateToRegister() }
        }
    }

    private fun navigateToLogin() =
        findNavController().navigate(LandingFragmentDirections.goToLogin())

    private fun navigateToRegister() =
        findNavController().navigate(LandingFragmentDirections.fromLandingToRegister())

}