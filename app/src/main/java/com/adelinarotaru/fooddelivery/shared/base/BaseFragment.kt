package com.adelinarotaru.fooddelivery.shared.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.adelinarotaru.fooddelivery.shared.SharedViewModel
import com.adelinarotaru.fooddelivery.utils.hide
import com.adelinarotaru.fooddelivery.utils.show
import com.adelinarotaru.fooddelivery.utils.showError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


abstract class BaseFragment<T : ViewBinding, R : BaseViewModel>(val lambdaInflater: (LayoutInflater) -> T) :
    Fragment() {

    var binding: T? = null
    abstract val viewModel: R
    val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = lambdaInflater(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                setProgress(uiState.loading)
                uiState.error?.let { showError(it) }
            }
        }
    }

    open fun setProgress(isLoading: Boolean) {
        val progressLayout =
            binding?.root?.findViewById<ProgressBar>(com.adelinarotaru.fooddelivery.R.id.layoutLoading)
                ?: return
        if (isLoading) {
            progressLayout.show()
        } else {
            progressLayout.hide()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}