package com.stepwriter.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import kotlinx.coroutines.*
import java.time.*

class MainActivity : AppCompatActivity() {

    private lateinit var healthConnectClient: HealthConnectClient
    private lateinit var tvStatus: TextView
    private lateinit var editStepCount: EditText

    private val PERMISSIONS = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class)
    )

    private val requestPermissionLauncher =
        registerForActivityResult(PermissionController.createRequestPermissionResultContract()) { granted ->
            if (granted.containsAll(PERMISSIONS)) {
                tvStatus.text = "✅ Permissions granted! You can now write steps."
            } else {
                tvStatus.text = "❌ Permissions denied. Please grant Health Connect permissions in Settings."
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        editStepCount = findViewById(R.id.editStepCount)
        val btnWrite = findViewById<Button>(R.id.btnWriteSteps)
        val btnRead = findViewById<Button>(R.id.btnReadSteps)

        // Check if Health Connect is available
        val availabilityStatus = HealthConnectClient.getSdkStatus(this)
        if (availabilityStatus != HealthConnectClient.SDK_AVAILABLE) {
            tvStatus.text = "❌ Health Connect is not available on this device."
            btnWrite.isEnabled = false
            btnRead.isEnabled = false
            return
        }

        healthConnectClient = HealthConnectClient.getOrCreate(this)

        // Check and request permissions
        CoroutineScope(Dispatchers.Main).launch {
            checkPermissions()
        }

        btnWrite.setOnClickListener {
            val stepCount = editStepCount.text.toString().toLongOrNull()
            if (stepCount == null || stepCount <= 0) {
                tvStatus.text = "⚠️ Enter a valid step count!"
                return@setOnClickListener
            }
            writeSteps(stepCount)
        }

        btnRead.setOnClickListener {
            readTodaySteps()
        }
    }

    private suspend fun checkPermissions() {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        if (!granted.containsAll(PERMISSIONS)) {
            tvStatus.text = "🔐 Requesting Health Connect permissions..."
            requestPermissionLauncher.launch(PERMISSIONS)
        } else {
            tvStatus.text = "✅ Permissions already granted. Ready to write steps!"
        }
    }

    private fun writeSteps(count: Long) {
        tvStatus.text = "⏳ Writing $count steps to Health Connect..."

        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Check permissions first
                val granted = healthConnectClient.permissionController.getGrantedPermissions()
                if (!granted.containsAll(PERMISSIONS)) {
                    requestPermissionLauncher.launch(PERMISSIONS)
                    return@launch
                }

                withContext(Dispatchers.IO) {
                    val zoneOffset = ZoneOffset.ofHoursMinutes(5, 30) // IST

                    // Split steps into realistic walking sessions throughout the day
                    val today = LocalDate.now()
                    val sessions = generateRealisticSessions(count, today, zoneOffset)

                    healthConnectClient.insertRecords(sessions)
                }

                tvStatus.text = "✅ Successfully wrote $count steps to Health Connect!\n\nThe steps should now appear in your Health Connect app."
                Toast.makeText(this@MainActivity, "✅ $count steps written!", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                tvStatus.text = "❌ Error: ${e.message}\n\nMake sure Health Connect permissions are granted."
                e.printStackTrace()
            }
        }
    }

    private fun generateRealisticSessions(
        totalSteps: Long,
        date: LocalDate,
        zoneOffset: ZoneOffset
    ): List<StepsRecord> {
        val records = mutableListOf<StepsRecord>()
        var remaining = totalSteps

        // Create walking sessions spread throughout the day
        // Each session starts at a different hour to look natural
        data class Session(val hour: Int, val minute: Int, val durationMinutes: Int, val fraction: Double)

        val sessionTemplates = listOf(
            Session(6, 30, 25, 0.10),   // Early morning walk
            Session(7, 15, 20, 0.08),   // Getting ready / moving around
            Session(8, 0, 15, 0.05),    // Commute
            Session(9, 30, 10, 0.04),   // Office movement
            Session(10, 45, 12, 0.05),  // Mid-morning
            Session(12, 0, 20, 0.08),   // Lunch break walk
            Session(13, 30, 10, 0.04),  // Post lunch
            Session(15, 0, 15, 0.06),   // Afternoon
            Session(16, 30, 12, 0.05),  // Tea break walk
            Session(17, 30, 25, 0.12),  // Evening walk/commute
            Session(18, 30, 30, 0.15),  // Evening exercise
            Session(20, 0, 20, 0.10),   // Post dinner walk
            Session(21, 0, 15, 0.08),   // Night walk
        )

        val now = Instant.now()

        for (template in sessionTemplates) {
            if (remaining <= 0) break

            val sessionSteps = minOf((totalSteps * template.fraction).toLong(), remaining)
            if (sessionSteps <= 0) continue

            val startTime = date.atTime(template.hour, template.minute)
                .toInstant(zoneOffset)

            // Skip sessions that start in the future
            if (startTime >= now) continue

            var endTime = startTime.plusSeconds(template.durationMinutes.toLong() * 60)
            // Cap end time to now if it's in the future
            if (endTime > now) {
                endTime = now.minusSeconds(1)
            }

            records.add(
                StepsRecord(
                    count = sessionSteps,
                    startTime = startTime,
                    endTime = endTime,
                    startZoneOffset = zoneOffset,
                    endZoneOffset = zoneOffset
                )
            )

            remaining -= sessionSteps
        }

        // If there are remaining steps, add them to the most recent past session
        if (remaining > 0) {
            val startTime = date.atTime(21, 30).toInstant(zoneOffset)
            // Only add if the start time is in the past
            if (startTime < now) {
                var endTime = startTime.plusSeconds(20 * 60)
                if (endTime > now) {
                    endTime = now.minusSeconds(1)
                }
                records.add(
                    StepsRecord(
                        count = remaining,
                        startTime = startTime,
                        endTime = endTime,
                        startZoneOffset = zoneOffset,
                        endZoneOffset = zoneOffset
                    )
                )
            } else if (records.isNotEmpty()) {
                // Add remaining steps to the last valid session
                val lastRecord = records.removeAt(records.lastIndex)
                records.add(
                    StepsRecord(
                        count = lastRecord.count + remaining,
                        startTime = lastRecord.startTime,
                        endTime = lastRecord.endTime,
                        startZoneOffset = lastRecord.startZoneOffset,
                        endZoneOffset = lastRecord.endZoneOffset
                    )
                )
            }
        }

        return records
    }

    private fun readTodaySteps() {
        tvStatus.text = "⏳ Reading today's steps..."

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val granted = healthConnectClient.permissionController.getGrantedPermissions()
                if (!granted.containsAll(PERMISSIONS)) {
                    requestPermissionLauncher.launch(PERMISSIONS)
                    return@launch
                }

                val result = withContext(Dispatchers.IO) {
                    val today = LocalDate.now()
                    val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant()
                    val endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()

                    healthConnectClient.aggregate(
                        AggregateRequest(
                            metrics = setOf(StepsRecord.COUNT_TOTAL),
                            timeRangeFilter = TimeRangeFilter.between(startOfDay, endOfDay)
                        )
                    )
                }

                val steps = result[StepsRecord.COUNT_TOTAL] ?: 0L
                tvStatus.text = "📊 Today's total steps: $steps"

            } catch (e: Exception) {
                tvStatus.text = "❌ Error reading: ${e.message}"
                e.printStackTrace()
            }
        }
    }
}
