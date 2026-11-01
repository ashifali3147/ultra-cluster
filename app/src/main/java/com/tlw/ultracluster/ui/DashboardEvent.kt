package com.tlw.ultracluster.ui

import com.tlw.ultracluster.data.Fault
import com.tlw.ultracluster.data.RideMode
import com.tlw.ultracluster.data.Scenario
import com.tlw.ultracluster.data.SimMode

sealed interface DashboardEvent {
    data object Play : DashboardEvent
    data object Pause : DashboardEvent
    data class JumpTo(val scenario: Scenario) : DashboardEvent
    data class SetFault(val fault: Fault?) : DashboardEvent
    data class SetSimMode(val mode: SimMode) : DashboardEvent
    data class SetRideMode(val mode: RideMode) : DashboardEvent
    data object ToggleNight : DashboardEvent
    data object ThrottleDown : DashboardEvent
    data object ThrottleUp : DashboardEvent
    data object BrakeDown : DashboardEvent
    data object BrakeUp : DashboardEvent
    data object ToggleMotor : DashboardEvent
    data class SetKill(val active: Boolean) : DashboardEvent
}