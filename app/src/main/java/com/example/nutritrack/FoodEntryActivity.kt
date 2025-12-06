// FoodEntryActivity.kt (Lógica LOCAL - Final)

package com.example.nutritrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nutritrack.databinding.ActivityFoodEntryBinding
import com.example.nutritrack.storage.RecordPrefs // <-- Ahora solo usa clases locales

class FoodEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodEntryBinding
    private lateinit var recordPrefs: RecordPrefs // Instancia de almacenamiento local

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoodEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recordPrefs = RecordPrefs(this) // Inicializar RecordPrefs

        // 1. Asignar listener al botón de guardar
        binding.btnSaveFood.setOnClickListener {
            saveFoodEntry()
        }
    }

    private fun saveFoodEntry() {
        // 1. Obtener datos de la UI
        val foodName = binding.etFoodName.text.toString().trim()
        val quantityStr = binding.etQuantity.text.toString().trim()
        val unit = binding.etUnit.text.toString().trim()
        val caloriesStr = binding.etCalories.text.toString().trim()

        // 2. Validación de campos obligatorios
        if (foodName.isEmpty() || quantityStr.isEmpty() || unit.isEmpty() || caloriesStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Conversión y validación numérica
        val calories = caloriesStr.toIntOrNull()

        if (calories == null || calories < 0) {
            Toast.makeText(this, "Calorías deben ser números válidos.", Toast.LENGTH_SHORT).show()
            return
        }

        // 4. SIMULACIÓN DE GUARDADO (Guarda localmente el último registro)
        recordPrefs.saveLastFoodEntry(foodName, calories)

        // 5. ÉXITO: Mostrar mensaje y limpiar
        Toast.makeText(this, "¡Comida registrada con éxito!", Toast.LENGTH_SHORT).show()

        // Limpiar campos y cerrar la Activity
        binding.etFoodName.text?.clear()
        binding.etQuantity.text?.clear()
        binding.etUnit.text?.clear()
        binding.etCalories.text?.clear()

        finish()
    }

    // Función de utilidad para mostrar mensajes en la UI
    private fun displayMessage(message: String, colorId: Int) {
        // Ya no se usa, pero se puede mantener vacío o eliminar si el compilador lo permite
    }
}