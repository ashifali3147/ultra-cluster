package com.tlw.ultracluster.ui

data class DashboardUiState(
    val title: String,
    val hint: String,
    val speed: SpeedUi,
    val isAccelerating: Boolean,
    val motor: MotorUi,
    val motorSubtitle: String,
    val killSwitch: KillSwitchUi,
    val alerts: List<AlertUi>,
    val rideMode: RideMode,
    val isStale: Boolean,
    val isNight: Boolean,
    val sim: SimUi,
)

enum class SimUi {

}

enum class RideMode {

}

enum class AlertUi {

}

enum class KillSwitchUi {

}

enum class MotorUi {

}

enum class SpeedUi {

}