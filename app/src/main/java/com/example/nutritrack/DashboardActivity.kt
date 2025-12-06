// DashboardActivity.kt (Corregido para uso local y RecordPrefs)
package com.example.nutritrack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutritrack.databinding.ActivityDashboardBinding
import com.example.nutritrack.storage.RecordPrefs
import com.example.nutritrack.storage.UserPrefs

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var recordPrefs: RecordPrefs
    private lateinit var userPrefs: UserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recordPrefs = RecordPrefs(this)
        userPrefs = UserPrefs(this)

        // 1. Mostrar el saludo
        val userName = intent.getStringExtra("USER_NAME") ?: userPrefs.getStoredName() ?: "Usuario"
        binding.tvWelcome.text = "¡Hola, $userName!"

        // 2. Asignar listeners a los botones principales
        binding.btnRegisterFood.setOnClickListener {
            val intent = Intent(this, FoodEntryActivity::class.java)
            startActivity(intent)
        }

        // El listener para el botón de actividad debe ir aquí (lo implementaremos en el siguiente módulo)
        binding.btnRegisterActivity.setOnClickListener {
            val intent = Intent(this, ActivityEntryActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            performLogout()
        }

        // 3. Mostrar el resumen (se llama también en onResume)
        updateBalanceView()
    }

    // Esta función se llama cada vez que la Activity vuelve a primer plano (ej: desde FoodEntryActivity)
    override fun onResume() {
        super.onResume()
        updateBalanceView()
    }

    private fun updateBalanceView() {
        val lastFood = recordPrefs.getLastFoodEntry()
        val lastActivity = recordPrefs.getLastActivityEntry()

        // Usamos una simulación de balance con los últimos registros guardados
        binding.tvBalanceValue.text =
            "Última Comida: $lastFood\n" +
                    "Última Actividad: $lastActivity"
    }

    private fun performLogout() {
        // Borra la sesión local y redirige al login (MainActivity)
        userPrefs.clearUser()
        Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}