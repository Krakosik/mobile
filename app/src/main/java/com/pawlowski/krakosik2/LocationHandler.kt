package com.pawlowski.krakosik2

import android.Manifest
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val LOCATION_INTERVAL_MILLIS = 2000L

internal class LocationHandler
    @Inject
    constructor(
        private val fusedLocationProviderClient: FusedLocationProviderClient,
    ) {
        private val locationRequest: LocationRequest by lazy {
            LocationRequest
                .Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    LOCATION_INTERVAL_MILLIS,
                ).setWaitForAccurateLocation(true)
                .build()
        }

        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
        fun streamLocation(): Flow<LocationResult> =
            callbackFlow {
                val callback =
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            trySend(locationResult)
                        }
                    }
                fusedLocationProviderClient
                    .requestLocationUpdates(
                        locationRequest,
                        callback,
                        Looper.getMainLooper(),
                    ).await()

                awaitClose {
                    fusedLocationProviderClient.removeLocationUpdates(callback)
                }
            }
    }
