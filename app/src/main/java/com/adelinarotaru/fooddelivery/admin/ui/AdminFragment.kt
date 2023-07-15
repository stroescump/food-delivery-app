package com.adelinarotaru.fooddelivery.admin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.adelinarotaru.fooddelivery.admin.AdminViewModel
import com.adelinarotaru.fooddelivery.admin.ui.statistics.orders.OrdersStatisticsFragment
import com.adelinarotaru.fooddelivery.admin.ui.statistics.revenue.RevenueStatisticsFragment
import com.adelinarotaru.fooddelivery.admin.ui.statistics.trendingitems.TrendingStatisticsFragment
import com.adelinarotaru.fooddelivery.databinding.FragmentAdminBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.setAllCorners

class AdminFragment :
    BaseFragment<FragmentAdminBinding, AdminViewModel>(FragmentAdminBinding::inflate) {

    override val viewModel: AdminViewModel by lazy { AdminViewModel(DependencyProvider.provideDispatcher()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewPager.setAllCorners()
            viewPager.adapter = AdminFragmentAdapter(this@AdminFragment)
            dotsIndicator.attachTo(viewPager)

            totalCustomersNumber.text = "1039"
            totalRevenueNumber.text = "+ $5899"
            totalOrdersNumber.text = "294"
            totalRestaurantsNumber.text = "12"

            goBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    companion object {
        private const val ORDERS = 0
        private const val REVENUE = 1
        private const val TRENDING_ITEMS = 2
    }

    inner class AdminFragmentAdapter<T : ViewBinding, R : BaseViewModel>(fragment: BaseFragment<T, R>) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                ORDERS -> OrdersStatisticsFragment.newInstance()
                REVENUE -> RevenueStatisticsFragment.newInstance()
                TRENDING_ITEMS -> TrendingStatisticsFragment.newInstance()
                else -> throw IllegalStateException("Position $position does not have a Fragment mapping")
            }
        }

    }

}