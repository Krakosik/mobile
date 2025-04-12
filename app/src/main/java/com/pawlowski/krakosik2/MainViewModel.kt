package com.pawlowski.krakosik2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pawlowski.network.IEventsDataProvider
import com.pawlowski.network.LocationUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel
    @Inject
    constructor(
        private val eventsDataProvider: IEventsDataProvider,
    ) : ViewModel() {
        init {
            val currentUser = FirebaseAuth.getInstance().currentUser

            viewModelScope.launch {
                if (currentUser == null) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword("maciekpawlowski1@onet.pl", "Test12345").await()
                }

                eventsDataProvider
                    .streamEvents(
                        locationUpdate =
                            flow {
                                emit(
                                    LocationUpdate(
                                        latitude = 1.0,
                                        longitude = 1.0,
                                        timestamp = System.currentTimeMillis(),
                                    ),
                                )
                                suspendCancellableCoroutine { }
                            },
                    ).onEach {
                        println("Events: $it")
                    }.launchIn(viewModelScope)
            }
        }
    }
