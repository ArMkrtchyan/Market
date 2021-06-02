package com.armboldmind.grandmarket.shared.globalextensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

fun Context.getColorCompat(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.getColorStateListCompat(@ColorRes resId: Int) = ContextCompat.getColorStateList(this, resId)

fun Context.getDrawableCompat(@DrawableRes resId: Int) = ResourcesCompat.getDrawable(resources, resId, theme)

fun Context.getDimensions(@DimenRes resId: Int) = resources.getDimension(resId)

fun Context.getStyledAttribute(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}