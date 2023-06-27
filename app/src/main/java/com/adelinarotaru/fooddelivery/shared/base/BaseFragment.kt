package com.adelinarotaru.fooddelivery.shared.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.adelinarotaru.fooddelivery.utils.showError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


abstract class BaseFragment<T : ViewBinding, R : BaseViewModel>(val lambdaInflater: (LayoutInflater) -> T) :
    Fragment() {

    abstract var binding: T?
    abstract val viewModel: R

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = lambdaInflater(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { error -> error?.let { showError(it) } }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}