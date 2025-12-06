// network/AuthService.kt

package com.example.nutritrack.network

import com.example.nutritrack.data.User // Importa el modelo User
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

// --- Modelos de Datos para Peticiones y Respuestas de la API ---

// 1. Datos que se envían al registrarse
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
    // El backend requiere el nombre para crear la cuenta
)

// 2. Datos que se envían al iniciar sesión
data class LoginRequest(
    val email: String,
    val password: String
)

// 3. Definición de la respuesta del login/registro (lo que devuelve el backend)
data class AuthResponse(
    // El token se usará para autenticar futuras peticiones (ej: registrar comida)
    val token: String,
    val user: User
)

// --- Interfaz Retrofit para los Endpoints ---
interface AuthService {

    // Endpoint para CREAR una nueva cuenta de usuario (Registro)
    // @Body indica que se enviará RegisterRequest en el cuerpo de la petición.
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    // Endpoint para INICIAR SESIÓN (Login)
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}