package com.adelinarotaru.fooddelivery.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<T : ViewBinding>(val lambdaInflater: (LayoutInflater) -> T) :
    Fragment() {

    abstract var binding: T?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = lambdaInflater(layoutInflater).also { binding = it }.root

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}