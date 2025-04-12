package com.pawlowski.network

import kotlinx.coroutines.flow.Flow

interface IEventsDataProvider {
    fun streamEvents(locationUpdate: Flow<LocationUpdate>): Flow<List<Event>>

    suspend fun reportEvent(
        lat: Double,
        lon: Double,
        type: EventType,
    )
}
