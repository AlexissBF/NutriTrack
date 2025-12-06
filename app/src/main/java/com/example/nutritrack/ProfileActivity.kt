package com.example.nutritrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nutritrack.databinding.ActivityProfileBinding
import com.example.nutritrack.storage.UserPrefs

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userPrefs: UserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefs = UserPrefs(this)

        // 1. Cargar los datos actuales al iniciar
        loadCurrentProfile()

        // 2. Asignar listener al botón de guardar
        binding.btnSaveProfile.setOnClickListener {
            saveProfileChanges()
        }
    }

    // Función para cargar los datos guardados en UserPrefs
    private fun loadCurrentProfile() {
        // En nuestra implementación local, solo guardamos nombre, email y password.
        // Simularemos la edad y peso ya que no están en UserPrefs.saveUser().

        val currentName = userPrefs.getStoredName()

        binding.etProfileName.setText(currentName)
        // Estos campos no se guardaron, se usan valores por defecto para simular
        binding.etProfileAge.setText("25")
        binding.etProfileWeight.setText("75.0")

        binding.tvProfileMessage.text = "Datos cargados."
        binding.tvProfileMessage.setTextColor(getColor(android.R.color.darker_gray))
    }

    // Función para guardar los cambios en UserPrefs
    private fun saveProfileChanges() {
        val newName = binding.etProfileName.text.toString().trim()
        val ageStr = binding.etProfileAge.text.toString().trim()
        val weightStr = binding.etProfileWeight.text.toString().trim()

        // 1. Validación
        if (newName.isEmpty() || ageStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos deben estar llenos.", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Guardar (Solo actualizaremos el nombre en UserPrefs, ya que es el único dato editable guardado)
        // Nota: En una app real, se actualizarían todos los campos

        val storedEmail = userPrefs.getStoredEmail() ?: ""
        val storedPassword = userPrefs.getStoredPassword() ?: ""

        if (storedEmail.isNotEmpty() && storedPassword.isNotEmpty()) {
            userPrefs.saveUser(storedEmail, storedPassword, newName)
            Toast.makeText(this, "¡Perfil actualizado con éxito!", Toast.LENGTH_LONG).show()

            binding.tvProfileMessage.text = "Perfil actualizado: $newName"
            binding.tvProfileMessage.setTextColor(getColor(android.R.color.holo_green_dark))

            // Cierra la Activity para volver al Dashboard
            finish()
        } else {
            binding.tvProfileMessage.text = "Error: Usuario no logueado."
            binding.tvProfileMessage.setTextColor(getColor(android.R.color.holo_red_dark))
        }
    }
}