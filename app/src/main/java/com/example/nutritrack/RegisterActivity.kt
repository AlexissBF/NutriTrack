package com.example.nutritrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutritrack.data.User // Asumo que esta es la ruta correcta
import com.example.nutritrack.storage.UserPrefs

class RegisterActivity : AppCompatActivity() {

    private lateinit var userPrefs: UserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userPrefs = UserPrefs(this)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        // Añadimos un campo para el nombre, ya que el objeto User lo requiere
        val etName = findViewById<EditText>(R.id.etName) // Necesitamos añadir este EditText al layout
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val name = etName.text.toString().trim()

            if (email.isNotBlank() && password.isNotBlank() && name.isNotBlank()) {
                // Creamos un nuevo usuario con rol "user" por defecto
                val newUser = User(email, name, password, "user")

                // Usamos el método de UserPrefs para registrarlo
                val success = userPrefs.registerNewUser(newUser)

                if (success) {
                    Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                    // Redirigir a la pantalla de inicio de sesión
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "El correo electrónico ya está en uso", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
