package com.pawlowski.krakosik2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.network.IEventsDataProvider
import com.pawlowski.network.LocationUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel
    @Inject
    constructor(
        private val eventsDataProvider: IEventsDataProvider,
    ) : ViewModel() {
        init {

            eventsDataProvider
                .streamEvents(
                    locationUpdate =
                        flowOf(
                            LocationUpdate(
                                latitude = 1.0,
                                longitude = 1.0,
                                timestamp = System.currentTimeMillis(),
                            ),
                        ),
                ).onEach {
                    println("Events: $it")
                }.launchIn(viewModelScope)
        }
    }
