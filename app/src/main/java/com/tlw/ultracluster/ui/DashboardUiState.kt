package com.tlw.ultracluster.ui

import androidx.compose.runtime.Immutable
import com.tlw.ultracluster.data.RideMode
import com.tlw.ultracluster.data.SimStatus

@Immutable
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
    val sim: SimStatus,
) {
    companion object {
        val Initial = DashboardUiState(
            title = "Starting",
            hint = "Waiting for the first reading.",
            speed = SpeedUi.Unavailable,
            isAccelerating = false,
            motor = MotorUi.UNKNOWN,
            motorSubtitle = "No reading",
            killSwitch = KillSwitchUi.UNKNOWN,
            alerts = emptyList(),
            rideMode = RideMode.ECO,
            isStale = false,
            isNight = false,
            sim = SimStatus(),
        )
    }
}

enum class Tone { CALM, INFO, STOP, WARN, OVER }

data class AlertUi(
    val tone: Tone,
    val title: String,
    val body: String,
)

enum class KillSwitchUi {
    ACTIVE, INACTIVE, UNKNOWN
}

enum class MotorUi {
    ON, OFF, UNKNOWN
}

sealed interface SpeedUi {
    data class Value(val kmh: Int) : SpeedUi
    data object Invalid : SpeedUi
    data object Unavailable : SpeedUi
}