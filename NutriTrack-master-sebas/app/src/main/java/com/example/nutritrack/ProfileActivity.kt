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

        // Cargar datos reales del perfil [cite: 190]
        loadCurrentProfile()

        binding.btnSaveProfile.setOnClickListener {
            saveProfileChanges()
        }
    }

    private fun loadCurrentProfile() {
        val currentUser = userPrefs.getActiveUser()

        if (currentUser != null) {
            binding.etProfileName.setText(currentUser.name)
            binding.etProfileAge.setText("25") // Valor por defecto simulado
            binding.etProfileWeight.setText("70.0") // Valor por defecto simulado
        }
    }

    private fun saveProfileChanges() {
        val newName = binding.etProfileName.text.toString().trim()

        if (newName.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio.", Toast.LENGTH_SHORT).show()
            return
        }

        // Simulación de actualización exitosa del perfil
        Toast.makeText(this, "¡Perfil actualizado con éxito!", Toast.LENGTH_LONG).show()
        finish()
    }
}