package com.tlw.ultracluster.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tlw.ultracluster.data.RideMode
import com.tlw.ultracluster.ui.theme.ClusterTheme
import com.tlw.ultracluster.ui.theme.ClusterType
import com.tlw.ultracluster.ui.theme.mode

@Composable
fun TopBar(
    title: String,
    mode: RideMode,
    killActive: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = ClusterTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .padding(horizontal = 36.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ModePill(mode)
            Text(
                text = title,
                style = ClusterType.title,
                color = if (killActive) colors.orange else colors.ink,   // title turns orange when motor is cut
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Rounded.PhoneAndroid,
                contentDescription = "Phone connected",
                tint = colors.sub,
                modifier = Modifier.size(22.dp),
            )
            VerticalDivider(
                modifier = Modifier.height(22.dp).padding(horizontal = 4.dp),
                thickness = 2.dp,
                color = colors.rule,
            )
            BatteryIcon(percent = 78)
            Text("78%", style = ClusterType.label, fontWeight = FontWeight.Bold, color = colors.ink)
            Text("62 km range", style = ClusterType.label, color = colors.sub)
        }
    }
}

@Composable
fun ModePill(mode: RideMode) {
    val colors = ClusterTheme.colors
    val (icon, label) = when (mode) {
        RideMode.ECO -> Icons.Rounded.Eco to "Eco"
        RideMode.RIDE -> Icons.Rounded.Speed to "Ride"
        RideMode.POWER -> Icons.Rounded.Bolt to "Power"
    }
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(colors.mode(mode))
            .padding(start = 14.dp, end = 18.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Ride Mode",
            tint = colors.onMode,
            modifier = Modifier.size(22.dp)
        )
        Text(label, style = ClusterType.label, fontWeight = FontWeight.Bold, color = colors.onMode)
    }
}

@Composable
private fun BatteryIcon(percent: Int) {
    val c = ClusterTheme.colors
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(30.dp)
                .height(16.dp)
                .border(2.dp, c.ink, RoundedCornerShape(4.dp))
                .padding(3.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percent.coerceIn(0, 100) / 100f)
                    .background(c.ink, RoundedCornerShape(1.5.dp)),
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 1.dp)
                .width(3.dp)
                .height(6.dp)
                .background(c.ink, RoundedCornerShape(1.dp)),
        )
    }
}

@Preview(name = "Day", widthDp = 1280, heightDp = 84, showBackground = true)
@Composable
private fun TopBarDayPreview() {
    ClusterTheme(isNight = false) {
        TopBar(title = "Riding", mode = RideMode.ECO, killActive = false)
    }
}

@Preview(name = "Night, kill switch", widthDp = 1280, heightDp = 84, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TopBarNightPreview() {
    ClusterTheme(isNight = true) {
        TopBar(title = "Motor stopped", mode = RideMode.POWER, killActive = true)
    }
}