// MainActivity.kt

package com.example.nutritrack

import android.content.Intent // Importar Intent para la navegación
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nutritrack.databinding.ActivityMainBinding
import com.example.nutritrack.network.LoginRequest
import com.example.nutritrack.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Variable para manejar las vistas de forma segura (View Binding)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Asignar listener al botón de Login
        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        // 3. Asignar listener al enlace de Registro (Navegación Corregida)
        binding.tvRegisterLink.setOnClickListener {
            // --- CÓDIGO CORREGIDO PARA INICIAR RegisterActivity ---
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            // ----------------------------------------------------
        }
    }

    // Función principal para la lógica de inicio de sesión
    private fun performLogin() {
        // 1. Obtener datos de la UI
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // 2. Validación básica de campos
        if (email.isEmpty() || password.isEmpty()) {
            binding.tvMessage.text = "Por favor, ingresa correo y contraseña."
            return
        }

        // Limpiar mensaje anterior
        binding.tvMessage.text = "Iniciando sesión..."

        // 3. Ejecutar la petición de red en una coroutine (asíncrono)
        // Usamos Dispatchers.IO para operaciones de red
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Crear el objeto de la petición
                val loginRequest = LoginRequest(email, password)

                // Llamar al servicio de Retrofit
                val response = RetrofitClient.authService.login(loginRequest)

                // Volver al hilo principal para actualizar la UI (Dispatchers.Main)
                with(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val authResponse = response.body()!!
                        // ÉXITO en la autenticación
                        binding.tvMessage.text = "¡Sesión iniciada con éxito! Usuario: ${authResponse.user.name}"

                        // Aquí se guardaría el token y se navegaría al Dashboard (DashboardActivity)
                        Toast.makeText(this@MainActivity, "Token recibido: ${authResponse.token.take(10)}...", Toast.LENGTH_LONG).show()

                        // --- Redirigir al Dashboard (Una vez que crees esa Activity) ---
                        // val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                        // startActivity(intent)
                        // finish()
                        // -----------------------------------------------------------------

                    } else {
                        // FALLO en la autenticación (ej: 401 Unauthorized)
                        val errorBody = response.errorBody()?.string() ?: "Credenciales inválidas"
                        binding.tvMessage.text = "Error de Login: ${response.code()}"

                    }
                }
            } catch (e: Exception) {
                // FALLO en la conexión (ej: el servidor no está corriendo o hay un error de red)
                with(Dispatchers.Main) {
                    binding.tvMessage.text = "Error de conexión: Verifica que el servidor esté activo en 10.0.2.2:3000"
                    e.printStackTrace()
                }
            }
        }
    }
}