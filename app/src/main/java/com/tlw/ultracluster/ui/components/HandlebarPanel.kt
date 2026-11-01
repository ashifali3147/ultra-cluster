package com.tlw.ultracluster.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.ultracluster.data.RideMode
import com.tlw.ultracluster.ui.KillSwitchUi
import com.tlw.ultracluster.ui.MotorUi
import com.tlw.ultracluster.ui.theme.ClusterTheme
import com.tlw.ultracluster.ui.theme.ClusterType
import com.tlw.ultracluster.ui.theme.mode

private const val COLOR_MS = 250

@Composable
fun HandlebarPanel(
    motor: MotorUi,
    motorSubtitle: String,
    killSwitch: KillSwitchUi,
    mode: RideMode,
    modifier: Modifier = Modifier,
) {
    val color = ClusterTheme.colors
    val killActive = killSwitch == KillSwitchUi.ACTIVE


    val panelBg by animateColorAsState(
        if (killActive) color.orange else color.panel,
        tween(COLOR_MS),
        label = "panelBg"
    )
    val ink by animateColorAsState(
        if (killActive) color.onOrange else color.ink,
        tween(COLOR_MS),
        label = "ink"
    )
    val sub by animateColorAsState(
        if (killActive) color.onOrangeSub else color.sub,
        tween(COLOR_MS),
        label = "sub"
    )
    val rule = if (killActive) color.onOrangeSub.copy(alpha = 0.4f) else color.rule

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(panelBg)
            .padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Motor", style = ClusterType.label, color = sub)
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MotorTelltale(on = motor == MotorUi.ON, mode = mode, idleColor = sub)
                Text(
                    text = when (motor) {
                        MotorUi.ON -> "On"
                        MotorUi.OFF -> "Off"
                        MotorUi.UNKNOWN -> "Unknown"
                    },
                    style = ClusterType.motor,
                    color = ink,
                )
            }
            Text(motorSubtitle, style = ClusterType.body, color = sub)
        }

        HorizontalDivider(thickness = 2.dp, color = rule)


        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Kill switch", style = ClusterType.label, color = sub)
            KillSwitchRocker(state = killSwitch)
            Text(
                text = when (killSwitch) {
                    KillSwitchUi.ACTIVE -> "Active"
                    KillSwitchUi.INACTIVE -> "Inactive"
                    KillSwitchUi.UNKNOWN -> "Unknown"
                },
                style = ClusterType.kill,
                color = ink,
            )
        }
    }
}

@Composable
fun MotorTelltale(on: Boolean, mode: RideMode, idleColor: Color) {
    val color = ClusterTheme.colors
    val shape = RoundedCornerShape(14.dp)
    val fill by animateColorAsState(
        if (on) color.mode(mode) else Color.Transparent,
        tween(200),
        label = "tFill"
    )
    val ring by animateColorAsState(
        if (on) color.mode(mode) else idleColor,
        tween(200),
        label = "tRing"
    )
    val icon by animateColorAsState(
        if (on) color.onMode else idleColor,
        tween(200),
        label = "tIcon"
    )

    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(shape)
            .background(fill)
            .border(3.dp, ring, shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.PowerSettingsNew,
            contentDescription = if (on) "Motor on" else "Motor off",
            tint = icon,
            modifier = Modifier.size(30.dp),
        )
    }
}

@Composable
fun KillSwitchRocker(state: KillSwitchUi) {
    val color = ClusterTheme.colors
    val active = state == KillSwitchUi.ACTIVE
    val inactive = state == KillSwitchUi.INACTIVE
    val shape = RoundedCornerShape(18.dp)

    val edge by animateColorAsState(
        if (active) color.onOrange else color.ink,
        tween(COLOR_MS),
        label = "edge"
    )
    val idle by animateColorAsState(
        if (active) color.rockerIdle else color.bg,
        tween(COLOR_MS),
        label = "idle"
    )
    val dim = if (active) color.onOrangeSub else color.sub

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(shape)
            .background(idle)
            .border(3.dp, edge, shape)
            .semantics {
                contentDescription = when (state) {
                    KillSwitchUi.ACTIVE -> "Kill switch at Stop"
                    KillSwitchUi.INACTIVE -> "Kill switch at Run"
                    KillSwitchUi.UNKNOWN -> "Kill switch position unknown"
                }
            },
    ) {
        RockerHalf(
            icon = Icons.Rounded.Refresh, label = "Run",
            lit = inactive, litBg = color.ink, litFg = color.bg, dimFg = dim,
            modifier = Modifier.weight(1f),
        )
        RockerHalf(
            icon = Icons.Rounded.Block, label = "Stop",
            lit = active, litBg = color.onOrange, litFg = color.orange, dimFg = dim,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun RockerHalf(
    icon: ImageVector,
    label: String,
    lit: Boolean,
    litBg: Color,
    litFg: Color,
    dimFg: Color,
    modifier: Modifier = Modifier,
) {
    val bg by animateColorAsState(
        if (lit) litBg else Color.Transparent,
        tween(200),
        label = "halfBg"
    )
    val fg by animateColorAsState(if (lit) litFg else dimFg, tween(200), label = "halfFg")

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(bg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(28.dp))
        Text(label, style = ClusterType.label, fontWeight = FontWeight.Bold, color = fg)
    }
}


@Preview(name = "Riding, day", widthDp = 310, heightDp = 480)
@Composable
private fun HandlebarRidingPreview() = ClusterTheme(isNight = false) {
    HandlebarPanel(
        MotorUi.OFF, "Driving the wheel", KillSwitchUi.INACTIVE, RideMode.ECO,
        Modifier
            .width(310.dp)
            .fillMaxHeight()
    )
}