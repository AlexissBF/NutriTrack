package com.example.nutritrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nutritrack.databinding.ActivityMainBinding
import com.example.nutritrack.storage.UserPrefs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userPrefs: UserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Configuración de ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Inicialización de almacenamiento local
        userPrefs = UserPrefs(this)

        // 3. Listener para el botón de Login
        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        // 4. Listener para ir a la pantalla de Registro
        binding.tvRegisterLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        // Obtener datos ingresados
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Validación de campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            binding.tvMessage.text = "Por favor, ingresa tus credenciales."
            return
        }

        // RF01: Intento de login consultando la lista de usuarios en UserPrefs
        val loggedUser = userPrefs.login(email, password)

        if (loggedUser != null) {
            // Guardar el usuario en la sesión activa
            userPrefs.saveSession(loggedUser)


            // Determinamos si es Admin (Alexis1) o contiene la palabra 'admin'
            val isAdmin = email.contains("admin", ignoreCase = true) || email == "alexis1@gmail.com"

            if (isAdmin) {
                // RF06: Navegar al Panel Administrativo
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            } else {
                // Navegar al Dashboard General del Usuario
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("USER_NAME", loggedUser.name)
                startActivity(intent)
            }

            finish() // Cerramos el Login para seguridad

        } else {
            // Error si no se encuentra el usuario en la lista local
            binding.tvMessage.text = "Correo o contraseña incorrectos."
        }
    }
}