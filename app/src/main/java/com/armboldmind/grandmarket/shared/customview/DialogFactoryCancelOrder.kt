package com.armboldmind.grandmarket.shared.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.isVisible
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.databinding.DialogFactoryCancelOrderBinding
import com.armboldmind.grandmarket.shared.utils.AnimationUtil
import com.armboldmind.grandmarket.shared.utils.AnimationUtil.onAnimationEnd

class DialogFactoryCancelOrder(
    context: Context,
    private var mOnPositiveButtonClick: (() -> Unit)?,
) : Dialog(context) {

    private lateinit var mBinding: DialogFactoryCancelOrderBinding

    private constructor(builder: Builder) : this(builder.context, builder.mOnPositiveButtonClick)

    companion object {
        inline fun build(required: Context, block: Builder.() -> Unit) = Builder(required).apply(block)
            .build()
    }

    class Builder(c: Context) {
        var context: Context = c
        var mOnPositiveButtonClick: (() -> Unit)? = null

        fun positiveButtonClick(onPositiveButtonClick: (() -> Unit)?) = apply { this.mOnPositiveButtonClick = onPositiveButtonClick }

        fun build(): DialogFactoryCancelOrder {
            val v = DialogFactoryCancelOrder(this)
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
        mBinding = DialogFactoryCancelOrderBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mBinding.apply {
            selectedReason.setOnClickListener {
                if (!reasonLayout.isVisible) {
                    reasonLayout.isVisible = true
                    AnimationUtil.rotate(arrow, 90f)
                    AnimationUtil.scaleView(reasonLayout, 0f, 1f)
                } else {
                    AnimationUtil.rotate(arrow, -90f)
                    AnimationUtil.scaleView(reasonLayout, 1f, 0f)
                        ?.onAnimationEnd { reasonLayout.isVisible = false }
                }
            }
            arrow.setOnClickListener { selectedReason.callOnClick() }
            notLongerNeeded.setOnClickListener {
                selectedReason.text = root.context.getString(R.string.the_purchase_is_not_longer_needed)
                AnimationUtil.rotate(arrow, -90f)
                AnimationUtil.scaleView(reasonLayout, 1f, 0f)
                    ?.onAnimationEnd { reasonLayout.isVisible = false }
            }
            other.setOnClickListener {
                selectedReason.text = root.context.getString(R.string.other)
                AnimationUtil.rotate(arrow, -90f)
                AnimationUtil.scaleView(reasonLayout, 1f, 0f)
                    ?.onAnimationEnd { reasonLayout.isVisible = false }
            }
            cancelledToReorder.setOnClickListener {
                selectedReason.text = root.context.getString(R.string.cancelled_to_reorder)
                AnimationUtil.rotate(arrow, -90f)
                AnimationUtil.scaleView(reasonLayout, 1f, 0f)
                    ?.onAnimationEnd { reasonLayout.isVisible = false }
            }
            negative.setOnClickListener {
                dismiss()
            }
            positive.setOnClickListener {
                dismiss()
                mOnPositiveButtonClick?.let { it1 -> it1() }
            }
        }
    }
}