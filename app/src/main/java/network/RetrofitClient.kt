// network/RetrofitClient.kt

package com.example.nutritrack.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL base de nuestro backend. Base_URL + el endpoint (ej: /api/auth/login)
    // Usamos 10.0.2.2 porque es la IP especial que el Android Emulator usa para conectarse al 'localhost' de tu PC.
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Inicializa el Servicio de Autenticación
    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    // Aquí se agregarían otros servicios a medida que desarrollemos más módulos (ej: MealService)
}