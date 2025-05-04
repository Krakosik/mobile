package com.pawlowski.krakosik2.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.pawlowski.network.Event
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GeoMap(
    mapPosition: () -> LatLng?,
    events: () -> List<Event>,
    modifier: Modifier = Modifier,
) {
    val polandLocation = LatLng(52.0, 20.0)
    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(polandLocation, 15.5f)
        }
    LaunchedEffect(Unit) {
        snapshotFlow {
            mapPosition()
        }.collectLatest {
            it?.let { latLng ->
                runCatching {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLng(latLng),
                    )
                }.onFailure { ensureActive() }
            }
        }
    }
    GoogleMap(
        uiSettings =
            remember {
                MapUiSettings(
                    myLocationButtonEnabled = true,
                    zoomControlsEnabled = false,
                    compassEnabled = true,
                    zoomGesturesEnabled = false,
                )
            },
        properties =
            remember {
                MapProperties(
                    isMyLocationEnabled = true,
                    isTrafficEnabled = true,
                    isBuildingEnabled = true,
                )
            },
        cameraPositionState = cameraPositionState,
        modifier = modifier,
    ) {
        events().forEach { event ->
            CustomMapMarker(
                event = event,
            )
        }
    }
}
