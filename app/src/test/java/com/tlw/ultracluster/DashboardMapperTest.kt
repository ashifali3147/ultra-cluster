package com.tlw.ultracluster

import com.tlw.ultracluster.data.RideMode
import com.tlw.ultracluster.data.VehicleSignal
import com.tlw.ultracluster.ui.DashboardMapper
import com.tlw.ultracluster.ui.KillSwitchUi
import com.tlw.ultracluster.ui.MotorUi
import com.tlw.ultracluster.ui.SpeedUi
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardMapperTest {

    private val mapper = DashboardMapper()

    private fun reading(speed: Float?, motorOn: Boolean?, kill: Boolean?) =
        VehicleSignal(speed, motorOn, kill, RideMode.ECO, timestampMs = 0L)

    @Test
    fun `motor on at 25 kmh shows riding`() {
        val state = mapper.map(reading(25f, motorOn = true, kill = false), previousSpeedKmh = 25f)

        assertEquals("Riding", state.title)
        assertEquals(SpeedUi.Value(25), state.speed)
        assertEquals(MotorUi.ON, state.motor)
    }

    @Test
    fun `negative speed is shown as invalid without hiding other signals`() {
        val state = mapper.map(reading(-12f, motorOn = true, kill = false), previousSpeedKmh = 25f)

        assertEquals(SpeedUi.Invalid, state.speed)
        assertEquals(MotorUi.ON, state.motor)
        assertEquals(KillSwitchUi.INACTIVE, state.killSwitch)
    }

    @Test
    fun `kill switch while moving keeps the real speed`() {
        val state = mapper.map(reading(18f, motorOn = false, kill = true), previousSpeedKmh = 20f)

        assertEquals("Motor stopped", state.title)
        assertEquals(SpeedUi.Value(18), state.speed)
        assertEquals(KillSwitchUi.ACTIVE, state.killSwitch)
    }
}