package com.tlw.ultracluster.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


object ClusterType {
    val speed = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 220.sp, lineHeight = 200.sp, letterSpacing = (-4).sp,
        fontFeatureSettings = "tnum"
    )
    val unit = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp)
    val title = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 36.sp)
    val motor = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 52.sp)
    val kill = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 32.sp)
    val alertTitle = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
    val body = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp)
    val label = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 19.sp)
}