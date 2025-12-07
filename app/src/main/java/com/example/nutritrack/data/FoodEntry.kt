package com.example.nutritrack.data

import java.util.Date

data class FoodEntry(
    val userEmail: String,
    val foodName: String,
    val quantity: String, // Usamos String para que el usuario pueda poner "1 taza", "100g", etc.
    val calories: Int,
    val date: Long = System.currentTimeMillis() // Guardamos la fecha del registro
)
