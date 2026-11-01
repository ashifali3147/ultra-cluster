package com.tlw.ultracluster.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.tlw.ultracluster.data.RideMode
import com.tlw.ultracluster.ui.components.AlertColumn
import com.tlw.ultracluster.ui.components.FooterBar
import com.tlw.ultracluster.ui.components.HandlebarPanel
import com.tlw.ultracluster.ui.components.SpeedReadout
import com.tlw.ultracluster.ui.components.SpeedTape
import com.tlw.ultracluster.ui.components.TopBar
import com.tlw.ultracluster.ui.theme.ClusterTheme
import kotlin.math.min


@Composable
fun RideDashboardScreen() {
    val colors = ClusterTheme.colors
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
                TopBar(title = "Motor stopped", mode = RideMode.POWER, killActive = true) //TODO
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    HandlebarPanel(
                        MotorUi.OFF,
                        "Driving the wheel",
                        KillSwitchUi.INACTIVE,
                        RideMode.ECO,
                        Modifier
                            .width(350.dp)
                            .fillMaxHeight()
                    ) //TODO

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(
                            30.dp,
                            Alignment.CenterVertically
                        ),
                    ) {
                        SpeedReadout(speed = SpeedUi.Value(50)) //TODO
                        SpeedTape(
                            SpeedUi.Value(0),
                            RideMode.ECO,
                            killActive = false,
                            accelerating = false
                        )
                    }

                    AlertColumn(
                        alerts = listOf(
                            AlertUi(Tone.OVER, "Over 80 km/h", "Ease off the throttle."),
                            AlertUi(
                                Tone.INFO,
                                "Speeding up",
                                "Speed has gone up since the last reading."
                            ),
                        ),
                        modifier = Modifier
                            .width(300.dp)
                            .fillMaxHeight(),
                    ) //TODO

                }

                FooterBar(
                    hint = "All signals normal.",
                    isStale = false, isNight = false, controlsOpen = true,
                    onToggleNight = {}, onToggleControls = {},
                ) //TODO

            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=720dp,orientation=landscape")
@Composable
fun PreviewRideDashboardScreen() {
    ClusterTheme(isNight = false) {
        RideDashboardScreen()
    }
}