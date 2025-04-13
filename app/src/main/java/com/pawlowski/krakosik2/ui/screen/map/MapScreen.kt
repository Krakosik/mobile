package com.pawlowski.krakosik2.ui.screen.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            val event = viewModel.nearbyEvent.collectAsStateWithLifecycle()
            val angleToNearestEvent = viewModel.angleToEvent.collectAsStateWithLifecycle()
            Text(
                "Najbliższe wydarzenie: ${event.value?.distance?.setScale(
                    2,
                    RoundingMode.HALF_UP,
                )?.toPlainString()} km ${event.value?.event}",
            )
            Image(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = null,
                modifier =
                    Modifier.graphicsLayer {
                        rotationZ = angleToNearestEvent.value ?: 0f
                    },
            )
            Text(text = "Azymut: ${angleToNearestEvent.value}")
            Spacer(modifier = Modifier.weight(weight = 1f))
            Button(onClick = { viewModel.reportEvent(eventType = EventType.ACCIDENT) }) {
                Text(text = "Raportuj wydarzenie")
            }
        }
    }
}
