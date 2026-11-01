package com.tlw.ultracluster.data

enum class SimMode {
    AUTO, MANUAL
}

enum class Fault(val label: String) {
    INVALID_SPEED("Sensor sends -12"),
    SILENT("Sensor not responding"),
    KILL_WHILE_MOVING("Kill switch at 19"),
    OVER_SPEED("Over speed 86");

    fun toSignal(rideMode: RideMode, timestampMs: Long): VehicleSignal? = when (this) {
        INVALID_SPEED -> VehicleSignal(-12f, motorOn = true, killSwitchActive = false, rideMode, timestampMs)
        SILENT -> null
        KILL_WHILE_MOVING -> VehicleSignal(18f, motorOn = false, killSwitchActive = true, rideMode, timestampMs)
        OVER_SPEED -> VehicleSignal(86f, motorOn = true, killSwitchActive = false, rideMode, timestampMs)
    }
}

data class ManualControls(
    val throttle: Boolean = false,
    val brake: Boolean = false,
    val motorOn: Boolean = false,
    val killActive: Boolean = false,
)