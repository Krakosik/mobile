package com.pawlowski.krakosik2.domain.useCase

import com.pawlowski.network.Event
import com.pawlowski.network.IEventsDataProvider
import com.pawlowski.network.LocationUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

internal class StreamEventsForCurrentLocation
    @Inject
    constructor(
        private val eventsDataProvider: IEventsDataProvider,
        private val getCurrentLocationState: GetCurrentLocationState,
    ) {
        private val latestLocationState = MutableStateFlow<LocationUpdate?>(null)

        operator fun invoke(): Flow<Pair<LocationUpdate, List<Event>>> =
            eventsDataProvider
                .streamEvents(
                    locationUpdate =
                        getCurrentLocationState().mapNotNull { lastLocation ->
                            lastLocation?.let {
                                LocationUpdate(
                                    latitude = lastLocation.latitude,
                                    longitude = lastLocation.longitude,
                                    timestamp = lastLocation.time,
                                ).also { latestLocationState.value = it }
                            }
                        },
                ).mapNotNull {
                    val location = latestLocationState.value ?: return@mapNotNull null
                    location to it
                }
    }
