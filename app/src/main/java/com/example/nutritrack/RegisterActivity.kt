package com.example.nutritrack

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutritrack.data.User
import com.example.nutritrack.databinding.ActivityRegisterBinding
import com.example.nutritrack.storage.UserPrefs

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userPrefs: UserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPrefs = UserPrefs(this)

        binding.btnRegister.setOnClickListener { performRegistration() }
    }

    private fun performRegistration() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            binding.tvRegisterMessage.text = "Todos los campos son obligatorios."
            return
        }

        val newUser = User(email, name, password)

        if (userPrefs.registerNewUser(newUser)) {
            Toast.makeText(this, "Cuenta para $name creada.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            binding.tvRegisterMessage.text = "El correo ya existe."
        }
    }
}