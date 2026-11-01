package com.tlw.ultracluster.data

enum class Scenario(
    val label: String,
    val speedKmh: Float,
    val motorOn: Boolean,
    val killSwitchActive: Boolean,
) {
    PARKED("Parked", 0f, false, false),
    READY("Ready", 0f, true, false),
    RIDING("Riding", 25f, true, false),
    ACCELERATING("Accelerating", 50f, true, false),
    MOTOR_DISABLED("Motor disabled", 0f, false, true);

    fun toSignal(rideMode: RideMode, timestampMs: Long) = VehicleSignal(
        speedKmh = speedKmh,
        motorOn = motorOn,
        killSwitchActive = killSwitchActive,
        rideMode = rideMode,
        timestampMs = timestampMs,
    )

    fun next(): Scenario = entries[(ordinal + 1) % entries.size]
}