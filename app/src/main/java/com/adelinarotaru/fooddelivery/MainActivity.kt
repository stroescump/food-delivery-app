package com.adelinarotaru.fooddelivery

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.adelinarotaru.fooddelivery.shared.SharedViewModel
import com.adelinarotaru.fooddelivery.databinding.ActivityMainBinding
import com.adelinarotaru.fooddelivery.utils.NetworkMonitor
import com.adelinarotaru.fooddelivery.utils.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val networkMonitor: NetworkMonitor by lazy { NetworkMonitor(applicationContext) }
    private val sharedViewModel by viewModels<SharedViewModel>()

    private lateinit var navController: NavController
    private lateinit var snackbar: Snackbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupNavController()
        provideNetworkMonitor()
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    private fun provideNetworkMonitor() = lifecycleScope.launch {
        networkMonitor.networkFlow.collect { isNetworkAvailable ->
            if (isNetworkAvailable) {
                if (this@MainActivity::snackbar.isInitialized) {
                    snackbar.dismiss()
                }
            } else {
                snackbar = showPermanentMessage(binding.root, getString(R.string.error_no_internet))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.startMonitoring()
    }

    override fun onPause() {
        super.onPause()
        networkMonitor.stopMonitoring()
    }

    private fun showPermanentMessage(view: View, message: String): Snackbar {
        return Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).run {
            show()
            this
        }
    }

}