package com.adelinarotaru.fooddelivery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adelinarotaru.fooddelivery.databinding.ActivityMainBinding
import com.adelinarotaru.fooddelivery.utils.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}