package com.tlw.ultracluster.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.ultracluster.data.RideMode
import com.tlw.ultracluster.ui.SpeedUi
import com.tlw.ultracluster.ui.theme.ClusterColors
import com.tlw.ultracluster.ui.theme.ClusterTheme
import com.tlw.ultracluster.ui.theme.ClusterType
import com.tlw.ultracluster.ui.theme.mode
import com.tlw.ultracluster.ui.theme.modeHi
import kotlin.math.roundToInt


const val SEGMENTS = 20 // 20 segments × 5 km/h = 0–100 km/h
private const val KMH_PER_SEGMENT = 5f
@Composable
fun SpeedTape(
    speed: SpeedUi,
    mode: RideMode,
    killActive: Boolean,
    accelerating: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = ClusterTheme.colors
    val kmh = (speed as? SpeedUi.Value)?.kmh

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clearAndSetSemantics { },
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            repeat(SEGMENTS) { i ->
                val target = color.segmentColor(i, kmh, mode, killActive, accelerating)
                val color by animateColorAsState(target, tween(300), label = "seg$i")
                Box(Modifier.weight(1f).fillMaxHeight().background(color = color, RoundedCornerShape(3.dp)))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            listOf(0, 20, 40, 60, 80, 100).forEach { mark ->
                Text(
                    text = mark.toString(),
                    style = ClusterType.label.copy(fontFeatureSettings = "tnum"),
                    color = color.sub,
                )
            }
        }
    }
}

internal fun ClusterColors.segmentColor(
    index: Int,
    kmh: Int?,
    mode: RideMode,
    killActive: Boolean,
    accelerating: Boolean,
): Color {
    if (kmh == null) return segNA

    val lit = (kmh.coerceIn(0, 100) / KMH_PER_SEGMENT).roundToInt()
    return when {
        index >= lit -> segOff                                      // above current speed
        killActive -> ink                                           // no drive power: neutral, not "go" colours
        index >= 16 -> red                                          // 80–100 km/h
        index >= 12 -> amber                                        // 60–80 km/h
        accelerating && index >= lit - 2 -> modeHi(mode)            // brighter leading edge while speeding up
        else -> mode(mode)
    }
}

@Preview(
    name = "86, Eco, night",
    widthDp = 600,
    heightDp = 90,
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
private fun TapeOverSpeedPreview() = ClusterTheme(isNight = true) {
    SpeedTape(SpeedUi.Value(80), RideMode.ECO, killActive = false, accelerating = false)
}