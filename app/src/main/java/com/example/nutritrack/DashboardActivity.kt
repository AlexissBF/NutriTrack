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
            // Limpiar el historial para que el usuario no pueda volver con el botón de atrás
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
        val activeUser = userPrefs.getActiveUser()
        if (activeUser == null) {
            // Si por alguna razón no hay usuario, no hay nada que mostrar
            return
        }

        val foodEntries = recordPrefs.getFoodEntriesForUser(activeUser.email)
        val foodListText = StringBuilder()
        var totalCalories = 0

        if (foodEntries.isEmpty()) {
            foodListText.append("Aún no has registrado ninguna comida hoy.")
        } else {
            foodListText.append("Comidas de hoy:\n")
            foodEntries.forEach { entry ->
                foodListText.append("  • ${entry.foodName} (${entry.quantity}): ${entry.calories} kcal\n")
                totalCalories += entry.calories
            }
        }

        // Asumo que tienes un TextView con id 'tv_food_summary' en tu layout
        binding.tvFoodSummary.text = foodListText.toString()

        // Asumo que tienes un TextView con id 'tv_total_calories' en tu layout
        binding.tvTotalCalories.text = "Total: $totalCalories kcal"

        // Mantengo la lógica de la actividad física si aún la necesitas
        // val lastActivity = recordPrefs.getLastActivityEntry(activeUser.email)
        // binding.tvActivitySummary.text = "Actividad: $lastActivity"
    }
}
