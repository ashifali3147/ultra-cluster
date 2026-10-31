package com.tlw.ultracluster.ui

import com.tlw.ultracluster.data.SimStatus
import com.tlw.ultracluster.data.VehicleSignal
import kotlin.math.roundToInt


class DashboardMapper(
    private val maxValidSpeedKmh: Float = 120f,
    private val overSpeedKmh: Int = 80,
    private val accelThresholdKmh: Float = 2f,
) {
    fun map(signal: VehicleSignal, previousSpeedKmh: Float?): DashboardUiState {
        val validSpeed: Float? = signal.speedKmh?.takeIf { isValidSpeed(it) }
        val speed: SpeedUi = validSpeed?.let { SpeedUi.Value(it.roundToInt()) } ?: SpeedUi.Invalid

        val motor = when (signal.motorOn) {
            true -> MotorUi.ON
            false -> MotorUi.OFF
            null -> MotorUi.UNKNOWN
        }

        val killSwitch = when (signal.killSwitchActive) {
            true -> KillSwitchUi.ACTIVE
            false -> KillSwitchUi.INACTIVE
            null -> KillSwitchUi.UNKNOWN
        }

        val moving = if (validSpeed == null) motor == MotorUi.ON else validSpeed > 0f

        val accelerating =
            validSpeed != null && previousSpeedKmh != null && motor == MotorUi.ON && killSwitch == KillSwitchUi.INACTIVE && validSpeed - previousSpeedKmh >= accelThresholdKmh

        val verdict = when {
            killSwitch == KillSwitchUi.UNKNOWN -> Verdict(
                title = "Kill switch not reporting",
                motorSubtitle = if (motor == MotorUi.UNKNOWN) "No reading" else motorWords(motor),
                hint = "Check the handlebar switch connection.",
                alert = AlertUi(
                    Tone.WARN,
                    "Missing kill switch data",
                    "No reading from the switch."
                ),
            )

            killSwitch == KillSwitchUi.ACTIVE && moving -> Verdict(
                title = "Motor stopped",
                motorSubtitle = "Cut by the kill switch",
                hint = "Use the brakes to slow down. Flip back to Run when you want power.",
                alert = AlertUi(
                    Tone.STOP,
                    "Coasting",
                    "No drive power, the scooter is still rolling."
                ),
            )

            killSwitch == KillSwitchUi.ACTIVE -> Verdict(
                title = "Motor stopped",
                motorSubtitle = "Cut by the kill switch",
                hint = "Flip the switch on your right handlebar back to Run to ride.",
                alert = AlertUi(
                    Tone.STOP,
                    "Throttle ignored",
                    "Safe to wait at a signal or get off."
                ),
            )

            motor == MotorUi.UNKNOWN -> Verdict(
                title = "Motor not reporting",
                motorSubtitle = "No reading",
                hint = "Speed and kill switch are still live.",
                alert = AlertUi(
                    Tone.WARN,
                    "Missing motor data",
                    "No reading from the motor controller."
                ),
            )

            motor == MotorUi.OFF && moving -> Verdict(
                title = "Rolling",
                motorSubtitle = "Switched off",
                hint = "Motor is off. Press start when you want power again.",
                alert = AlertUi(
                    Tone.CALM,
                    "Coasting",
                    "No drive power, the scooter is still rolling."
                ),
            )

            motor == MotorUi.OFF -> Verdict(
                title = "Parked",
                motorSubtitle = "Not powered",
                hint = "Hold a brake lever and press start to switch the motor on.",
                alert = ALL_CLEAR,
            )

            !moving -> Verdict(
                title = "Ready to ride",
                motorSubtitle = "Waiting for throttle",
                hint = "The motor is silent. A small twist of the throttle will move you.",
                alert = AlertUi(
                    Tone.INFO,
                    "Auto-off in 0:25",
                    "The motor switches off if you stay idle."
                ),
            )

            else -> Verdict(
                title = "Riding",
                motorSubtitle = "Driving the wheel",
                hint = "All signals normal.",
                alert = if (accelerating)
                    AlertUi(Tone.INFO, "Speeding up", "Speed has gone up since the last reading.")
                else ALL_CLEAR,
            )
        }

        var alerts = listOf(verdict.alert)
        var hint = verdict.hint

        if (validSpeed == null) {
            val body = signal.speedKmh
                ?.let { "Got ${it.roundToInt()} km/h, which is not possible. Ignored." }
                ?: "No speed value received."
            alerts = listOf(AlertUi(Tone.WARN, "Speed sensor fault", body)) + alerts.withoutCalm()
            hint = "Speed comes back with the next valid reading."
        }

        if (validSpeed != null && validSpeed.roundToInt() > overSpeedKmh) {
            alerts =
                listOf(AlertUi(Tone.OVER, "Over $overSpeedKmh km/h", "Ease off the throttle.")) +
                        alerts.withoutCalm()
        }

        return DashboardUiState(
            title = verdict.title,
            hint = hint,
            speed = speed,
            isAccelerating = accelerating,
            motor = motor,
            motorSubtitle = verdict.motorSubtitle,
            killSwitch = killSwitch,
            alerts = alerts,
            rideMode = signal.rideMode,
            isStale = false,
            isNight = false,
            sim = SimStatus()
        )
    }

    fun stale(last: VehicleSignal): DashboardUiState = DashboardUiState.Initial.copy(
        title = "Speed sensor not responding",
        hint = "This is the vehicle feed, not your phone. Old numbers are hidden on purpose.",
        speed = SpeedUi.Unavailable,
        motorSubtitle = "No reading",
        alerts = listOf(AlertUi(Tone.WARN, "No vehicle data", "Nothing received for 3 seconds.")),
        rideMode = last.rideMode,
        isStale = true
    )

    fun unavailable(): DashboardUiState = DashboardUiState.Initial.copy(
        title = "Vehicle data unavailable",
        hint = "The data source stopped. Restart the app.",
        alerts = listOf(AlertUi(Tone.WARN, "Data source error", "No more readings will arrive.")),
        isStale = true,
    )

    private fun motorWords(motor: MotorUi) =
        if (motor == MotorUi.ON) "Driving the wheel" else "Not powered"

    private fun isValidSpeed(kmh: Float) = !kmh.isNaN() && kmh >= 0f && kmh <= maxValidSpeedKmh

    private fun List<AlertUi>.withoutCalm() = filter { it.tone != Tone.CALM }

    private data class Verdict(
        val title: String,
        val motorSubtitle: String,
        val hint: String,
        val alert: AlertUi,
    )

    private companion object {
        val ALL_CLEAR = AlertUi(Tone.CALM, "All clear", "No warnings right now.")
    }
}