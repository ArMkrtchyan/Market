package com.armboldmind.grandmarket.shared.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.DrawableRes
import com.armboldmind.grandmarket.databinding.DialogFactoryBinding
import com.armboldmind.grandmarket.shared.globalextensions.gone

class DialogFactory(
    context: Context,
    @DrawableRes private var mDrawableImage: Int?,
    private val mTitle: String?,
    private val mDescription: String?,
    private val mNegativeButtonText: String?,
    private val mPositiveButtonText: String?,
    private val mCancelable: Boolean = true,
    private var mOnNegativeButtonClick: (() -> Unit)?,
    private var mOnPositiveButtonClick: (() -> Unit)?,
) : Dialog(context) {

    private lateinit var mBinding: DialogFactoryBinding

    private constructor(builder: Builder) : this(builder.context,
                                                 builder.mDrawableImage,
                                                 builder.mTitle,
                                                 builder.mDescription,
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
        var mDrawableImage: Int? = null
        var mTitle: String? = null
        var mDescription: String? = null
        var mCancelable: Boolean = true
        var mNegativeButtonText: String? = null
        var mPositiveButtonText: String? = null
        var mOnNegativeButtonClick: (() -> Unit)? = null
        var mOnPositiveButtonClick: (() -> Unit)? = null

        fun imageRes(image: Int?) = apply { this.mDrawableImage = image }

        fun title(title: String?) = apply { this.mTitle = title }

        fun description(description: String?) = apply { this.mDescription = description }

        fun negativeButtonText(negativeButtonText: String) = apply { this.mNegativeButtonText = negativeButtonText }

        fun positiveButtonText(positiveButtonText: String) = apply { this.mPositiveButtonText = positiveButtonText }

        fun setCancelable(isCancelable: Boolean) = apply { this.mCancelable = isCancelable }

        fun negativeButtonClick(onNegativeButtonClick: (() -> Unit)?) = apply { this.mOnNegativeButtonClick = onNegativeButtonClick }

        fun positiveButtonClick(onPositiveButtonClick: (() -> Unit)?) = apply { this.mOnPositiveButtonClick = onPositiveButtonClick }

        fun build(): DialogFactory {
            val v = DialogFactory(this)
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
        mBinding = DialogFactoryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        mBinding.apply {
            mDrawableImage?.let { image.setImageResource(it) } ?: run { image.gone() }
            if (mTitle.isNullOrEmpty()) title.gone() else title.text = mTitle
            if (mDescription.isNullOrEmpty()) description.gone() else description.text = mDescription
            if (mPositiveButtonText.isNullOrEmpty()) positive.gone() else positive.text = mPositiveButtonText
            if (mNegativeButtonText.isNullOrEmpty()) negative.gone() else negative.text = mNegativeButtonText
            setCancelable(mCancelable)
            negative.setOnClickListener {
                dismiss()
                mOnNegativeButtonClick?.let { it1 -> it1() }
            }
            positive.setOnClickListener {
                dismiss()
                mOnPositiveButtonClick?.let { it1 -> it1() }
            }
        }
    }
}