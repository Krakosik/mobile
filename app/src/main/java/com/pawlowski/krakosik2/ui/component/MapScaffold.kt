package com.pawlowski.krakosik2.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun MapScaffold(
    map: @Composable () -> Unit,
    nearbyEvent: @Composable (Modifier) -> Unit,
    reportButton: @Composable (Modifier) -> Unit,
    voteDialog: @Composable (Modifier) -> Unit,
) {
    Box {
        map()
        nearbyEvent(Modifier.align(Alignment.TopCenter).padding(top = 16.dp))
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            reportButton(Modifier.padding(bottom = 16.dp))
            voteDialog(Modifier)
        }
    }
}
