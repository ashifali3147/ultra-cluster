package com.tlw.ultracluster.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RadioButtonChecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tlw.ultracluster.data.Fault
import com.tlw.ultracluster.data.ManualControls
import com.tlw.ultracluster.data.RideMode
import com.tlw.ultracluster.data.Scenario
import com.tlw.ultracluster.data.SimMode
import com.tlw.ultracluster.data.SimStatus
import com.tlw.ultracluster.ui.DashboardEvent
import com.tlw.ultracluster.ui.theme.ClusterTheme


private object SimPalette {
    val bar = Color(0xFF111417)
    val button = Color(0xFF22282D)
    val border = Color(0xFF3A444C)
    val text = Color(0xFFF6F7F3)
    val textSub = Color(0xFFB7C0C7)
    val selected = Color(0xFFF6F7F3)
    val onSelected = Color(0xFF111417)
    val play = Color(0xFF2350D8)
    val faultOn = Color(0xFFF2B233)
    val stop = Color(0xFFD9480F)
    val eco = Color(0xFF0B7A43)
    val ride = Color(0xFF2350D8)
    val power = Color(0xFF7A2BD9)
}

private val Round14 = RoundedCornerShape(14.dp)
private val Round20 = RoundedCornerShape(20.dp)

@Composable
fun SimControlBar(
    sim: SimStatus,
    onEvent: (DashboardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(SimPalette.bar)
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(
            modifier = Modifier
                .width(210.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SegmentedToggle(
                options = SimMode.entries,
                selected = sim.mode,
                label = { if (it == SimMode.AUTO) "Auto demo" else "Manual ride" },
                selectedColor = { SimPalette.selected },
                selectedTextColor = SimPalette.onSelected,
                onSelect = { onEvent(DashboardEvent.SetSimMode(it)) },
                modifier = Modifier.height(56.dp),
            )
            Text(
                text = if (sim.mode == SimMode.AUTO) "The required sequence, one reading per second."
                else "You ride it. Same data source, same rules.",
                fontSize = 14.sp,
                color = SimPalette.textSub,
                lineHeight = 18.sp,
            )
            SegmentedToggle(
                options = RideMode.entries,
                selected = sim.rideMode,
                label = { it.name.lowercase().replaceFirstChar(Char::uppercase) },
                selectedColor = {
                    when (it) {
                        RideMode.ECO -> SimPalette.eco
                        RideMode.RIDE -> SimPalette.ride
                        RideMode.POWER -> SimPalette.power
                    }
                },
                selectedTextColor = Color.White,
                onSelect = { onEvent(DashboardEvent.SetRideMode(it)) },
                modifier = Modifier.weight(1f),
            )
        }

        if (sim.mode == SimMode.AUTO) {
            AutoPane(sim, onEvent, Modifier.weight(1f))
        } else {
            ManualPane(sim, onEvent, Modifier.weight(1f))
        }
    }
}

// ======================= AUTO =======================

@Composable
private fun AutoPane(sim: SimStatus, onEvent: (DashboardEvent) -> Unit, modifier: Modifier) {
    val running = sim.playing && sim.fault == null

    Row(modifier = modifier.fillMaxHeight(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        SimButton(
            onClick = { onEvent(if (running) DashboardEvent.Pause else DashboardEvent.Play) },
            background = SimPalette.play,
            modifier = Modifier
                .width(120.dp)
                .fillMaxHeight(),
        ) {
            Icon(
                imageVector = if (running) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp),
            )
            Text(
                if (running) "Pause" else "Play",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Scenario.entries.forEachIndexed { i, scenario ->
                    val on = sim.fault == null && sim.scenario == scenario
                    SimButton(
                        onClick = { onEvent(DashboardEvent.JumpTo(scenario)) },
                        background = if (on) SimPalette.selected else SimPalette.button,
                        border = if (on) SimPalette.selected else SimPalette.border,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        alignStart = true,
                    ) {
                        val fg = if (on) SimPalette.onSelected else SimPalette.text
                        Text("Step ${i + 1}", fontSize = 13.sp, color = fg)
                        Text(
                            scenario.label,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = fg,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Fault.entries.forEach { fault ->
                    FaultButton(
                        fault,
                        active = sim.fault == fault,
                        onEvent = onEvent,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}


// ======================= MANUAL =======================
@Composable
private fun ManualPane(sim: SimStatus, onEvent: (DashboardEvent) -> Unit, modifier: Modifier) {
    val ctl = sim.controls
    val killed = ctl.killActive
    val driving = ctl.throttle && ctl.motorOn && !killed

    Row(modifier = modifier.fillMaxHeight(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {

        Column(
            modifier = Modifier
                .width(180.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SimButton(
                onClick = { if (!killed) onEvent(DashboardEvent.ToggleMotor) },
                background = if (ctl.motorOn) SimPalette.play else SimPalette.button,
                border = if (ctl.motorOn) SimPalette.play else SimPalette.border,
                enabled = !killed,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = when {
                        killed -> "Motor blocked"
                        ctl.motorOn -> "Stop motor"
                        else -> "Start motor"
                    },
                    color = if (killed) SimPalette.textSub else Color.White,
                    fontWeight = FontWeight.Bold, fontSize = 17.sp,
                )
            }
            SegmentedToggle(
                options = listOf(false, true),
                selected = killed,
                label = { if (it) "Stop" else "Run" },
                selectedColor = { if (it) SimPalette.stop else SimPalette.selected },
                selectedTextColor = SimPalette.onSelected,
                onSelect = { onEvent(DashboardEvent.SetKill(it)) },
                modifier = Modifier.weight(1f),
            )
        }

        Column(
            modifier = Modifier
                .width(170.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(Fault.INVALID_SPEED, Fault.SILENT).forEach { fault ->
                FaultButton(
                    fault,
                    active = sim.fault == fault,
                    onEvent = onEvent,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }

        HoldButton(
            label = "Brake",
            note = "Hold",
            icon = Icons.Rounded.RadioButtonChecked,
            background = SimPalette.button,
            pressedBackground = SimPalette.border,
            onPress = { onEvent(DashboardEvent.BrakeDown) },
            onRelease = { onEvent(DashboardEvent.BrakeUp) },
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight(),
        )

        HoldButton(
            label = "Throttle",
            note = when {
                killed -> "Ignored, kill switch at Stop"
                !ctl.motorOn -> "Start the motor first"
                else -> "Hold to speed up"
            },
            icon = Icons.Rounded.KeyboardArrowUp,
            background = if (driving) Color(0xFF1A3FB8) else SimPalette.play,
            pressedBackground = Color(0xFF1A3FB8),
            onPress = { onEvent(DashboardEvent.ThrottleDown) },
            onRelease = { onEvent(DashboardEvent.ThrottleUp) },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )
    }
}

@Composable
private fun FaultButton(
    fault: Fault,
    active: Boolean,
    onEvent: (DashboardEvent) -> Unit,
    modifier: Modifier
) {
    SimButton(
        onClick = { onEvent(DashboardEvent.SetFault(if (active) null else fault)) },
        background = SimPalette.button,
        border = if (active) SimPalette.faultOn else SimPalette.border,
        modifier = modifier,
    ) {
        Text(
            fault.label,
            color = SimPalette.text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SimButton(
    onClick: () -> Unit,
    background: Color,
    modifier: Modifier = Modifier,
    border: Color = background,
    enabled: Boolean = true,
    alignStart: Boolean = false,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(Round14)
            .background(background)
            .border(BorderStroke(2.dp, border), Round14)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
        horizontalAlignment = if (alignStart) Alignment.Start else Alignment.CenterHorizontally,
    ) { content() }
}

@Composable
private fun HoldButton(
    label: String,
    note: String,
    icon: ImageVector,
    background: Color,
    pressedBackground: Color,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var pressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(Round20)
            .background(if (pressed) pressedBackground else background)
            .semantics { role = Role.Button; contentDescription = "$label, press and hold" }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        onPress()
                        tryAwaitRelease()
                        pressed = false
                        onRelease()
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
            Text(label, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
            Text(note, color = Color(0xFFDCE4FF), fontSize = 14.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun <T> SegmentedToggle(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    selectedColor: (T) -> Color,
    selectedTextColor: Color,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(Round14)
            .background(SimPalette.button)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEach { option ->
            val on = option == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (on) selectedColor(option) else Color.Transparent)
                    .clickable(role = Role.Tab) { onSelect(option) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label(option),
                    color = if (on) selectedTextColor else SimPalette.text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                )
            }
        }
    }
}


@Preview(name = "Auto", widthDp = 1280, heightDp = 200)
@Composable
private fun SimAutoPreview() = ClusterTheme(isNight = false) {
    SimControlBar(sim = SimStatus(scenario = Scenario.RIDING), onEvent = {})
}

@Preview(name = "Manual, killed", widthDp = 1280, heightDp = 200)
@Composable
private fun SimManualPreview() = ClusterTheme(isNight = false) {
    SimControlBar(
        sim = SimStatus(
            mode = SimMode.MANUAL,
            controls = ManualControls(killActive = true)
        ),
        onEvent = {},
    )
}