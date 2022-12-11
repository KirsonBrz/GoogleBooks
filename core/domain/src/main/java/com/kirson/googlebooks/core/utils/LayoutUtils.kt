package com.kirson.googlebooks.core.utils

import kotlin.math.roundToInt

fun lerp(start: Int, stop: Int, fraction: Float): Int {
    return start + ((stop - start) * fraction.toDouble()).roundToInt()
}
