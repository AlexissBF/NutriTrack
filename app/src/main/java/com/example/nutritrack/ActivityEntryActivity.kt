// ActivityEntryActivity.kt

package com.example.nutritrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nutritrack.databinding.ActivityActivityEntryBinding
import com.example.nutritrack.storage.RecordPrefs

class ActivityEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActivityEntryBinding
    private lateinit var recordPrefs: RecordPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recordPrefs = RecordPrefs(this)

        // Asignar listener al botón de guardar
        binding.btnSaveActivity.setOnClickListener {
            saveActivityEntry()
        }
    }

    private fun saveActivityEntry() {
        // 1. Obtener datos de la UI
        val activityName = binding.etActivityName.text.toString().trim()
        val durationStr = binding.etDuration.text.toString().trim()
        val caloriesStr = binding.etCaloriesBurned.text.toString().trim()

        // 2. Validación de campos obligatorios
        if (activityName.isEmpty() || durationStr.isEmpty() || caloriesStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Conversión y validación numérica
        val duration = durationStr.toIntOrNull()
        val calories = caloriesStr.toIntOrNull()

        if (duration == null || duration <= 0 || calories == null || calories < 0) {
            Toast.makeText(this, "Duración y calorías deben ser números válidos y positivos.", Toast.LENGTH_SHORT).show()
            return
        }

        // 4. SIMULACIÓN DE GUARDADO (Guarda localmente el último registro)
        // Usamos el nombre y las calorías quemadas para el registro simulado
        recordPrefs.saveLastActivityEntry(activityName, calories)

        // 5. ÉXITO: Mostrar mensaje y limpiar
        Toast.makeText(this, "¡Actividad registrada con éxito!", Toast.LENGTH_SHORT).show()

        // Limpiar campos y cerrar la Activity
        binding.etActivityName.text?.clear()
        binding.etDuration.text?.clear()
        binding.etCaloriesBurned.text?.clear()

        // Cierra la Activity y regresa al Dashboard
        finish()
    }
}