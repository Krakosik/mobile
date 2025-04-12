package com.pawlowski.network.dataProvider

import com.pawlowski.network.Event
import com.pawlowski.network.EventType
import com.pawlowski.network.IEventsDataProvider
import com.pawlowski.network.LocationUpdate
import com.pawlowski.network.service.IEventsServiceProvider
import events.EventServiceGrpcKt
import events.ReportEventRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import events.EventType as EventTypeDto
import events.LocationUpdate as LocationUpdateDto

internal class EventsDataProvider
    @Inject
    constructor(
        private val eventsServiceProvider: IEventsServiceProvider,
    ) : IEventsDataProvider {
        override fun streamEvents(locationUpdate: Flow<LocationUpdate>): Flow<List<Event>> =
            withStreamServiceBiDirectional(
                method = EventServiceGrpcKt.EventServiceCoroutineStub::streamLocation,
                requestFlow =
                    locationUpdate.map {
                        LocationUpdateDto
                            .newBuilder()
                            .setLatitude(it.latitude)
                            .setLongitude(it.longitude)
                            .setTimestamp(it.timestamp)
                            .build()
                    },
            ).map { eventsResponse ->
                eventsResponse.eventsList.map { eventDto ->
                    Event(
                        id = eventDto.eventId,
                        type = EventType.valueOf(eventDto.type.name),
                        latitude = eventDto.latitude,
                        longitude = eventDto.longitude,
                        votes = eventDto.votes,
                    )
                }
            }

        override suspend fun reportEvent(
            lat: Double,
            lon: Double,
            type: EventType,
        ) {
            withUnaryService(
                method = EventServiceGrpcKt.EventServiceCoroutineStub::reportEvent,
                request =
                    ReportEventRequest
                        .newBuilder()
                        .setLatitude(lat)
                        .setLongitude(lon)
                        .setType(EventTypeDto.valueOf(type.name))
                        .build(),
            )
        }

        private fun <REQ : Any, RESP : Any> withStreamServiceBiDirectional(
            method: EventServiceGrpcKt.EventServiceCoroutineStub.(Flow<REQ>) -> Flow<RESP>,
            requestFlow: Flow<REQ>,
        ): Flow<RESP> =
            flow {
                emitAll(
                    eventsServiceProvider
                        .invoke()
                        .method(requestFlow),
                )
            }

        private suspend fun <REQ : Any, RESP : Any> withUnaryService(
            method: suspend EventServiceGrpcKt.EventServiceCoroutineStub.(REQ) -> RESP,
            request: REQ,
        ): RESP =
            eventsServiceProvider
                .invoke()
                .method(request)
    }
