package com.tlw.ultracluster.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.milliseconds

class SimulatedVehicleDataSource(
    private val autoTickMs: Long = 1_000L,
    private val manualTickMs: Long = 200L,
    private val ticksPerScenario: Int = 2,
    private val clock: () -> Long = System::currentTimeMillis,
) : VehicleDataSource {

    private val _status = MutableStateFlow(SimStatus())

    override val status: StateFlow<SimStatus>
        get() = _status.asStateFlow()

    override val signal: Flow<VehicleSignal> = flow {
        var ticksOnScenario = 0

        while (true) {
            val s = _status.value

            when (s.mode) {
                SimMode.AUTO -> {
                    val signal = if (s.fault != null) {
                        s.fault.toSignal(s.rideMode, clock())
                    } else {
                        s.scenario.toSignal(s.rideMode, clock())
                    }
                    signal?.let { emit(it) }

                    if (s.playing && s.fault == null) {
                        ticksOnScenario++
                        if (ticksOnScenario >= ticksPerScenario) {
                            ticksOnScenario = 0
                            _status.update { it.copy(scenario = it.scenario.next()) }
                        }
                    }
                    delay(autoTickMs.milliseconds)
                }

                SimMode.MANUAL -> {
                    val speed = nextManualSpeed(s)
                    _status.update { it.copy(manualSpeedKmh = speed) }

                    val control = s.controls
                    val signal = when (s.fault) {
                        Fault.INVALID_SPEED -> VehicleSignal(
                            -12f,
                            control.motorOn,
                            control.killActive,
                            s.rideMode,
                            clock()
                        )

                        Fault.SILENT -> null
                        else -> VehicleSignal(
                            speed,
                            control.motorOn,
                            control.killActive,
                            s.rideMode,
                            clock()
                        )
                    }

                    signal?.let { emit(it) }
                    delay(manualTickMs.milliseconds)
                }
            }
        }
    }

    private fun nextManualSpeed(s: SimStatus): Float = s.manualSpeedKmh

    override fun setMode(mode: SimMode) = _status.update {
        if (mode == SimMode.MANUAL) {
            it.copy(mode = mode, fault = null, controls = ManualControls(), manualSpeedKmh = 0f)
        } else {
            it.copy(mode = mode, fault = null, playing = true)
        }
    }

    override fun play() = _status.update { it.copy(playing = true, fault = null) }

    override fun pause() = _status.update { it.copy(playing = false) }

    override fun jumpTo(scenario: Scenario) =
        _status.update { it.copy(scenario = scenario, playing = false, fault = null) }

    override fun setFault(fault: Fault?) =
        _status.update { it.copy(fault = fault, playing = fault == null && it.playing) }

    override fun setRideMode(mode: RideMode) = _status.update { it.copy(rideMode = mode) }

    override fun updateControl(change: (ManualControls) -> ManualControls) = _status.update {
        val next = change(it.controls)
        it.copy(controls = if (next.killActive) next.copy(motorOn = false) else next)
    }
}