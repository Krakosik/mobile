package com.pawlowski.krakosik2.ui.screen.map

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.krakosik2.domain.useCase.ReportNewEvent
import com.pawlowski.krakosik2.domain.useCase.StreamNearbyEvent
import com.pawlowski.network.EventType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
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
    ) : ViewModel() {
        val nearbyEvent by lazy {
            streamNearbyEvent()
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5000),
                    initialValue = null,
                )
        }

        fun reportEvent(eventType: EventType) {
            viewModelScope.launch {
                reportNewEvent(eventType)
            }
        }
    }
