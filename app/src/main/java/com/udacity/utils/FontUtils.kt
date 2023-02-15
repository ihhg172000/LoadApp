package com.udacity.utils

import android.content.res.Resources
import android.util.TypedValue

fun Number.spToPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
}