package com.pawlowski.krakosik2.ui.screen.map

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CarCrash
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pawlowski.krakosik2.domain.model.NearbyEvent
import com.pawlowski.krakosik2.domain.model.distanceText
import com.pawlowski.network.EventType
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun NearestEventBox(
    event: NearbyEvent,
    angle: () -> Float?,
    modifier: Modifier = Modifier,
) {
    val animatedAngle =
        remember {
            Animatable(initialValue = 0f)
        }
    LaunchedEffect(Unit) {
        snapshotFlow {
            angle()
        }.collectLatest {
            animatedAngle.animateTo(targetValue = it ?: 0f)
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .clip(shape = RoundedCornerShape(size = 24.dp))
                .background(color = Color.LightGray)
                .padding(all = 2.dp),
    ) {
        Icon(
            imageVector = event.event.type.toIcon(),
            contentDescription = null,
            modifier =
                Modifier
                    .clip(shape = CircleShape)
                    .background(color = Color.Blue)
                    .padding(all = 4.dp),
            tint = Color.White,
        )
        Text(
            text = event.distanceText(),
            fontWeight = FontWeight.SemiBold,
        )
        Image(
            imageVector = Icons.Default.ArrowUpward,
            contentDescription = null,
            modifier =
                Modifier.graphicsLayer {
                    rotationZ = angle() ?: 0f
                },
        )
    }
}

internal fun EventType.toIcon() =
    when (this) {
        EventType.POLICE_CHECK -> Icons.Default.LocalPolice
        EventType.ACCIDENT -> Icons.Default.CarCrash
        EventType.TRAFFIC_JAM -> Icons.Default.Traffic
        EventType.SPEED_CAMERA -> Icons.Default.Speed
    }
