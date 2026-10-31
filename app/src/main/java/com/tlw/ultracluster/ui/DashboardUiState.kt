package com.tlw.ultracluster.ui

import androidx.compose.runtime.Immutable
import com.tlw.ultracluster.data.Fault
import com.tlw.ultracluster.data.ManualControls
import com.tlw.ultracluster.data.RideMode
import com.tlw.ultracluster.data.Scenario
import com.tlw.ultracluster.data.SimMode

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
    val sim: SimUi,
)

data class SimUi(
    val mode: SimMode = SimMode.AUTO,
    val playing: Boolean = true,
    val current: Scenario? = Scenario.PARKED,
    val fault: Fault? = null,
    val controls: ManualControls = ManualControls(),
)

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