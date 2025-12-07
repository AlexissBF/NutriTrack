package com.example.nutritrack.data

import java.util.Date

data class ActivityEntry(
    val userEmail: String,
    val activityName: String,
    val duration: String, // Usamos String para que el usuario pueda poner "30 minutos", "1 hora", etc.
    val caloriesBurned: Int,
    val date: Long = System.currentTimeMillis() // Guardamos la fecha del registro
)
