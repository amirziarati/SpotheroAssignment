package com.spothero.challenge

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun Int.spToDp(): Dp {
    val px = this * Resources.getSystem().displayMetrics.scaledDensity
    return px.pxToDp()
}

fun Int.ptToDp(): Dp {
    val px = (this / 72f) * Resources.getSystem().displayMetrics.densityDpi
    return px.pxToDp()
}

fun Int.ptToSp(): TextUnit {
    val px = (this / 72f) * Resources.getSystem().displayMetrics.densityDpi
    return (px / Resources.getSystem().displayMetrics.scaledDensity).sp
}

fun Float.pxToDp(): Dp {
    return (this / Resources.getSystem().displayMetrics.density).dp
}
