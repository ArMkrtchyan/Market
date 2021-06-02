package com.armboldmind.grandmarket.shared.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StringRes
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.databinding.LayoutLoadingButtonBinding
import com.armboldmind.grandmarket.shared.globalextensions.gone
import com.armboldmind.grandmarket.shared.globalextensions.show

class LoadingButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val mBinding: LayoutLoadingButtonBinding by lazy { LayoutLoadingButtonBinding.inflate(LayoutInflater.from(context), this, false) }
    private var text: CharSequence = ""

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(mBinding.root, layoutParams)
        val incomingValues = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)
        text = incomingValues.getText(R.styleable.LoadingButton_text)
        mBinding.button.text = text
        invalidate()
        requestLayout()
        incomingValues.recycle()
    }

    private fun showLoading() {
        isEnabled = false
        mBinding.apply {
            progress.show()
            button.text = ""
        }
    }

    fun setText(@StringRes text: Int) {
        setText(context.getString(text))
    }

    fun setText(text: String) {
        mBinding.button.text = text
        this.text = text
    }

    private fun hideLoading() {
        isEnabled = true
        mBinding.apply {
            progress.gone()
            button.text = text
        }
    }

    fun setIsLoading(loading: Boolean) {
        if (loading) showLoading() else hideLoading()
    }
}