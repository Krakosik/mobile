package com.pawlowski.krakosik2.domain.useCase

import com.pawlowski.network.IEventsDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EventVotingUseCase
    @Inject
    constructor(
        private val eventsDataProvider: IEventsDataProvider,
    ) {
        private val alreadyVotingShownEvents = MutableStateFlow(setOf<Int>())

        suspend fun voteForEvent(
            eventId: Int,
            upVote: Boolean,
        ) {
            eventsDataProvider.voteForEvent(
                eventId = eventId,
                upVote = upVote,
            )
            alreadyVotingShownEvents.update {
                it + eventId
            }
        }

        fun markEventAsVoted(eventId: Int) {
            alreadyVotingShownEvents.update {
                it + eventId
            }
        }

        suspend fun awaitEventVoted(eventId: Int) {
            alreadyVotingShownEvents
                .first {
                    it.contains(eventId)
                }
        }

        fun isEventAlreadyVoted(eventId: Int): Boolean = alreadyVotingShownEvents.value.contains(eventId)
    }
