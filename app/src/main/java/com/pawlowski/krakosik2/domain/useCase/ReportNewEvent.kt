package com.pawlowski.krakosik2.domain.useCase

import com.pawlowski.network.EventType
import com.pawlowski.network.IEventsDataProvider
import javax.inject.Inject

internal class ReportNewEvent
    @Inject
    constructor(
        private val eventsDataProvider: IEventsDataProvider,
        private val getCurrentLocationState: GetCurrentLocationState,
    ) {
        suspend operator fun invoke(eventType: EventType) {
            val location = getCurrentLocationState().value ?: return
            eventsDataProvider.reportEvent(
                type = eventType,
                lat = location.latitude,
                lon = location.longitude,
            )
        }
    }
