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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefs = UserPrefs(this)

        // 1. Asignar listener al botón de Login
        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        // 2. Asignar listener al enlace de Registro
        binding.tvRegisterLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        // 1. Obtener datos de la UI
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // 2. Validación básica de campos
        if (email.isEmpty() || password.isEmpty()) {
            binding.tvMessage.text = "Por favor, ingresa correo y contraseña."
            return
        }

        // 3. Obtener credenciales almacenadas localmente
        val storedEmail = userPrefs.getStoredEmail()
        val storedPassword = userPrefs.getStoredPassword()
        val storedName = userPrefs.getStoredName()

        // 4. Simulación de validación (LOCAL)
        if (email == storedEmail && password == storedPassword) {
            // ÉXITO en la autenticación
            binding.tvMessage.text = "¡Sesión iniciada! Bienvenido, $storedName"

            // Redirigir al Dashboard (DashboardActivity)
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("USER_NAME", storedName)
            startActivity(intent)
            finish()

        } else if (!userPrefs.isUserRegistered()) {
            binding.tvMessage.text = "Error: No hay cuentas registradas."
        } else {
            // FALLO en la autenticación
            binding.tvMessage.text = "Credenciales inválidas. Intenta de nuevo."
        }
    }
}