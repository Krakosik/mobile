package com.pawlowski.krakosik2.domain.useCase

import com.pawlowski.krakosik2.retryEverySecond
import com.pawlowski.network.Event
import com.pawlowski.network.IEventsDataProvider
import com.pawlowski.network.LocationUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

internal class StreamEventsForCurrentLocation
    @Inject
    constructor(
        private val eventsDataProvider: IEventsDataProvider,
        private val getCurrentLocationState: GetCurrentLocationState,
    ) {
        private val latestLocationState = MutableStateFlow<LocationUpdate?>(null)
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        private val eventsFlow by lazy {
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
                ).mapNotNull { events ->
                    val location = latestLocationState.value ?: return@mapNotNull null
                    location to events.filter { it.votes > -2 }
                }.retryEverySecond()
                .shareIn(
                    scope = scope,
                    started = WhileSubscribed(0),
                )
        }

        operator fun invoke(): Flow<Pair<LocationUpdate, List<Event>>> = eventsFlow
    }
