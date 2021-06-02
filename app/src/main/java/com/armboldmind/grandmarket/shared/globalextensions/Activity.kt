package com.armboldmind.grandmarket.shared.globalextensions

import android.os.Build
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import com.armboldmind.grandmarket.R


fun AppCompatActivity.withFullScreen() {
    window.apply {
        decorView.systemUiVisibility = if (Build.VERSION.SDK_INT > 27) {
            statusBarColor = getColorCompat(android.R.color.transparent)
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            if (Build.VERSION.SDK_INT > 22) {
                statusBarColor = getColorCompat(android.R.color.transparent)
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                statusBarColor = getColorCompat(android.R.color.transparent)
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
    }
}

fun AppCompatActivity.setLightStatusBar(@ColorRes statusColor: Int = android.R.color.white) {
    window.apply {
        if (Build.VERSION.SDK_INT > 22) {
            decorView.systemUiVisibility = if (Build.VERSION.SDK_INT > 27) {
                navigationBarColor = getColorCompat(

                    android.R.color.white)
                navigationBarDividerColor = getColorCompat(

                    R.color.white)
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                navigationBarColor = getColorCompat(

                    android.R.color.black)
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            statusBarColor = getColorCompat(statusColor)
        } else {
            statusBarColor = getColorCompat(android.R.color.black)
        }
    }
}

fun AppCompatActivity.clearLightStatusBar() {
    window?.apply {
        if (Build.VERSION.SDK_INT > 22) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = getColorCompat(android.R.color.transparent)
        } else {
            statusBarColor = getColorCompat(android.R.color.black)
        }
    }
}
