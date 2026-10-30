package com.tlw.ultracluster.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ClusterColors(
    val bg: Color,
    val panel: Color,
    val ink: Color,
    val sub: Color,
    val rule: Color,
    val segOff: Color,
    val segNA: Color,
    val naInk: Color,
    val amber: Color,
    val red: Color,
    val orange: Color,
    val onOrange: Color,
    val onOrangeSub: Color,
    val rockerIdle: Color,
    val amberBg: Color,
    val amberFg: Color,
    val eco: Color,
    val ecoHi: Color,
    val ride: Color,
    val rideHi: Color,
    val power: Color,
    val powerHi: Color,
    val onMode: Color,
    val isNight: Boolean,
)

val DayColor = ClusterColors(
    bg = Color(0xFFFFFFFF),
    panel = Color(0xFFEDF0F3),
    ink = Color(0xFF000000),
    sub = Color(0xFF2E3338),
    rule = Color(0xFFC3CAD1),
    segOff = Color(0xFFD3D9DF),
    segNA = Color(0xFFE7EAEE),
    naInk = Color(0xFF7A838B),
    amber = Color(0xFFD98A00),
    red = Color(0xFFD11A2A),
    orange = Color(0xFFC2410C),
    onOrange = Color(0xFFFFFFFF),
    onOrangeSub = Color(0xFF9A330A),
    rockerIdle = Color(0xFFFFFFFF),
    amberBg = Color(0xFFFFE3A6),
    amberFg = Color(0xFF4A2E00),
    eco = Color(0xFF0B7A43),
    ecoHi = Color(0xFF3FB37A),
    ride = Color(0xFF0040E6),
    rideHi = Color(0xFF5A85FF),
    power = Color(0xFF7A2BD9),
    powerHi = Color(0xFFA97BF0),
    onMode = Color(0xFFFFFFFF),
    isNight = false,
)