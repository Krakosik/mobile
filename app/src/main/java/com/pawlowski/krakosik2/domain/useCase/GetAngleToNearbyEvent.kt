package com.pawlowski.krakosik2.domain.useCase

import com.pawlowski.krakosik2.domain.model.NearbyEvent
import com.pawlowski.krakosik2.throttle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class GetAngleToNearbyEvent
    @Inject
    constructor(
        private val getCurrentLocationState: GetCurrentLocationState,
        private val getAzimuthState: GetCurrentAzimuthState,
    ) {
        operator fun invoke(nearbyEvent: NearbyEvent): Flow<Float?> =
            combine(getAzimuthState(), getCurrentLocationState()) { azimuth, userLocation ->
                azimuth ?: return@combine null
                userLocation ?: return@combine null
                calculateRotation(
                    userLat = userLocation.latitude,
                    userLon = userLocation.longitude,
                    eventLat = nearbyEvent.event.latitude,
                    eventLon = nearbyEvent.event.longitude,
                    azimuth = azimuth,
                )
            }.throttle(waitMillis = 100)
    }
