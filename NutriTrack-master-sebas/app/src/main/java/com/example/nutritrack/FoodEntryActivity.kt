package com.example.nutritrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nutritrack.data.FoodEntry
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
        val quantityValue = binding.etQuantity.text.toString().trim()
        val unitValue = binding.etUnit.text.toString().trim()
        val calories = binding.etCalories.text.toString().toIntOrNull()

        // 1. Validar que todos los campos estén completos
        if (foodName.isEmpty() || quantityValue.isEmpty() || unitValue.isEmpty() || calories == null) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Obtener el email del usuario activo
        val activeUser = userPrefs.getActiveUser()
        if (activeUser == null) {
            Toast.makeText(this, "Error: No se ha podido identificar al usuario", Toast.LENGTH_SHORT).show()
            return
        }
        val userEmail = activeUser.email

        // 3. Combinar cantidad y unidad en un solo String
        val fullQuantity = "$quantityValue $unitValue"

        // 4. Crear el objeto FoodEntry
        val newFoodEntry = FoodEntry(
            userEmail = userEmail,
            foodName = foodName,
            quantity = fullQuantity,
            calories = calories
        )

        // 5. Guardar el registro usando el nuevo método de RecordPrefs
        recordPrefs.addFoodEntry(userEmail, newFoodEntry)

        Toast.makeText(this, "Alimento registrado correctamente", Toast.LENGTH_SHORT).show()
        finish() // Cierra la actividad y vuelve al Dashboard
    }
}
