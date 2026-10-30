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
    MOTOR_DISABLED("Motor disabled", 0f, false, true),
}