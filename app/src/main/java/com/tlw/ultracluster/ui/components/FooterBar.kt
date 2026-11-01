package com.tlw.ultracluster.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.ultracluster.ui.theme.ClusterTheme
import com.tlw.ultracluster.ui.theme.ClusterType

private val ButtonShape = RoundedCornerShape(12.dp)

@Composable
fun FooterBar(
    hint: String,
    isStale: Boolean,
    isNight: Boolean,
    controlsOpen: Boolean,
    onToggleNight: () -> Unit,
    onToggleControls: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = ClusterTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
            .padding(start = 36.dp, end = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = hint,
            style = ClusterType.label.copy(fontSize = ClusterType.label.fontSize * 1.05f),
            color = c.ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(if (isStale) c.amber else c.ride, CircleShape),
            )
            Text(
                text = if (isStale) "Last update 3 s ago" else "Live",
                style = ClusterType.label,
                fontWeight = FontWeight.SemiBold,
                color = c.sub,
            )
        }

        OutlinedButton(
            onClick = onToggleNight,
            shape = ButtonShape,
            border = BorderStroke(2.dp, c.rule),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = c.ink),
            modifier = Modifier.height(48.dp),
        ) {
            Icon(
                imageVector = if (isNight) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = if (isNight) "  Day view" else "  Night view",
                style = ClusterType.label,
                fontWeight = FontWeight.Bold,
            )
        }

        Button(
            onClick = onToggleControls,
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = c.ink, contentColor = c.bg),
            modifier = Modifier.height(48.dp),
        ) {
            Icon(Icons.Rounded.Tune, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(
                text = if (controlsOpen) "  Hide controls" else "  Controls",
                style = ClusterType.label,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}


@Preview(name = "Live, day", widthDp = 1280, heightDp = 76, showBackground = true)
@Composable
private fun FooterDayPreview() = ClusterTheme(isNight = false) {
    FooterBar(
        hint = "All signals normal.",
        isStale = false, isNight = false, controlsOpen = true,
        onToggleNight = {}, onToggleControls = {},
    )
}