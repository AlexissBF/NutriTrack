package com.example.nutritrack.storage

import android.content.Context

class RecordPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("NutriTrackRecords", Context.MODE_PRIVATE)

    // RF02 & RF03: Guardar registros ligados específicamente al correo del usuario [cite: 81, 85]
    fun saveLastFoodEntry(userEmail: String, name: String, calories: Int) {
        val entry = "$name ($calories kcal)"
        prefs.edit().putString("${userEmail}_last_food", entry).apply()
    }

    fun saveLastActivityEntry(userEmail: String, name: String, calories: Int) {
        val entry = "$name ($calories kcal gastadas)"
        prefs.edit().putString("${userEmail}_last_activity", entry).apply()
    }

    // Recuperar solo los datos del usuario actual [cite: 98]
    fun getLastFoodEntry(userEmail: String): String =
        prefs.getString("${userEmail}_last_food", "Sin registros") ?: "Sin registros"

    fun getLastActivityEntry(userEmail: String): String =
        prefs.getString("${userEmail}_last_activity", "Sin registros") ?: "Sin registros"
}