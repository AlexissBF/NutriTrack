package com.example.nutritrack.storage

import android.content.Context
import com.example.nutritrack.data.FoodEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecordPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("NutriTrackRecords", Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Obtiene la lista completa de alimentos para un usuario específico.
     * @param userEmail El email del usuario para buscar sus registros.
     * @return Una lista mutable de FoodEntry. Si no hay registros, devuelve una lista vacía.
     */
    fun getFoodEntriesForUser(userEmail: String): MutableList<FoodEntry> {
        // La clave es única para cada usuario, ej: "gael1@gmail.com_food_entries"
        val json = prefs.getString("${userEmail}_food_entries", null)
        if (json == null) {
            return mutableListOf()
        }
        val type = object : TypeToken<MutableList<FoodEntry>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * Añade un nuevo registro de comida a la lista de un usuario.
     * @param userEmail El email del usuario al que pertenece el registro.
     * @param foodEntry El objeto FoodEntry que se va a añadir.
     */
    fun addFoodEntry(userEmail: String, foodEntry: FoodEntry) {
        // 1. Obtener la lista actual de comidas
        val foodList = getFoodEntriesForUser(userEmail)
        // 2. Añadir el nuevo registro a la lista
        foodList.add(foodEntry)
        // 3. Convertir la lista actualizada a JSON
        val json = gson.toJson(foodList)
        // 4. Guardar el JSON en SharedPreferences
        prefs.edit().putString("${userEmail}_food_entries", json).apply()
    }

    // NOTA: He eliminado los métodos saveLastFoodEntry, getLastFoodEntry, etc.
    // para reemplazarlos por este nuevo sistema que guarda una lista completa.
    // Si necesitas guardar también un historial de actividades físicas, seguiríamos un patrón similar.
}
