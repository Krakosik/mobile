package com.pawlowski.krakosik2.domain.model

import com.pawlowski.network.Event
import java.math.BigDecimal
import java.math.RoundingMode

internal data class NearbyEvent(
    val distanceKm: BigDecimal,
    val event: Event,
)

internal fun NearbyEvent.distanceText() =
    if (distanceKm < BigDecimal("1")) {
        (distanceKm.movePointRight(1)).setScale(0, RoundingMode.DOWN).movePointRight(2).toPlainString() + "m"
    } else {
        distanceKm.setScale(2, RoundingMode.HALF_UP).toPlainString() + "km"
    }
