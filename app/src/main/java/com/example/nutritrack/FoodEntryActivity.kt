package com.example.nutritrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nutritrack.databinding.ActivityFoodEntryBinding
import com.example.nutritrack.storage.RecordPrefs
import com.example.nutritrack.storage.UserPrefs

class FoodEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodEntryBinding
    private lateinit var recordPrefs: RecordPrefs
    private lateinit var userPrefs: UserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recordPrefs = RecordPrefs(this)
        userPrefs = UserPrefs(this)

        binding.btnSaveFood.setOnClickListener { saveFoodEntry() }
    }

    private fun saveFoodEntry() {
        val foodName = binding.etFoodName.text.toString().trim()
        val calories = binding.etCalories.text.toString().toIntOrNull()

        if (foodName.isNotEmpty() && calories != null) {
            // Obtenemos el email de la sesión activa para guardar privadamente [cite: 81]
            val activeEmail = userPrefs.getActiveUser()?.email ?: ""

            recordPrefs.saveLastFoodEntry(activeEmail, foodName, calories)

            Toast.makeText(this, "Comida guardada en tu perfil personal", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}