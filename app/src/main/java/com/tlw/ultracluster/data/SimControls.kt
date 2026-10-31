package com.tlw.ultracluster.data

enum class SimMode {
    AUTO, MANUAL
}

enum class Fault(val label: String) {
    INVALID_SPEED("Sensor sends -12"),
    SILENT("Sensor not responding"),
    KILL_WHILE_MOVING("Kill switch at 19"),
    OVER_SPEED("Over speed 86");

    fun toSignal(rideMode: RideMode, timestampMs: Long): VehicleSignal? = when(this) {
        INVALID_SPEED -> VehicleSignal(-12f, motorOn = true, killSwitchActive = false, rideMode = rideMode, timestampMs)
        SILENT -> TODO()
        KILL_WHILE_MOVING -> TODO()
        OVER_SPEED -> TODO()
    }
}

data class ManualControls(
    val throttle: Boolean = false,
    val brake: Boolean = false,
    val motorOn: Boolean = false,
    val killActive: Boolean = false,
)