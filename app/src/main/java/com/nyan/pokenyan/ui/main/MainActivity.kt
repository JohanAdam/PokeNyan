package com.nyan.pokenyan.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nyan.pokenyan.R
import com.nyan.pokenyan.databinding.ActivityMainBinding
import com.nyan.pokenyan.viewmodel.main.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}