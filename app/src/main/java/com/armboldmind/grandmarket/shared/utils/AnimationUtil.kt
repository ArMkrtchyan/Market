package com.armboldmind.grandmarket.shared.utils

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation


object AnimationUtil {

    @JvmStatic
    fun alphaFrom0To1(view: View) {
        ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f)
            .apply {
                duration = 700
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
    }

    @JvmStatic
    fun rotate(view: View, toAngle: Float) {
        val imageViewObjectAnimator = ObjectAnimator.ofFloat(view, "rotation", toAngle)
        imageViewObjectAnimator.duration = 350
        imageViewObjectAnimator.start()
    }

    @JvmStatic
    fun scaleView(v: View, startScale: Float, endScale: Float): Animation? {
        val anim: Animation = ScaleAnimation(1f, 1f,  // Start and end values for the X axis scaling
                                             startScale, endScale,  // Start and end values for the Y axis scaling
                                             Animation.RELATIVE_TO_SELF, 0f,  // Pivot point of X scaling
                                             Animation.RELATIVE_TO_SELF, 0f) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 350
        v.startAnimation(anim)
        return v.animation
    }

    @JvmStatic
    fun Animation.onAnimationEnd(onAnimationEnd: () -> Unit) {
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                onAnimationEnd.invoke()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
    }
}