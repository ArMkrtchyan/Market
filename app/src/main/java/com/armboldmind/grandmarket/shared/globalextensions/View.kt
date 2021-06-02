package com.armboldmind.grandmarket.shared.globalextensions

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.FrameMetricsAggregator
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun View.expandHeight() {
    this.measure(View.MeasureSpec.makeMeasureSpec(this.rootView.width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(this.rootView.height, View.MeasureSpec.AT_MOST))
    val targetHeight = this.measuredHeight

    val heightAnimator = ValueAnimator.ofInt(0, targetHeight)
    heightAnimator.addUpdateListener { animation ->
        this.layoutParams.height = animation.animatedValue as Int
        this.requestLayout()
    }
    heightAnimator.duration = FrameMetricsAggregator.ANIMATION_DURATION.toLong()
    heightAnimator.start()
}

fun View.collapseHeight() {
    val initialHeight = this.measuredHeight
    val heightAnimator = ValueAnimator.ofInt(0, initialHeight)
    heightAnimator.addUpdateListener { animation ->
        val animatedValue = animation.animatedValue as Int
        this.layoutParams.height = initialHeight - animatedValue
        this.requestLayout()
    }
    heightAnimator.duration = FrameMetricsAggregator.ANIMATION_DURATION.toLong()
    heightAnimator.start()
}

fun View.collapseWidth() {
    val initialWidth = this.measuredWidth
    val widthAnimator = ValueAnimator.ofInt(0, initialWidth)
    widthAnimator.addUpdateListener { animation ->
        val animatedValue = animation.animatedValue as Int
        this.layoutParams.width = initialWidth - animatedValue
        this.requestLayout()
    }
    widthAnimator.duration = FrameMetricsAggregator.ANIMATION_DURATION.toLong()
    widthAnimator.start()
}

fun View.expandWidth(toWidth: Int) {
    this.measure(View.MeasureSpec.makeMeasureSpec(this.rootView.width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(this.rootView.height, View.MeasureSpec.EXACTLY))

    val widthAnimator = ValueAnimator.ofInt(0, toWidth)
    widthAnimator.addUpdateListener { animation ->
        this.layoutParams.width = animation.animatedValue as Int
        this.requestLayout()
    }
    widthAnimator.duration = FrameMetricsAggregator.ANIMATION_DURATION.toLong()
    widthAnimator.start()
}

fun View.getMeasure(): Pair<Int, Int> {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val width = measuredWidth
    val height = measuredHeight
    return width to height
}

fun View.animateVertically(
    target: Float,
    duration: Long = FrameMetricsAggregator.ANIMATION_DURATION.toLong(),
    endAction: (() -> Unit)? = null,
) {
    this.animate()
            .y(target)
            .setDuration(duration)
            .withEndAction {
                endAction?.invoke()
            }
            .start()
}

fun View.animateHorizontally(
    target: Float,
    duration: Long = FrameMetricsAggregator.ANIMATION_DURATION.toLong(),
    endAction: (() -> Unit)? = null,
) {
    this.animate()
            .x(target)
            .setDuration(duration)
            .withEndAction {
                endAction?.invoke()
            }
            .start()
}

fun View.animateAlpha(
    target: Float,
    duration: Long = FrameMetricsAggregator.ANIMATION_DURATION.toLong(),
    startAction: (View.() -> Unit)? = null,
    endAction: (View.() -> Unit)? = null,
) {
    startAction?.invoke(this)
    animate().alpha(target)
            .setDuration(duration)
            .withEndAction {
                endAction?.invoke(this)
            }
            .start()
}


fun View.setKeyboardListeners(vararg editText: EditText) {
    viewTreeObserver.addOnGlobalLayoutListener {
        val r = Rect()
        getWindowVisibleDisplayFrame(r)
        val screenHeight = rootView.height
        val keypadHeight = screenHeight - r.bottom
        editText.forEach {
            it.isCursorVisible = keypadHeight > screenHeight * 0.15
        }
    }
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
    setOnClickListener(null)
}

fun View.show() {
    visibility = View.VISIBLE
}

@SuppressLint("SetTextI18n")
fun TextView.startTimer(lifecycleScope: CoroutineScope, keys: Keys, onTimerFinish: () -> Unit) {
    lifecycleScope.launch {
        show()
        setTextColor(ContextCompat.getColor(context, R.color.black))
        setOnClickListener(null)
        requestLayout()
        for (seconds in 59 downTo 1) {
            text = keys.resend_in + if (seconds > 9) " 00:$seconds"
            else " 00:0$seconds"
            delay(1000)
        }
        gone()
        onTimerFinish.invoke()
    }
}

fun View.toTransitionGroup() = this to transitionName

fun CardView.setDimensionRatio(ratio: String) {
    updateLayoutParams<ConstraintLayout.LayoutParams> { dimensionRatio = ratio }
}

fun ImageView.load(url: String, shimmer: ShimmerFrameLayout? = null, onResourceReady: (() -> Unit)? = null) {
    Glide.with(context)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    shimmer?.stopShimmer()
                    shimmer?.gone()
                    setImageDrawable(context.getDrawableCompat(R.drawable.default_error_image))
                    return true
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    shimmer?.stopShimmer()
                    shimmer?.gone()
                    setImageDrawable(resource)
                    onResourceReady?.invoke()
                    return false
                }
            })
            .into(this)
}

fun RangeSlider.onStopTrackingTouch(values: (from: Long, to: Long) -> Unit) {
    addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: RangeSlider) {}

        override fun onStopTrackingTouch(slider: RangeSlider) {
            values.invoke(slider.values[0].toLong(), slider.values[1].toLong())
        }

    })
}

fun BottomSheetBehavior<*>.onSlide(alpha: (alpha: Float) -> Unit) {
    addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            val offset = 1 - slideOffset
            alpha.invoke(if (offset > 0.3) offset else 0.3f)
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
        }
    })
}

fun BottomSheetBehavior<*>.onState(alpha: (state: Int) -> Unit) {
    addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            alpha.invoke(newState)
        }
    })
}