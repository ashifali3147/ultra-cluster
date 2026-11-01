package com.tlw.ultracluster.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.ultracluster.ui.AlertUi
import com.tlw.ultracluster.ui.Tone
import com.tlw.ultracluster.ui.theme.ClusterColors
import com.tlw.ultracluster.ui.theme.ClusterTheme
import com.tlw.ultracluster.ui.theme.ClusterType

@Composable
fun AlertColumn(
    alerts: List<AlertUi>,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = alerts,
        contentKey = { list -> list.map { it.title } },
        transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
        label = "alerts",
        modifier = modifier,
    ) { list ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .semantics { liveRegion = LiveRegionMode.Polite },
            verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically),
        ) {
            list.forEach { alert -> AlertPill(alert) }
        }
    }
}

@Composable
fun AlertPill(alert: AlertUi, modifier: Modifier = Modifier) {
    val c = ClusterTheme.colors
    val look = c.lookFor(alert.tone)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(look.bg)
            .padding(horizontal = 22.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(14.dp)
                .background(look.dot, CircleShape),
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(alert.title, style = ClusterType.alertTitle, color = look.fg)
            Text(alert.body, style = ClusterType.body, color = look.fg)
        }
    }
}
private data class PillLook(val bg: Color, val fg: Color, val dot: Color)

private fun ClusterColors.lookFor(tone: Tone) = when (tone) {
    Tone.CALM -> PillLook(bg = panel, fg = ink, dot = sub)
    Tone.INFO -> PillLook(bg = panel, fg = ink, dot = ride)
    Tone.STOP -> PillLook(bg = orange, fg = onOrange, dot = onOrange)
    Tone.WARN -> PillLook(bg = amberBg, fg = amberFg, dot = amberFg)
    Tone.OVER -> PillLook(bg = red, fg = onOrange, dot = onOrange)
}



@Preview(name = "Mixed, day", widthDp = 300, heightDp = 480, showBackground = true)
@Composable
private fun AlertsDayPreview() = ClusterTheme(isNight = false) {
    AlertColumn(
        listOf(
            AlertUi(Tone.OVER, "Over 80 km/h", "Ease off the throttle."),
            AlertUi(Tone.INFO, "Speeding up", "Speed has gone up since the last reading."),
        ),
        Modifier.width(300.dp),
    )
}

@Preview(name = "Stop, night", widthDp = 300, heightDp = 480, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AlertsNightPreview() = ClusterTheme(isNight = true) {
    AlertColumn(
        listOf(AlertUi(Tone.STOP, "Throttle ignored", "Safe to wait at a signal or get off.")),
        Modifier.width(300.dp),
    )
}