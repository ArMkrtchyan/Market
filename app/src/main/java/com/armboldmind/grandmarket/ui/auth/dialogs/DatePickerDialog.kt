package com.armboldmind.grandmarket.ui.auth.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.armboldmind.grandmarket.base.BaseBottomSheetDialogFragment
import com.armboldmind.grandmarket.databinding.BottomSheetDatePickerDialogBinding
import com.armboldmind.grandmarket.shared.enums.DatePatternsEnum
import com.armboldmind.grandmarket.shared.formatters.Formatter
import com.armboldmind.grandmarket.shared.formatters.IFormatter
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class DatePickerDialog(private val date: String?, private val block: (Date) -> Unit) : BaseBottomSheetDialogFragment() {

    private val mFormatter: IFormatter by lazy { Formatter() }
    private lateinit var mBinding: BottomSheetDatePickerDialogBinding
    private lateinit var mDate: Date

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return BottomSheetDatePickerDialogBinding.inflate(inflater, container, false)
            .apply {
                keysLiveData().observe(viewLifecycleOwner) {
                    lifecycleOwner = viewLifecycleOwner
                    keys = it
                    mBinding = this
                    date?.let {
                        datePicker.setCurrentDate(mFormatter.formatPatternToDate(it, DatePatternsEnum.DAY_MONTH_YEAR) ?: Date())
                        mDate = mFormatter.formatPatternToDate(it, DatePatternsEnum.DAY_MONTH_YEAR) ?: Calendar.getInstance()
                            .apply {
                                set(Calendar.YEAR,
                                    Calendar.getInstance()
                                        .get(Calendar.YEAR) - 18)
                            }.time
                    }
                    datePicker.addOnDateChangeListener { year, month, day ->
                        mDate = Calendar.getInstance()
                            .apply {
                                set(Calendar.YEAR, year)
                                set(Calendar.MONTH, month - 1)
                                set(Calendar.DAY_OF_MONTH, day)
                            }.time
                    }
                    save.setOnClickListener {
                        if (::mDate.isInitialized) {
                            block.invoke(mDate)
                            dismiss()
                        }
                    }
                }
            }.root
    }

}