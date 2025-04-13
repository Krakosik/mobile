package com.pawlowski.krakosik2.domain.useCase

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

internal fun calculateRotation(
    userLat: Double,
    userLon: Double,
    eventLat: Double,
    eventLon: Double,
    azimuth: Float,
): Float {
    val directionToEvent = calculateBearing(userLat, userLon, eventLat, eventLon)

    return directionToEvent - azimuth
}

private fun calculateBearing(
    userLat: Double,
    userLon: Double,
    targetLat: Double,
    targetLon: Double,
): Float {
    val phi1 = Math.toRadians(userLat)
    val phi2 = Math.toRadians(targetLat)
    val deltaLambda = Math.toRadians(targetLon - userLon)

    val y = sin(deltaLambda) * cos(phi2)
    val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)
    var bearing = Math.toDegrees(atan2(y, x)).toFloat()
    if (bearing < 0) bearing += 360f

    return bearing
}
