package com.pawlowski.krakosik2.ui.screen.map

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.krakosik2.domain.useCase.GetAngleToNearbyEvent
import com.pawlowski.krakosik2.domain.useCase.ReportNewEvent
import com.pawlowski.krakosik2.domain.useCase.StreamNearbyEvent
import com.pawlowski.network.EventType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
internal class MapViewModel
    @Inject
    constructor(
        private val reportNewEvent: ReportNewEvent,
        private val streamNearbyEvent: StreamNearbyEvent,
        private val getAngleToNearbyEvent: GetAngleToNearbyEvent,
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
    }
