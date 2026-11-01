package com.tlw.ultracluster.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.ultracluster.ui.SpeedUi
import com.tlw.ultracluster.ui.theme.ClusterTheme
import com.tlw.ultracluster.ui.theme.ClusterType

@Composable
fun SpeedReadout(
    speed: SpeedUi,
    modifier: Modifier = Modifier,
) {
    val c = ClusterTheme.colors
    val valid = speed is SpeedUi.Value

    val target = (speed as? SpeedUi.Value)?.kmh ?: 0
    val shown by animateIntAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
        label = "speed",
    )

    val description = when (speed) {
        is SpeedUi.Value -> "Speed ${speed.kmh} kilometres per hour"
        SpeedUi.Invalid -> "Speed reading invalid"
        SpeedUi.Unavailable -> "Speed not available"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = if (valid) shown.toString() else "--",
            style = ClusterType.speed,
            color = if (valid) c.ink else c.naInk,
            modifier = Modifier.alignByBaseline(),
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = "km/h",
            style = ClusterType.unit,
            color = c.sub,
            modifier = Modifier.alignByBaseline(),
        )
    }
}

@Preview(name = "50 km/h, day", widthDp = 600, heightDp = 260, showBackground = true)
@Composable
private fun SpeedValuePreview() = ClusterTheme(isNight = false) {
    SpeedReadout(SpeedUi.Value(50))
}