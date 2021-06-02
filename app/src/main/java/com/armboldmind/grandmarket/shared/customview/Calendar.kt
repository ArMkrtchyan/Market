package com.armboldmind.grandmarket.shared.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.databinding.AdapterCalendarItemBinding
import com.armboldmind.grandmarket.databinding.AdapterCalendarPagerItemBinding
import com.armboldmind.grandmarket.databinding.LayoutCalendarBinding
import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import com.armboldmind.grandmarket.shared.formatters.Formatter
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import java.util.Calendar

class Calendar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val mBinding: LayoutCalendarBinding by lazy { LayoutCalendarBinding.inflate(LayoutInflater.from(context), this, false) }
    private val mCalendarMin = Calendar.getInstance()
        .apply { set(1921, 0, 1) }
    private val mCalendar = Calendar.getInstance()
    private val mCalendarMax = Calendar.getInstance()
        .apply { set(2050, 11, 31) }


    init {

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(mBinding.root, layoutParams)
        mBinding.title.text = Formatter().formatToPattern(mCalendar.time, DatePatternsEnum.MOUNT_YEAR_PATTERN)
        mBinding.calendarPager.adapter = PagerAdapter()
        val incomingValues = context.obtainStyledAttributes(attrs, R.styleable.Calendar)
        invalidate()
        requestLayout()
        incomingValues.recycle()
    }

    inner class DaysAdapter() : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {
        inner class ViewHolder(val binding: AdapterCalendarItemBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(AdapterCalendarItemBinding.inflate(parent.context.inflater(), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        }

        override fun getItemCount() = 42
    }

    inner class PagerAdapter() : RecyclerView.Adapter<PagerAdapter.ViewHolder>() {
        inner class ViewHolder(val binding: AdapterCalendarPagerItemBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(AdapterCalendarPagerItemBinding.inflate(parent.context.inflater(), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.list.adapter = DaysAdapter()
        }

        override fun getItemCount() = 42
    }
}