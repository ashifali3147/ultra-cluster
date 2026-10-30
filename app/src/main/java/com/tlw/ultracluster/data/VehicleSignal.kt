package com.tlw.ultracluster.data

data class VehicleSignal(
    val speedKmh: Float?,
    val motorOn: Boolean?,
    val killSwitchActive: Boolean?,
    val rideMode: RideMode,
    val timestampMs: Long
)