package com.tlw.ultracluster.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface VehicleDataSource {
    val signal: Flow<VehicleSignal>

    val status: StateFlow<SimStatus>

    fun setMode(mode: SimMode)
    fun play()
    fun pause()
    fun jumpTo(scenario: Scenario)
    fun setFault(fault: Fault?)
    fun setRideMode(mode: RideMode)
    fun updateControl(change: (ManualControls) -> ManualControls)
}

data class SimStatus(
    val mode: SimMode = SimMode.AUTO,
    val playing: Boolean = true,
    val scenario: Scenario = Scenario.PARKED,
    val fault: Fault? = null,
    val rideMode: RideMode = RideMode.ECO,
    val controls: ManualControls = ManualControls(),
    val manualSpeedKmh: Float = 0f,
)
