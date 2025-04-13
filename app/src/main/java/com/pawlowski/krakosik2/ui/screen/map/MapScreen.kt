package com.pawlowski.krakosik2.ui.screen.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.krakosik2.ui.WrapLocationPermission
import com.pawlowski.network.EventType
import java.math.RoundingMode

@Composable
internal fun MapScreen() {
    WrapLocationPermission {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            val viewModel = hiltViewModel<MapViewModel>()
            val event = viewModel.nearbyEvent.collectAsState()
            Text(
                "Najbliższe wydarzenie: ${event.value?.distance?.setScale(
                    2,
                    RoundingMode.HALF_UP,
                )?.toPlainString()} km ${event.value?.event}",
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
            Button(onClick = { viewModel.reportEvent(eventType = EventType.ACCIDENT) }) {
                Text(text = "Raportuj wydarzenie")
            }
        }
    }
}
