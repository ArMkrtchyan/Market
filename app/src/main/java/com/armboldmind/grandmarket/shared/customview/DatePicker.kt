package com.armboldmind.grandmarket.shared.customview

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.databinding.LayoutDatePickerBinding
import com.armboldmind.grandmarket.shared.globalextensions.dpToPx
import com.armboldmind.grandmarket.shared.globalextensions.inflater
import java.util.*
import java.util.Calendar

class DatePicker @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val mBinding: LayoutDatePickerBinding = LayoutDatePickerBinding.inflate(context.inflater(), this, false)
    private var mYears: ArrayList<String> = arrayListOf()
    private var mMonths = listOf(context.getString(R.string.january),
                                 context.getString(R.string.february),
                                 context.getString(R.string.march),
                                 context.getString(R.string.april),
                                 context.getString(R.string.may),
                                 context.getString(R.string.june),
                                 context.getString(R.string.july),
                                 context.getString(R.string.august),
                                 context.getString(R.string.september),
                                 context.getString(R.string.october),
                                 context.getString(R.string.november),
                                 context.getString(R.string.december))

    private var mDays: ArrayList<String> = arrayListOf()
    private val mCalendar = Calendar.getInstance()
    private var currentDate: Date? = null

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(mBinding.root, layoutParams)
        for (i in 1..31) {
            mDays.add(i.toString())
        }
        for (i in mCalendar.get(Calendar.YEAR) - 120..mCalendar.get(Calendar.YEAR) - 18) {
            mYears.add(i.toString())
        }
        mBinding.apply {
            years.apply {
                displayedValues = mYears.reversed()
                    .toTypedArray()
                value = mYears.size - 1
                minValue = 0
                maxValue = mYears.size - 1
            }
            months.apply {
                displayedValues = mMonths.toTypedArray()
                value = 0
                minValue = 0
                maxValue = mMonths.size - 1
            }
            days.apply {
                displayedValues = mDays.toTypedArray()
                value = 0
                minValue = 0
                maxValue = mDays.size - 1
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                years.selectionDividerHeight = 0.5.dpToPx()
                months.selectionDividerHeight = 0.5.dpToPx()
                days.selectionDividerHeight = 0.5.dpToPx()
            } else {
                years.setDividerHeight(0.5.dpToPx())
                months.setDividerHeight(0.5.dpToPx())
                days.setDividerHeight(0.5.dpToPx())
            }
        }
        requestLayout()
    }

    fun addOnDateChangeListener(onDateChange: (year: Int, month: Int, day: Int) -> Unit) {
        mBinding.apply {
            years.setOnValueChangedListener { _, _, newVal ->
                currentDate?.let {
                    val cal = Calendar.getInstance()
                        .apply { time = it }
                    if (cal.get(Calendar.YEAR) == newVal) years.setNumberPickerTextColor(ContextCompat.getColor(context, R.color.black)) else years.setNumberPickerTextColor(
                        ContextCompat.getColor(context, R.color.black))
                }
                if (newVal == 0) {
                    if (months.value > mCalendar.get(Calendar.MONTH)) {
                        months.value = mCalendar.get(Calendar.MONTH)
                        days.value = mCalendar.get(Calendar.DAY_OF_MONTH) - 1
                    } else if (months.value == mCalendar.get(Calendar.MONTH) && days.value > mCalendar.get(Calendar.DAY_OF_MONTH)) {
                        days.value = mCalendar.get(Calendar.DAY_OF_MONTH)
                    }
                }
                Calendar.getInstance()
                    .apply {
                        set(Calendar.YEAR, mYears.reversed()[newVal].toInt())
                        set(Calendar.MONTH, months.value)
                        val maxDayPosition = getActualMaximum(Calendar.DAY_OF_MONTH)
                        if (mDays[days.value].toInt() > maxDayPosition - 1) {
                            days.value = maxDayPosition - 1
                        }
                    }
                onDateChange(mYears.reversed()[newVal].toInt(), months.value + 1, mDays[days.value].toInt())
            }
            months.setOnValueChangedListener { _, _, newVal ->
                currentDate?.let {
                    val cal = Calendar.getInstance()
                        .apply { time = it }
                    if (cal.get(Calendar.MONTH) - 1 == newVal) months.setNumberPickerTextColor(ContextCompat.getColor(context, R.color.black)) else months.setNumberPickerTextColor(
                        ContextCompat.getColor(context, R.color.black))
                }
                if (years.value == 0 && newVal > mCalendar.get(Calendar.MONTH)) {
                    months.value = mCalendar.get(Calendar.MONTH)
                }
                Calendar.getInstance()
                    .apply {
                        set(Calendar.YEAR, mYears.reversed()[years.value].toInt())
                        set(Calendar.MONTH, newVal)
                        val maxDayPosition = getActualMaximum(Calendar.DAY_OF_MONTH)
                        if (mDays[days.value].toInt() > maxDayPosition - 1) {
                            days.value = maxDayPosition - 1
                        }
                    }
                onDateChange(mYears.reversed()[years.value].toInt(), newVal + 1, mDays[days.value].toInt())
            }
            days.setOnValueChangedListener { _, _, newVal ->
                currentDate?.let {
                    val cal = Calendar.getInstance()
                        .apply { time = it }
                    if (cal.get(Calendar.DAY_OF_MONTH) - 1 == newVal) days.setNumberPickerTextColor(ContextCompat.getColor(context,
                                                                                                                           R.color.black)) else days.setNumberPickerTextColor(
                        ContextCompat.getColor(context, R.color.black))
                }
                if (years.value == 0) {
                    if (months.value == mCalendar.get(Calendar.MONTH) && newVal > mCalendar.get(Calendar.DAY_OF_MONTH) - 1) days.value = mCalendar.get(Calendar.DAY_OF_MONTH) - 1
                }
                Calendar.getInstance()
                    .apply {
                        set(Calendar.YEAR, mYears.reversed()[years.value].toInt())
                        set(Calendar.MONTH, months.value)
                        val maxDayPosition = getActualMaximum(Calendar.DAY_OF_MONTH)
                        if (mDays[days.value].toInt() > maxDayPosition - 1) {
                            days.value = maxDayPosition - 1
                        }
                    }
                onDateChange(mYears.reversed()[years.value].toInt(), months.value + 1, mDays[newVal].toInt())
            }
        }
    }

    fun setCurrentDate(date: Date?) {
        currentDate = date
        date?.let {
            val calendar = Calendar.getInstance()
                .apply { time = date }
            mBinding.apply {
                months.setNumberPickerTextColor(ContextCompat.getColor(context, R.color.black))
                days.setNumberPickerTextColor(ContextCompat.getColor(context, R.color.black))
                years.setNumberPickerTextColor(ContextCompat.getColor(context, R.color.black))
                months.value = calendar.get(Calendar.MONTH)
                days.value = calendar.get(Calendar.DAY_OF_MONTH) - 1
                mYears.reversed()
                    .mapIndexed { index, s ->
                        if (calendar.get(Calendar.YEAR) == s.toInt()) {
                            years.value = index
                            return@apply
                        }
                    }
            }
        }
        requestLayout()
    }

    private fun NumberPicker.setDividerHeight(height: Int) {
        val pickerFields = NumberPicker::class.java.declaredFields
        for (pf in pickerFields) {
            if (pf.name == "mSelectionDividerHeight") {
                pf.isAccessible = true
                try { // set divider height in pixels
                    pf.set(this, height)
                } catch (e: java.lang.IllegalArgumentException) { // log exception here
                } catch (e: Resources.NotFoundException) { // log exception here
                } catch (e: IllegalAccessException) { // log exception here
                }
                break
            }
        }
    }

    private fun NumberPicker.setNumberPickerTextColor(color: Int) {
        try {
            val selectorWheelPaintField = this.javaClass.getDeclaredField("mSelectorWheelPaint")
            selectorWheelPaintField.isAccessible = true
            (selectorWheelPaintField.get(this) as Paint).color = color
        } catch (e: NoSuchFieldException) {
            Log.w("setNumber", e)
        } catch (e: IllegalAccessException) {
            Log.w("setNumber", e)
        } catch (e: IllegalArgumentException) {
            Log.w("setNumber", e)
        }
        val count = this.childCount
        for (i in 0 until count) {
            val child: View = this.getChildAt(i)
            if (child is EditText) child.setTextColor(color)
        }
        this.invalidate()
    }
}