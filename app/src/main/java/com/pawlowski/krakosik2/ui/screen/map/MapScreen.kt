package com.pawlowski.krakosik2.ui.screen.map

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pawlowski.krakosik2.ui.WrapLocationPermission
import com.pawlowski.krakosik2.ui.component.GeoMap
import com.pawlowski.krakosik2.ui.component.MapScaffold
import com.pawlowski.krakosik2.ui.screen.chooseEventType.ChooseEventTypeBottomSheet
import com.pawlowski.network.Event

@Composable
internal fun MapScreen() {
    WrapLocationPermission {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            val viewModel = hiltViewModel<MapViewModel>()

            val showBottomSheet =
                remember {
                    mutableStateOf(false)
                }
            MapScaffold(
                map = {
                    val currentLocation = viewModel.currentLocation.collectAsState()
                    val events = viewModel.allEvents.collectAsState()
                    GeoMap(
                        mapPosition = currentLocation::value,
                        events = events::value,
                        modifier = Modifier.weight(weight = 1f),
                    )
                },
                nearbyEvent = { modifier ->
                    val event by viewModel.nearbyEvent.collectAsStateWithLifecycle()
                    val angleToNearestEvent = viewModel.angleToEvent.collectAsStateWithLifecycle()
                    event?.let {
                        NearestEventBox(
                            event = it,
                            angle = angleToNearestEvent::value,
                            modifier = modifier,
                        )
                    }
                },
                voteDialog = {
                    val eventToVoteOn by viewModel.eventToVote.collectAsState()
                    val isVotingInProgress by viewModel.isVotingInProgress.collectAsState()
                    val showThanYouDialog by viewModel.showThankYouDialog.collectAsState()

                    val showAnyDialog = eventToVoteOn != null || showThanYouDialog || isVotingInProgress

                    AnimatedContent(
                        targetState = showAnyDialog,
                    ) { targetShowAnyDialog ->
                        if (targetShowAnyDialog) {
                            Surface(
                                modifier = Modifier.padding(16.dp),
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = 8.dp,
                            ) {
                                val eventToVote = eventToVoteOn
                                when {
                                    isVotingInProgress -> {
                                        CircularProgressIndicator(modifier = Modifier.padding(all = 16.dp))
                                    }
                                    showThanYouDialog -> {
                                        Text(
                                            text = "Dziękujemy za głos! 😍",
                                            style = MaterialTheme.typography.titleLarge,
                                            modifier = Modifier.padding(all = 16.dp),
                                        )
                                    }
                                    eventToVote != null -> {
                                        RecommendationBox(
                                            event = eventToVote.event,
                                            onVoteUp = {
                                                viewModel.voteForEvent(eventId = eventToVote.event.id, isLike = true)
                                            },
                                            onVoteDown = {
                                                viewModel.voteForEvent(eventId = eventToVote.event.id, isLike = false)
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                reportButton = {
                    Button(
                        onClick = { showBottomSheet.value = true },
                        enabled = !viewModel.isReportingInProgress.collectAsState().value,
                        modifier = it,
                    ) {
                        Text(text = "Raportuj wydarzenie")
                    }
                },
            )

            if (showBottomSheet.value) {
                ChooseEventTypeBottomSheet(
                    show = showBottomSheet.value,
                    onDismiss = { showBottomSheet.value = false },
                    onConfirm = {
                        viewModel.reportEvent(eventType = it)
                        showBottomSheet.value = false
                    },
                )
            }
        }
    }
}

@Composable
private fun RecommendationBox(
    event: Event,
    onVoteUp: () -> Unit,
    onVoteDown: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(all = 16.dp),
    ) {
        Text(
            text = event.type.name,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = "Czy to wydarzenie było pomocne?",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.size(size = 16.dp))
        Row {
            IconButton(
                onClick = onVoteUp,
            ) {
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(size = 48.dp),
                )
            }
            Spacer(modifier = Modifier.size(size = 16.dp))
            IconButton(
                onClick = onVoteDown,
            ) {
                Icon(
                    imageVector = Icons.Filled.ThumbDown,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(size = 48.dp),
                )
            }
        }
    }
}
