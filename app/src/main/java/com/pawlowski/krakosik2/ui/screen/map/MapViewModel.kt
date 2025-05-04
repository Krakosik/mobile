package com.pawlowski.krakosik2.ui.screen.map

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.krakosik2.domain.useCase.EventVotingUseCase
import com.pawlowski.krakosik2.domain.useCase.GetAngleToNearbyEvent
import com.pawlowski.krakosik2.domain.useCase.ReportNewEvent
import com.pawlowski.krakosik2.domain.useCase.StreamNearbyEvent
import com.pawlowski.network.EventType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@SuppressLint("MissingPermission")
@HiltViewModel
internal class MapViewModel
    @Inject
    constructor(
        private val reportNewEvent: ReportNewEvent,
        private val streamNearbyEvent: StreamNearbyEvent,
        private val getAngleToNearbyEvent: GetAngleToNearbyEvent,
        private val eventVotingUseCase: EventVotingUseCase,
    ) : ViewModel() {
        val nearbyEvent by lazy {
            streamNearbyEvent()
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5000),
                    initialValue = null,
                )
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        val angleToEvent by lazy {
            nearbyEvent
                .flatMapLatest {
                    getAngleToNearbyEvent(nearbyEvent = it ?: return@flatMapLatest flowOf(null))
                }.stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5000),
                    initialValue = null,
                )
        }

        val isVotingInProgress = MutableStateFlow(false)
        val showThankYouDialog = MutableStateFlow(false)

        val eventToVote by lazy {
            nearbyEvent
                .map { event ->
                    event?.takeIf {
                        event.event.canVote &&
                            event.distanceKm < BigDecimal("0.15") &&
                            !eventVotingUseCase.isEventAlreadyVoted(
                                eventId = event.event.id,
                            )
                    }
                }.distinctUntilChangedBy { it?.event?.id }
                .transform { event ->
                    emit(event)
                    if (event != null) {
                        val voted =
                            withTimeoutOrNull(timeout = 15.seconds) {
                                eventVotingUseCase.awaitEventVoted(eventId = event.event.id)
                            } != null
                        emit(null)
                        if (!voted) {
                            eventVotingUseCase.markEventAsVoted(eventId = event.event.id)
                        }
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(stopTimeoutMillis = 5000),
                    initialValue = null,
                )
        }

        val isReportingInProgress = MutableStateFlow(false)

        fun reportEvent(eventType: EventType) {
            if (isReportingInProgress.value) return
            viewModelScope.launch {
                isReportingInProgress.value = true
                runCatching {
                    reportNewEvent(eventType)
                }.onFailure { ensureActive() }
                    .onSuccess {
                        isReportingInProgress.value = false
                    }
            }
        }

        fun voteForEvent(
            eventId: Int,
            isLike: Boolean,
        ) {
            if (isVotingInProgress.value) return
            isVotingInProgress.value = true
            viewModelScope.launch {
                runCatching {
                    eventVotingUseCase.voteForEvent(
                        eventId = eventId,
                        upVote = isLike,
                    )
                }.onFailure {
                    ensureActive()
                    it.printStackTrace()
                    isVotingInProgress.value = false
                }.onSuccess {
                    isVotingInProgress.value = false
                    showThankYouDialog.value = true
                    delay(5.seconds)
                    showThankYouDialog.value = false
                }
            }
        }
    }
