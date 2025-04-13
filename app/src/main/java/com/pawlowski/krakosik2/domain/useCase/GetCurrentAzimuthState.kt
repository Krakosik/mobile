package com.pawlowski.krakosik2.domain.useCase

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetCurrentAzimuthState
    @Inject
    constructor(
        private val context: Context,
    ) : SensorEventListener {
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        private val sensorManager: SensorManager by lazy {
            getSystemService(context, SensorManager::class.java)
        }

        private val accelerometer: Sensor? by lazy {
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }

        private val magneticField: Sensor? by lazy {
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        }

        private val azymuthState: StateFlow<Float?> by lazy {
            val accelerometerFlow =
                accelerometer?.let { sensorManager.eventsFlow(it) } ?: return@lazy MutableStateFlow(null)
            val magneticFieldFLow =
                magneticField?.let { sensorManager.eventsFlow(it) } ?: return@lazy MutableStateFlow(null)
            combine(
                accelerometerFlow,
                magneticFieldFLow,
            ) { accelerometerEvent, magneticFieldEvent ->
                val gravity = accelerometerEvent.values.clone()
                val geomagnetic = magneticFieldEvent.values.clone()

                val rotationMatrix = FloatArray(9)
                val orientation = FloatArray(3)

                if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    if (azimuth < 0) azimuth += 360f

                    azimuth
                } else {
                    null
                }
            }.distinctUntilChanged()
                .stateIn(
                    scope = scope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = null,
                )
        }

        operator fun invoke() = azymuthState

        override fun onSensorChanged(sensorEvent: SensorEvent) {
        }

        override fun onAccuracyChanged(
            p0: Sensor?,
            p1: Int,
        ) {}
    }
