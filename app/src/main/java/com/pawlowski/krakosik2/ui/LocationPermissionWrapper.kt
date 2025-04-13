package com.pawlowski.krakosik2.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun WrapLocationPermission(content: @Composable () -> Unit) {
    val permissions =
        rememberMultiplePermissionsState(
            permissions =
                listOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
        )
    if (permissions.allPermissionsGranted) {
        content()
    } else {
        LaunchedEffect(Unit) {
            permissions.launchMultiplePermissionRequest()
        }
        if (permissions.shouldShowRationale) {
            Text(text = "Włącz uprawnienia do lokalizacji w ustawieniach")
        }
    }
}
