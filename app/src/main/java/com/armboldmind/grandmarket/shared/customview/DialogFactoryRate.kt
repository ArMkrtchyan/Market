package com.armboldmind.grandmarket.shared.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import com.armboldmind.grandmarket.databinding.DialogFactoryRateBinding
import com.armboldmind.grandmarket.shared.globalextensions.gone
import com.armboldmind.grandmarket.shared.validations.FullNameValidator

class DialogFactoryRate(
    context: Context,
    private val mTitle: String?,
    private val mNegativeButtonText: String?,
    private val mPositiveButtonText: String?,
    private val mCancelable: Boolean = true,
    private var mOnNegativeButtonClick: (() -> Unit)?,
    private var mOnPositiveButtonClick: ((rating: Int) -> Unit)?,
) : Dialog(context) {
    private val mValidator by lazy { FullNameValidator() }
    private lateinit var mBinding: DialogFactoryRateBinding

    private constructor(builder: Builder) : this(builder.context,
                                                 builder.mTitle,
                                                 builder.mNegativeButtonText,
                                                 builder.mPositiveButtonText,
                                                 builder.mCancelable,
                                                 builder.mOnNegativeButtonClick,
                                                 builder.mOnPositiveButtonClick)

    companion object {
        inline fun build(required: Context, block: Builder.() -> Unit) = Builder(required).apply(block)
            .build()
    }

    class Builder(c: Context) {
        var context: Context = c
        var mTitle: String? = null
        var mCancelable: Boolean = true
        var mNegativeButtonText: String? = null
        var mPositiveButtonText: String? = null
        var mOnNegativeButtonClick: (() -> Unit)? = null
        var mOnPositiveButtonClick: ((rating: Int) -> Unit)? = null


        fun title(title: String?) = apply { this.mTitle = title }

        fun negativeButtonText(negativeButtonText: String) = apply { this.mNegativeButtonText = negativeButtonText }

        fun positiveButtonText(positiveButtonText: String) = apply { this.mPositiveButtonText = positiveButtonText }

        fun setCancelable(isCancelable: Boolean) = apply { this.mCancelable = isCancelable }

        fun negativeButtonClick(onNegativeButtonClick: (() -> Unit)?) = apply { this.mOnNegativeButtonClick = onNegativeButtonClick }

        fun positiveButtonClick(onPositiveButtonClick: ((rating: Int) -> Unit)?) = apply { this.mOnPositiveButtonClick = onPositiveButtonClick }

        fun build(): DialogFactoryRate {
            val v = DialogFactoryRate(this)
            v.show()
            return v
        }
    }

    init {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.requestFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogFactoryRateBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        mBinding.apply {
            if (mTitle.isNullOrEmpty()) title.gone() else title.text = mTitle
            if (mPositiveButtonText.isNullOrEmpty()) positive.gone() else positive.text = mPositiveButtonText
            if (mNegativeButtonText.isNullOrEmpty()) negative.gone() else negative.text = mNegativeButtonText
            setCancelable(mCancelable)
            negative.setOnClickListener {
                dismiss()
                mOnNegativeButtonClick?.let { it1 -> it1() }
            }
            positive.setOnClickListener {
                if (mValidator.isValid(message.text)) {
                    dismiss()
                    mOnPositiveButtonClick?.let { it1 -> it1(rating.numStars) }
                } else mValidator.showError(message)
            }
        }
    }
}