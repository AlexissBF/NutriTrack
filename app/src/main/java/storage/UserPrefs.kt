// storage/UserPrefs.kt
package com.example.nutritrack.storage

import android.content.Context

class UserPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_EMAIL = "user_email"
        const val KEY_PASSWORD = "user_password"
        const val KEY_NAME = "user_name"
    }

    fun saveUser(email: String, password: String, name: String) {
        prefs.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
            putString(KEY_NAME, name)
            apply()
        }
    }

    fun getStoredEmail(): String? = prefs.getString(KEY_EMAIL, null)
    fun getStoredPassword(): String? = prefs.getString(KEY_PASSWORD, null)
    fun getStoredName(): String? = prefs.getString(KEY_NAME, "Usuario")

    fun isUserRegistered(): Boolean = prefs.contains(KEY_EMAIL)

    fun clearUser() {
        prefs.edit().clear().apply()
    }
}