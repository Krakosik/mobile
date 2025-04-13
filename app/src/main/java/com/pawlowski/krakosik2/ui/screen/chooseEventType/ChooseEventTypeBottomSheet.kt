package com.pawlowski.krakosik2.ui.screen.chooseEventType

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pawlowski.krakosik2.ui.BaseBottomSheet
import com.pawlowski.krakosik2.ui.screen.map.toIcon
import com.pawlowski.krakosik2.ui.theme.Krakosik2Theme
import com.pawlowski.network.EventType

@Composable
internal fun ChooseEventTypeBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (EventType) -> Unit,
) {
    BaseBottomSheet(
        show = show,
        onDismiss = onDismiss,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 15.dp)
                    .padding(horizontal = 16.dp),
        ) {
            EventType.entries.forEach {
                EventTypeCard(
                    eventType = it,
                    onClick = {
                        onConfirm(it)
                    },
                )
            }
        }
    }
}

@Composable
private fun EventTypeCard(
    eventType: EventType,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(all = 16.dp),
    ) {
        Icon(
            imageVector = eventType.toIcon(),
            contentDescription = null,
            modifier =
                Modifier
                    .clip(shape = CircleShape)
                    .background(color = Color.Blue)
                    .padding(all = 4.dp),
            tint = Color.White,
        )

        Text(
            text = eventType.name,
            modifier = Modifier.weight(weight = 1f),
        )

        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
private fun EventTypeCardPreview(modifier: Modifier = Modifier) =
    Krakosik2Theme {
        ChooseEventTypeBottomSheet(
            show = true,
            onDismiss = { },
            onConfirm = {},
        )
    }
