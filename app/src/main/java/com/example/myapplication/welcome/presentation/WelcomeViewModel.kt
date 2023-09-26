package com.example.myapplication.welcome.presentation

import androidx.lifecycle.ViewModel

class WelcomeViewModel(private val name: String) : ViewModel() {
    fun getName(): String = name
}