package com.example.nutritrack.storage

import android.content.Context
import com.example.nutritrack.data.ActivityEntry
import com.example.nutritrack.data.FoodEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecordPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("NutriTrackRecords", Context.MODE_PRIVATE)
    private val gson = Gson()

    // --- Métodos para el registro de Alimentos ---

    fun getFoodEntriesForUser(userEmail: String): MutableList<FoodEntry> {
        val json = prefs.getString("${userEmail}_food_entries", null)
        if (json == null) {
            return mutableListOf()
        }
        val type = object : TypeToken<MutableList<FoodEntry>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addFoodEntry(userEmail: String, foodEntry: FoodEntry) {
        val foodList = getFoodEntriesForUser(userEmail)
        foodList.add(foodEntry)
        val json = gson.toJson(foodList)
        prefs.edit().putString("${userEmail}_food_entries", json).apply()
    }

    // --- Métodos para el registro de Actividad Física ---

    fun getActivityEntriesForUser(userEmail: String): MutableList<ActivityEntry> {
        val json = prefs.getString("${userEmail}_activity_entries", null)
        if (json == null) {
            return mutableListOf()
        }
        val type = object : TypeToken<MutableList<ActivityEntry>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addActivityEntry(userEmail: String, activityEntry: ActivityEntry) {
        val activityList = getActivityEntriesForUser(userEmail)
        activityList.add(activityEntry)
        val json = gson.toJson(activityList)
        prefs.edit().putString("${userEmail}_activity_entries", json).apply()
    }
}
