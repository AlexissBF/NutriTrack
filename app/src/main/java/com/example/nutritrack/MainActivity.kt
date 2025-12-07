package com.example.nutritrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nutritrack.data.User // Asumo la ruta
import com.example.nutritrack.databinding.ActivityMainBinding
import com.example.nutritrack.storage.UserPrefs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userPrefs: UserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPrefs = UserPrefs(this)

        // Comprobar si ya hay una sesión activa
        val activeUser = userPrefs.getActiveUser()
        if (activeUser != null) {
            navigateToDashboard(activeUser)
            return // Evita que el resto del onCreate se ejecute
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.tvRegisterLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            binding.tvMessage.text = "Por favor, ingresa tus credenciales."
            return
        }

        val loggedUser = userPrefs.login(email, password)

        if (loggedUser != null) {
            userPrefs.saveSession(loggedUser)
            navigateToDashboard(loggedUser)
        } else {
            binding.tvMessage.text = "Correo o contraseña incorrectos."
        }
    }

    private fun navigateToDashboard(user: User) {
        val isAdmin = user.email.contains("admin", ignoreCase = true) || user.email == "alexis1@gmail.com"

        if (isAdmin) {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("USER_NAME", user.name)
            startActivity(intent)
        }
        finish() // Cierra MainActivity para que el usuario no pueda volver con el botón de atrás
    }
}
