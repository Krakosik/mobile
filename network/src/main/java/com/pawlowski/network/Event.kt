package com.pawlowski.network

data class Event(
    val id: Int,
    val type: EventType,
    val latitude: Double,
    val longitude: Double,
    val votes: Int,
    val canVote: Boolean,
)

enum class EventType {
    POLICE_CHECK,
    ACCIDENT,
    TRAFFIC_JAM,
    SPEED_CAMERA,
}
