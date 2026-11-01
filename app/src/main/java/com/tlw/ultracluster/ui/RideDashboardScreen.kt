package com.tlw.ultracluster.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.tlw.ultracluster.ui.components.AlertColumn
import com.tlw.ultracluster.ui.components.FooterBar
import com.tlw.ultracluster.ui.components.HandlebarPanel
import com.tlw.ultracluster.ui.components.SimControlBar
import com.tlw.ultracluster.ui.components.SpeedReadout
import com.tlw.ultracluster.ui.components.SpeedTape
import com.tlw.ultracluster.ui.components.TopBar
import com.tlw.ultracluster.ui.theme.ClusterTheme
import kotlin.math.min


@Composable
fun RideDashboardScreen(state: DashboardUiState, onEvent: (DashboardEvent) -> Unit) {
    val colors = ClusterTheme.colors
    val killActive = state.killSwitch == KillSwitchUi.ACTIVE

    var showControls by rememberSaveable { mutableStateOf(true) }

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(colors.bg)
    ) {
        val scale = min(maxWidth / 1280.dp, maxHeight / 800.dp)
        val density = LocalDensity.current

        CompositionLocalProvider(
            LocalDensity provides Density(density.density * scale, fontScale = 1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.bg)
            ) {
                TopBar(title = state.title, mode = state.rideMode, killActive = killActive)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    HandlebarPanel(
                        motor = state.motor,
                        motorSubtitle = state.motorSubtitle,
                        killSwitch = state.killSwitch,
                        mode = state.rideMode,
                        modifier = Modifier
                            .width(350.dp)
                            .fillMaxHeight()
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(
                            30.dp,
                            Alignment.CenterVertically
                        ),
                    ) {
                        SpeedReadout(speed = state.speed)
                        SpeedTape(
                            speed = state.speed,
                            mode = state.rideMode,
                            killActive = killActive,
                            accelerating = state.isAccelerating
                        )
                    }

                    AlertColumn(
                        alerts = state.alerts,
                        modifier = Modifier
                            .width(300.dp)
                            .fillMaxHeight(),
                    )

                }

                FooterBar(
                    hint = state.hint,
                    isStale = state.isStale,
                    isNight = state.isNight,
                    controlsOpen = showControls,
                    onToggleNight = { onEvent(DashboardEvent.ToggleNight) },
                    onToggleControls = { showControls = !showControls},
                )

                AnimatedVisibility(
                    visible = showControls,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    SimControlBar(
                        sim = state.sim,
                        onEvent = onEvent,
                    )
                }

            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=720dp,orientation=landscape")
@Composable
fun PreviewRideDashboardScreen() {
    ClusterTheme(isNight = false) {
        RideDashboardScreen(
            state = DashboardUiState.Initial,
            onEvent = {},
        )
    }
}