package com.example.nutritrack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
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

        val activeUser = userPrefs.getActiveUser()
        binding.tvWelcome.text = "¡Hola, ${activeUser?.name ?: "Usuario"}!"

        binding.btnOpenDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_about -> startActivity(Intent(this, AboutActivity::class.java))
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        binding.btnRegisterFood.setOnClickListener { startActivity(Intent(this, FoodEntryActivity::class.java)) }
        binding.btnRegisterActivity.setOnClickListener { startActivity(Intent(this, ActivityEntryActivity::class.java)) }
        binding.btnViewReports.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }

        binding.btnLogout.setOnClickListener {
            userPrefs.clearSession()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        updateView()
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    private fun updateView() {
        val activeUser = userPrefs.getActiveUser() ?: return

        // --- Resumen de Comidas ---
        val foodEntries = recordPrefs.getFoodEntriesForUser(activeUser.email)
        val foodListText = StringBuilder()
        var totalCaloriesConsumed = 0

        if (foodEntries.isEmpty()) {
            foodListText.append("Aún no has registrado ninguna comida.")
        } else {
            foodEntries.forEach { entry ->
                foodListText.append("• ${entry.foodName}: ${entry.calories} kcal\n")
                totalCaloriesConsumed += entry.calories
            }
        }
        binding.tvFoodSummary.text = foodListText.toString()
        binding.tvTotalCalories.text = "Total Consumido: $totalCaloriesConsumed kcal"

        // --- Resumen de Actividad Física ---
        val activityEntries = recordPrefs.getActivityEntriesForUser(activeUser.email)
        val activityListText = StringBuilder()
        var totalCaloriesBurned = 0

        if (activityEntries.isEmpty()) {
            activityListText.append("Aún no has registrado actividad física.")
        } else {
            activityEntries.forEach { entry ->
                activityListText.append("• ${entry.activityName} (${entry.duration}): ${entry.caloriesBurned} kcal\n")
                totalCaloriesBurned += entry.caloriesBurned
            }
        }

        // Asumo que tienes un TextView con id 'tv_activity_summary' en tu layout
        binding.tvActivitySummary.text = activityListText.toString()
        
        // Asumo que tienes un TextView con id 'tv_total_calories_burned' en tu layout
        binding.tvTotalCaloriesBurned.text = "Total Quemado: $totalCaloriesBurned kcal"
    }
}
