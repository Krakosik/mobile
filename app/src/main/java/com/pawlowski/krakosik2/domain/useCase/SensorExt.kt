package com.pawlowski.krakosik2.domain.useCase

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

internal fun SensorManager.eventsFlow(sensor: Sensor) =
    callbackFlow {
        val callback =
            object : SensorEventListener {
                override fun onSensorChanged(sensorEvent: SensorEvent) {
                    trySend(sensorEvent)
                }

                override fun onAccuracyChanged(
                    p0: Sensor?,
                    p1: Int,
                ) {}
            }
        registerListener(
            callback,
            sensor,
            SensorManager.SENSOR_DELAY_UI,
        )

        awaitClose {
            unregisterListener(callback)
        }
    }
