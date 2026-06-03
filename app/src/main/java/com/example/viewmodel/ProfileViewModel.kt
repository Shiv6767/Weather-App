package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileState(
    val isLoggedIn: Boolean = false,
    val name: String = "Guest",
    val email: String = "",
    val isPremium: Boolean = false
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)
        val name = prefs.getString("name", "Guest") ?: "Guest"
        val email = prefs.getString("email", "") ?: ""
        val isPremium = prefs.getBoolean("isPremium", false)

        _uiState.value = ProfileState(
            isLoggedIn = isLoggedIn,
            name = name,
            email = email,
            isPremium = isPremium
        )
    }

    fun mockGoogleSignIn(email: String = "user@gmail.com") {
        val name = if (email.contains("@")) email.substringBefore("@").replaceFirstChar { it.uppercase() } else "User"
        saveProfile(name, email, true)
    }

    fun updateProfile(name: String, email: String) {
        saveProfile(name, email, _uiState.value.isPremium)
    }

    private fun saveProfile(name: String, email: String, isPremium: Boolean) {
        prefs.edit()
            .putBoolean("isLoggedIn", true)
            .putString("name", name)
            .putString("email", email)
            .putBoolean("isPremium", isPremium)
            .apply()

        loadProfile()
    }

    fun signOut() {
        prefs.edit().clear().apply()
        loadProfile()
    }
}
