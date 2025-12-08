package com.example.nutritrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nutritrack.data.ActivityEntry
import com.example.nutritrack.databinding.ActivityActivityEntryBinding
import com.example.nutritrack.storage.RecordPrefs
import com.example.nutritrack.storage.UserPrefs

class ActivityEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActivityEntryBinding
    private lateinit var recordPrefs: RecordPrefs
    private lateinit var userPrefs: UserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recordPrefs = RecordPrefs(this)
        userPrefs = UserPrefs(this)

        binding.btnSaveActivity.setOnClickListener { saveActivityEntry() }
    }

    private fun saveActivityEntry() {
        val activityName = binding.etActivityName.text.toString().trim()
        val duration = binding.etDuration.text.toString().trim()
        val caloriesBurned = binding.etCaloriesBurned.text.toString().toIntOrNull()

        // 1. Validar que todos los campos estén completos
        if (activityName.isEmpty() || duration.isEmpty() || caloriesBurned == null) {
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

        // 3. Crear el objeto ActivityEntry
        val newActivityEntry = ActivityEntry(
            userEmail = userEmail,
            activityName = activityName,
            duration = "$duration minutos", // Añadimos "minutos" para más contexto
            caloriesBurned = caloriesBurned
        )

        // 4. Guardar el registro usando el nuevo método de RecordPrefs
        recordPrefs.addActivityEntry(userEmail, newActivityEntry)

        Toast.makeText(this, "Actividad registrada correctamente", Toast.LENGTH_SHORT).show()
        finish() // Cierra la actividad y vuelve al Dashboard
    }
}
