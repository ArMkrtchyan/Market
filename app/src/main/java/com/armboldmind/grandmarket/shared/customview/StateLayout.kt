package com.armboldmind.grandmarket.shared.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.data.network.BaseDataSource
import com.armboldmind.grandmarket.databinding.LayoutEmptyViewBinding
import com.armboldmind.grandmarket.databinding.LayoutLoadingBinding
import com.armboldmind.grandmarket.shared.globalextensions.getColorCompat
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.armboldmind.grandmarket.shared.globalextensions.show
import com.armboldmind.grandmarket.shared.mvi.states.ViewState
import com.google.android.material.snackbar.Snackbar
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class StateLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val mLoadingBinding: LayoutLoadingBinding by lazy { LayoutLoadingBinding.inflate(context.inflater(), this, false) }
    private val mEmptyBinding: LayoutEmptyViewBinding by lazy { LayoutEmptyViewBinding.inflate(context.inflater(), this, false) }

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val incomingValues = context.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        incomingValues.recycle()
        requestLayout()
    }

    fun setViewState(viewState: ViewState) {
        removeAllViews()
        when (viewState) {
            is ViewState.LoadingViewState -> {
                addView(mLoadingBinding.root, layoutParams)
            }
            is ViewState.EmptyState -> {
                addView(mEmptyBinding.root, layoutParams)
            }
            is ViewState.ErrorState -> {
                addView(mEmptyBinding.root, layoutParams)
                if (viewState.exception is SocketTimeoutException || viewState.exception is UnknownHostException) setEmpty(EmptyModel(R.drawable.ic_network_error,
                                                                                                                                      keysLiveData().value?.network_error ?: "Null",
                                                                                                                                      keysLiveData().value?.network_error
                                                                                                                                          ?: "Null"))
                else if (viewState.exception is BaseDataSource.SuccessException) showSnackBar(viewState.exception.localizedMessage ?: keysLiveData().value?.default_error_message
                ?: "Null")
                else setEmpty(EmptyModel(R.drawable.ic_server_error, keysLiveData().value?.server_error ?: "Null", keysLiveData().value?.default_error_message ?: "Null"))

                Log.i("ExceptionTAg", viewState.exception.localizedMessage ?: "")
            }
            else -> {
            }
        }
    }

    fun showSnackBar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
            .setTextColor(context.getColorCompat(android.R.color.white))
            .setBackgroundTint(context.getColorCompat(android.R.color.black))
            .show()
    }


    fun setEmpty(emptyModel: EmptyModel?) {
        emptyModel?.let { model ->
            model.emptyDescription?.let { mEmptyBinding.subtitle.text = it;mEmptyBinding.subtitle.show() }
            model.emptyTitle?.let { mEmptyBinding.title.text = it;mEmptyBinding.title.show() }
            model.imageRes?.let { mEmptyBinding.viewImage.setImageResource(it);mEmptyBinding.viewImage.show() }
        }
    }

    fun setOnButtonClick(buttonTitle: String, onButtonClick: () -> Unit) {
        mEmptyBinding.apply {
            btn.text = buttonTitle
            btn.setOnClickListener { onButtonClick.invoke() }
            btn.show()
        }
    }

    data class EmptyModel(
        @DrawableRes val imageRes: Int? = null,
        var emptyTitle: String? = null,
        val emptyDescription: String? = null,
    )
}