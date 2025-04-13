package com.pawlowski.krakosik2.ui.screen.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pawlowski.krakosik2.ui.WrapLocationPermission
import com.pawlowski.network.EventType

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
            Button(onClick = { viewModel.reportEvent(eventType = EventType.ACCIDENT) }) {
                Text(text = "Raportuj wydarzenie")
            }
        }
    }
}
