// storage/RecordPrefs.kt
package com.example.nutritrack.storage

import android.content.Context

class RecordPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("NutriTrackRecords", Context.MODE_PRIVATE)

    companion object {
        const val KEY_LAST_FOOD = "last_food_entry"
        const val KEY_LAST_ACTIVITY = "last_activity_entry"
    }

    // Guarda el último registro de comida
    fun saveLastFoodEntry(name: String, calories: Int) {
        val entry = "$name (${calories} kcal)"
        prefs.edit().putString(KEY_LAST_FOOD, entry).apply()
    }

    // Guarda el último registro de actividad (para el siguiente módulo)
    fun saveLastActivityEntry(name: String, calories: Int) {
        val entry = "$name (${calories} kcal gastadas)"
        prefs.edit().putString(KEY_LAST_ACTIVITY, entry).apply()
    }

    // Obtiene el último registro para mostrarlo en el Dashboard
    fun getLastFoodEntry(): String = prefs.getString(KEY_LAST_FOOD, "N/A") ?: "N/A"
    fun getLastActivityEntry(): String = prefs.getString(KEY_LAST_ACTIVITY, "N/A") ?: "N/A"
}