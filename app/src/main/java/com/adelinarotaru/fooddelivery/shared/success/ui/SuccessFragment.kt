package com.adelinarotaru.fooddelivery.shared.success.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.FragmentSuccessBinding
import com.adelinarotaru.fooddelivery.utils.Constants

class SuccessFragment : Fragment() {
    private var binding: FragmentSuccessBinding? = null
    private val navArgs by navArgs<SuccessFragmentArgs>()
    private val userType by lazy { navArgs.userType }
    private fun configureUi(userType: Int?) {
        binding?.apply {
            val topMessageRes =
                if (userType == Constants.CUSTOMER) R.string.enjoy_your_food else R.string.well_done_courier
            topMessage.text = getString(topMessageRes)

            illustration.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    if (userType == Constants.CUSTOMER) R.drawable.illustration_man_eating else R.drawable.illustration_scooter_delivery
                )
            )

            thanksButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentSuccessBinding.inflate(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUi(userType)
        binding
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
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