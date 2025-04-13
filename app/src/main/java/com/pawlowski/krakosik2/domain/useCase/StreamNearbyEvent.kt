package com.pawlowski.krakosik2.domain.useCase

import android.location.Location
import com.pawlowski.krakosik2.domain.model.NearbyEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class StreamNearbyEvent
    @Inject
    constructor(
        private val streamEventsForCurrentLocation: StreamEventsForCurrentLocation,
    ) {
        operator fun invoke(): Flow<NearbyEvent?> =
            streamEventsForCurrentLocation()
                .map { (currentLocation, events) ->
                    events
                        .map { event ->
                            val results = FloatArray(1)
                            Location.distanceBetween(
                                currentLocation.latitude,
                                currentLocation.longitude,
                                event.latitude,
                                event.longitude,
                                results,
                            )
                            NearbyEvent(
                                distance = results[0].toBigDecimal().movePointLeft(3),
                                event = event,
                            )
                        }.minByOrNull { it.distance }
                }
    }
