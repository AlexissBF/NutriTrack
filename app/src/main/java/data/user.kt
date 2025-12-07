package com.example.nutritrack.data

data class User(
    val email: String,
    val name: String,
    val password: String, // Clave para validación local
    val role: String = "user"
)