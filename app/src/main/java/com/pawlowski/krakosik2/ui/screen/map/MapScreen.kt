package com.pawlowski.krakosik2.ui.screen.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pawlowski.krakosik2.ui.WrapLocationPermission
import com.pawlowski.krakosik2.ui.screen.chooseEventType.ChooseEventTypeBottomSheet

@Composable
internal fun MapScreen() {
    WrapLocationPermission {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            val viewModel = hiltViewModel<MapViewModel>()
            val event by viewModel.nearbyEvent.collectAsStateWithLifecycle()
            val angleToNearestEvent = viewModel.angleToEvent.collectAsStateWithLifecycle()
            event?.let {
                NearestEventBox(
                    event = it,
                    angle = angleToNearestEvent::value,
                )
            }
            Spacer(modifier = Modifier.weight(weight = 1f))

            val showBottomSheet =
                remember {
                    mutableStateOf(false)
                }
            Button(
                onClick = { showBottomSheet.value = true },
                enabled = !viewModel.isReportingInProgress.collectAsState().value,
            ) {
                Text(text = "Raportuj wydarzenie")
            }
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
