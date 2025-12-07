package com.example.nutritrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
        val durationStr = binding.etDuration.text.toString().trim()
        val caloriesStr = binding.etCaloriesBurned.text.toString().trim()

        if (activityName.isEmpty() || durationStr.isEmpty() || caloriesStr.isEmpty()) {
            Toast.makeText(this, "Campos obligatorios vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        val calories = caloriesStr.toIntOrNull() ?: 0
        val activeEmail = userPrefs.getActiveUser()?.email ?: ""

        recordPrefs.saveLastActivityEntry(activeEmail, activityName, calories)

        Toast.makeText(this, "Actividad guardada!", Toast.LENGTH_SHORT).show()
        finish()
    }
}