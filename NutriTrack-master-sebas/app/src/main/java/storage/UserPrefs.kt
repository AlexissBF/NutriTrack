package com.example.nutritrack.storage

import android.content.Context
import com.example.nutritrack.data.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    init {
        val currentUsers = getAllUsers()
        if (currentUsers.isEmpty()) {
            val list = mutableListOf<User>()
            list.add(User("alexis1@gmail.com", "Alexis1", "1234", "admin"))
            list.add(User("gael1@gmail.com", "Gael1", "1234", "user"))
            list.add(User("sebas1@gmail.com", "Sebas1", "1234", "user"))
            list.add(User("max1@gmail.com", "Max1", "1234", "user"))
            saveUsersList(list)
        }
    }

    fun getAllUsers(): MutableList<User> {
        val json = prefs.getString("users_list", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<User>>() {}.type
        return gson.fromJson(json, type)
    }

    fun registerNewUser(user: User): Boolean {
        val users = getAllUsers()
        if (users.any { it.email == user.email }) return false
        users.add(user)
        saveUsersList(users)
        return true
    }

    fun deleteUser(email: String): Boolean {
        val users = getAllUsers()
        val removed = users.removeIf { it.email == email }
        if (removed) saveUsersList(users)
        return removed
    }

    private fun saveUsersList(users: List<User>) {
        prefs.edit().putString("users_list", gson.toJson(users)).apply()
    }

    fun login(email: String, pass: String): User? {
        return getAllUsers().find { it.email == email && it.password == pass }
    }

    fun saveSession(user: User) {
        prefs.edit().putString("active_user", gson.toJson(user)).apply()
    }

    fun getActiveUser(): User? {
        val json = prefs.getString("active_user", null) ?: return null
        return gson.fromJson(json, User::class.java)
    }

    fun clearSession() {
        prefs.edit().remove("active_user").apply()
    }
}