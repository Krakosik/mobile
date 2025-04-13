package com.pawlowski.krakosik2.domain.model

import com.pawlowski.network.Event
import java.math.BigDecimal

internal data class NearbyEvent(
    val distance: BigDecimal,
    val event: Event,
)
