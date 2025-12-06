// RegisterActivity.kt (Lógica LOCAL)
package com.example.nutritrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nutritrack.databinding.ActivityRegisterBinding
import com.example.nutritrack.storage.UserPrefs // Importar el almacenamiento local

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userPrefs: UserPrefs // Inicializar la clase de almacenamiento

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefs = UserPrefs(this) // Inicializar UserPrefs

        // Asignar listener al botón de Registro
        binding.btnRegister.setOnClickListener {
            performRegistration()
        }
    }

    private fun performRegistration() {
        // 1. Obtener datos de la UI
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // 2. Validación de campos obligatorios y formato
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            binding.tvRegisterMessage.text = "Todos los campos son obligatorios."
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tvRegisterMessage.text = "Formato de correo inválido."
            return
        }

        // 3. Simulación de validación (El usuario ya existe)
        if (userPrefs.isUserRegistered()) {
            binding.tvRegisterMessage.text = "Fallo: Ya existe una cuenta registrada. Intente iniciar sesión."
            return
        }

        // 4. GUARDAR USUARIO LOCALMENTE (Simula el registro exitoso en la DB)
        userPrefs.saveUser(email, password, name)

        // 5. ÉXITO: Redirigir al Login
        Toast.makeText(this, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}