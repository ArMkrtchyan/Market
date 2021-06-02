package com.armboldmind.grandmarket.shared.globalextensions

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseFragment

object Spannable {

    @JvmStatic
    fun TextView.setSpannable(spannableString: SpannableString) {
        text = spannableString
        highlightColor = ContextCompat.getColor(context, R.color.white)
        movementMethod = LinkMovementMethod.getInstance()
    }

    @JvmStatic
    fun BaseFragment<*>.setClickableSpan(@ColorRes color: Int, context: Context, onClick: () -> Unit): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
                ds.color = ContextCompat.getColor(context, color)
            }
        }
    }
}