// RegisterActivity.kt

package com.example.nutritrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nutritrack.databinding.ActivityRegisterBinding
import com.example.nutritrack.network.RegisterRequest // Importa el modelo de la petición
import com.example.nutritrack.network.RetrofitClient // Importa el cliente de red
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Asignar listener al botón de Registro
        binding.btnRegister.setOnClickListener {
            performRegistration()
        }
    }

    // Función principal para manejar la lógica de registro
    private fun performRegistration() {
        // 1. Obtener datos de la UI
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // 2. Validación básica de campos
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            binding.tvRegisterMessage.text = "Todos los campos son obligatorios."
            return
        }

        // 3. Validación de formato simple (ej: correo electrónico)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tvRegisterMessage.text = "Formato de correo inválido."
            return
        }

        // Limpiar mensaje anterior y mostrar estado de carga
        binding.tvRegisterMessage.text = "Registrando usuario..."

        // 4. Ejecutar la petición de red en una coroutine (asíncrono)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Crear el objeto de la petición de registro
                val registerRequest = RegisterRequest(email, password, name)

                // Llamar al servicio de Retrofit para el registro
                val response = RetrofitClient.authService.register(registerRequest)

                // Volver al hilo principal para actualizar la UI (Dispatchers.Main)
                with(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // ÉXITO en el registro
                        Toast.makeText(this@RegisterActivity, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show()

                        // Redirigir al usuario de vuelta a la pantalla de Login (MainActivity)
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        // Limpiamos la pila de Activities para que no pueda volver al registro
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()

                    } else {
                        // FALLO en el registro (ej: el correo ya existe)
                        val errorBody = response.errorBody()?.string() ?: "Error de registro desconocido."
                        binding.tvRegisterMessage.text = "Fallo: ${response.code()}. El usuario podría ya existir."
                    }
                }
            } catch (e: Exception) {
                // FALLO en la conexión
                with(Dispatchers.Main) {
                    binding.tvRegisterMessage.text = "Error de conexión: No se pudo comunicar con el servidor."
                    e.printStackTrace()
                }
            }
        }
    }
}