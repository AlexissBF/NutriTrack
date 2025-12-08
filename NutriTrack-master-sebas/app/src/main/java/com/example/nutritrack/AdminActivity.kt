package com.example.nutritrack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutritrack.databinding.ActivityAdminBinding
import com.example.nutritrack.storage.UserPrefs

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var userPrefs: UserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefs = UserPrefs(this)

        // Cargar lista inicial
        refreshUserList()

        // RF06: Eliminar usuario específico elegido por el administrador
        binding.btnDeleteSelectedUser.setOnClickListener {
            val emailToDelete = binding.etDeleteEmail.text.toString().trim()

            if (emailToDelete.isEmpty()) {
                Toast.makeText(this, "Escribe un correo para eliminar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Evitar que el admin se borre a sí mismo accidentalmente
            val currentAdmin = userPrefs.getActiveUser()?.email
            if (emailToDelete == currentAdmin) {
                Toast.makeText(this, "No puedes eliminar tu propia cuenta de administrador", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = userPrefs.deleteUser(emailToDelete)
            if (success) {
                Toast.makeText(this, "Usuario $emailToDelete eliminado", Toast.LENGTH_SHORT).show()
                binding.etDeleteEmail.text?.clear()
                refreshUserList() // Actualizar lista visual
            } else {
                Toast.makeText(this, "No se encontró ningún usuario con ese correo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAdminBack.setOnClickListener {
            userPrefs.clearSession()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun refreshUserList() {
        val users = userPrefs.getAllUsers()
        var listText = "Lista de Usuarios Registrados:\n\n"

        users.forEachIndexed { index, user ->
            listText += "${index + 1}. ${user.name} [${user.role}]\n   ${user.email}\n\n"
        }

        binding.tvAdminUserList.text = listText
    }
}