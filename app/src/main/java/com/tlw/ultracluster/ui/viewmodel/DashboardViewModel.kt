package com.tlw.ultracluster.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tlw.ultracluster.data.SimulatedVehicleDataSource
import com.tlw.ultracluster.data.VehicleDataSource
import com.tlw.ultracluster.data.VehicleSignal
import com.tlw.ultracluster.ui.DashboardEvent
import com.tlw.ultracluster.ui.DashboardMapper
import com.tlw.ultracluster.ui.DashboardUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel(
    private val source: VehicleDataSource,
    private val mapper: DashboardMapper = DashboardMapper(),
    private val staleAfterMs: Long = 3_000L,
) : ViewModel() {

    private val isNight = MutableStateFlow(false)

    private data class Tick(val signal: VehicleSignal, val previousSpeedKhm: Float?)

    val uiState: StateFlow<DashboardUiState> =
        source.signal.scan<VehicleSignal, Tick?>(null) { prev, signal ->
            val lastValid = prev?.let { p ->
                p.signal.speedKmh?.takeIf { it in 0f..120f } ?: p.previousSpeedKhm
            }

            Tick(signal, lastValid)
        }
            .filterNotNull()
            .transformLatest { tick ->
                emit(mapper.map(tick.signal, tick.previousSpeedKhm))
                delay(staleAfterMs.milliseconds)
                emit(mapper.stale(tick.signal))
            }
            .catch { emit(mapper.unavailable()) }
            .combine(isNight) { state, night -> state.copy(isNight = night) }
            .combine(source.status) { state, sim -> state.copy(sim = sim) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DashboardUiState.Initial
            )

    fun onEvent(event: DashboardEvent) {
        when (event) {
            DashboardEvent.BrakeDown -> source.updateControl { it.copy(brake = true) }
            DashboardEvent.BrakeUp -> source.updateControl { it.copy(brake = false) }
            is DashboardEvent.JumpTo -> source.jumpTo(event.scenario)
            DashboardEvent.Pause -> source.pause()
            DashboardEvent.Play -> source.play()
            is DashboardEvent.SetFault -> source.setFault(event.fault)
            is DashboardEvent.SetKill -> source.updateControl { it.copy(killActive = event.active) }
            is DashboardEvent.SetRideMode -> source.setRideMode(event.mode)
            is DashboardEvent.SetSimMode -> source.setMode(event.mode)
            DashboardEvent.ThrottleDown -> source.updateControl { it.copy(throttle = true) }
            DashboardEvent.ThrottleUp -> source.updateControl { it.copy(throttle = false) }
            DashboardEvent.ToggleMotor -> source.updateControl { it.copy(motorOn = !it.motorOn) }
            DashboardEvent.ToggleNight -> isNight.update { !it }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { DashboardViewModel(SimulatedVehicleDataSource()) }
        }
    }
}