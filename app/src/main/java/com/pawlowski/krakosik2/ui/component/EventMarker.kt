package com.pawlowski.krakosik2.ui.component

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.pawlowski.network.Event

@Composable
fun CustomMapMarker(event: Event) {
    Marker(
        state =
            rememberUpdatedMarkerState(
                position =
                    LatLng(
                        event.latitude,
                        event.longitude,
                    ),
            ),
        title = "${event.type.name} (${event.votes})",
    )
}
