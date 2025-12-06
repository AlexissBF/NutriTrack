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


        val userName = intent.getStringExtra("USER_NAME") ?: userPrefs.getStoredName() ?: "Usuario"
        binding.tvWelcome.text = "¡Hola, $userName!"




        binding.btnRegisterFood.setOnClickListener {
            val intent = Intent(this, FoodEntryActivity::class.java)
            startActivity(intent)
        }


        binding.btnRegisterActivity.setOnClickListener {
            val intent = Intent(this, ActivityEntryActivity::class.java)
            startActivity(intent)
        }

        binding.btnViewReports.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }


        binding.btnLogout.setOnClickListener {
            performLogout()
        }


        updateBalanceView()
    }


    override fun onResume() {
        super.onResume()
        updateBalanceView()
    }

    private fun updateBalanceView() {
        val lastFood = recordPrefs.getLastFoodEntry()
        val lastActivity = recordPrefs.getLastActivityEntry()

        val balanceStatus = getSimulatedBalanceStatus()

        binding.tvBalanceValue.text =
            "Balance Diario: $balanceStatus\n" +
                    "Última Comida: $lastFood\n" +
                    "Última Actividad: $lastActivity"
    }

    private fun getSimulatedBalanceStatus(): String {
        val randomStatus = (0..2).random()
        return when (randomStatus) {
            0 -> "Superávit (+)"
            1 -> "Déficit (-)"
            else -> "Neutro (≈)"
        }
    }


    private fun performLogout() {
        // Borra la sesión local y redirige al login
        userPrefs.clearUser()
        Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}