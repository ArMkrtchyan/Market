package com.armboldmind.grandmarket.shared.globalextensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import com.armboldmind.grandmarket.R
import kotlin.math.roundToInt


private val density by lazy { Resources.getSystem().displayMetrics.density }

private val densityDpi by lazy { Resources.getSystem().displayMetrics.densityDpi }

fun Int.dpToPx(): Int {
    return (this * density).roundToInt()
}

fun Double.dpToPx(): Int {
    return (this * density).roundToInt()
}

fun Int.pxToDp(): Float {
    return this / (densityDpi / 160f)
}

fun Float.dpToPx(): Int {
    return (this * density).roundToInt()
}

fun Float.pxToDp(): Float {
    return this / (densityDpi / 160f)
}

fun Context.getDisplayHeight(): Int {
    val displayMetrics = DisplayMetrics()
    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

fun Context.getDisplayWidth(): Int {
    val displayMetrics = DisplayMetrics()
    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun Context.getListPreferredItemHeight(): Int {
    return TypedValue.complexToDimensionPixelSize(getStyledAttribute(R.attr.listPreferredItemHeight), this.resources.displayMetrics)
}

fun Context.getNavigationBarHeight(): Int {
    val resources = this.resources
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

fun Activity.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context.getActionBarHeight(): Int {
    var actionBarHeight = 0
    val tv = TypedValue()
    if (this.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    }
    return actionBarHeight
}