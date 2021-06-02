package com.armboldmind.grandmarket.shared

import android.graphics.drawable.Drawable
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import com.armboldmind.grandmarket.shared.formatters.Formatter
import com.bumptech.glide.Glide
import java.util.*

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("imageCycle", "errorCycle")
    fun imageCycle(view: ImageView, url: String?, errorImage: Drawable?) {
        url?.let {
            Glide.with(view.context)
                    .load(url)
                    .circleCrop()
                    .error(errorImage)
                    .placeholder(R.drawable.ic_profile)
                    .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("image", "error")
    fun image(view: ImageView, url: String?, errorImage: Drawable?) {
        url?.let {
            Glide.with(view.context)
                    .load(url)
                    .error(errorImage)
                    .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("dateOfBirth")
    fun dob(editText: EditText, date: Long?) {
        date?.let { editText.setText(Formatter().formatToPattern(Date(it), DatePatternsEnum.DAY_MONTH_YEAR)) }
    }

    @JvmStatic
    @BindingAdapter("createdDate")
    fun createdDate(editText: TextView, date: Long?) {
        date?.let { editText.text = Formatter().formatToPattern(Date(it), DatePatternsEnum.DAY_MONTH_YEAR) }
    }

    @JvmStatic
    @BindingAdapter("requestCreatedDate")
    fun requestCreatedDate(editText: TextView, date: Long?) {
        date?.let { editText.text = Formatter().formatToPattern(Date(it), DatePatternsEnum.DAY_MONTH_YEAR_TIME_PATTERN2) }
    }

    @JvmStatic
    @BindingAdapter("notificationDate")
    fun notificationDate(editText: TextView, date: Long?) {
        date?.let { editText.text = Formatter().formatToPattern(Date(it), DatePatternsEnum.DAY_MONTH_YEAR_TIME_PATTERN2) }
    }

    @JvmStatic
    @BindingAdapter("setHtml")
    fun setHtml(editText: TextView, data: String?) {
        data?.let { editText.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    }
}