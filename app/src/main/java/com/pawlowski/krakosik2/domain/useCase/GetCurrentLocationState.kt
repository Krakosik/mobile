package com.pawlowski.krakosik2.domain.useCase

import android.annotation.SuppressLint
import android.location.Location
import com.pawlowski.krakosik2.LocationHandler
import com.pawlowski.krakosik2.retryEverySecond
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("MissingPermission")
@Singleton
internal class GetCurrentLocationState
    @Inject
    constructor(
        private val locationHandler: LocationHandler,
    ) {
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        private val locationState by lazy {
            locationHandler
                .streamLocation()
                .map { it.lastLocation }
                .retryEverySecond()
                .stateIn(
                    scope = scope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = null,
                )
        }

        operator fun invoke(): StateFlow<Location?> = locationState
    }
