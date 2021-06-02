package com.armboldmind.grandmarket.shared.customview

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.base.BaseAdapter
import com.armboldmind.grandmarket.base.BaseViewHolder
import com.armboldmind.grandmarket.data.models.getDiffCallback
import com.armboldmind.grandmarket.databinding.AdapterOptionItemBinding
import com.armboldmind.grandmarket.databinding.LayoutDropDownSelectorBinding
import com.armboldmind.grandmarket.shared.globalextensions.*
import com.armboldmind.grandmarket.shared.utils.AnimationUtil
import com.armboldmind.gsport24.root.utils.DifItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DropDownSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val mBinding: LayoutDropDownSelectorBinding = LayoutDropDownSelectorBinding.inflate(context.inflater(), this, false)
    private val mAdapter by lazy { OptionAdapter() }
    private var selectedItemPosition = 0f
    private var selectedItem: Option? = null

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(mBinding.root, layoutParams)
        mBinding.apply {
            optionsList.adapter = mAdapter
        }
    }

    fun initData(list: List<Option>, hint: String) {
        mAdapter.submitList(list.toMutableList())
        selectedItem = list[0]
        mBinding.selectedOption.text = list[0].title
        mBinding.selectedOptionHint.text = hint
        initListener()
        invalidate()
    }

    private fun initListener() {
        mBinding.apply {
            selectedOption.setOnClickListener { if (!selectedOptionHint.isVisible) startAnimation() else startBackAnimation() }
            selectedOptionHint.setOnClickListener { startBackAnimation() }
            arrow.setOnClickListener { selectedOption.callOnClick() }
        }
    }

    private fun startAnimation() {
        mBinding.apply {
            layoutFull.show()
            val scale = ScaleAnimation(1f, 1f, 0.2f, 1f)
            scale.duration = 300
            scale.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    isEnabled = false
                    layout.invisible()
                    selectedOptionHint.show()
                    startRecyclerAnimation()
                    if (selectedItemPosition == 0f) selectedItemPosition = mBinding.optionsList.y - 6.dpToPx()
                    startSelectedOptionAnimation()
                    AnimationUtil.rotate(arrow, 90f)
                }

                override fun onAnimationEnd(animation: Animation?) {
                    isEnabled = true
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }

            })
            layoutFull.startAnimation(scale)
        }
    }

    private fun startBackAnimation() {
        CoroutineScope(Dispatchers.Main).launch {
            mBinding.apply {
                val scale = ScaleAnimation(1f, 1f, 1f, 0.2f)
                scale.duration = 300
                scale.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        isEnabled = false
                        startRecyclerBackAnimation()
                        startSelectedOptionBackAnimation()
                        AnimationUtil.rotate(arrow, -90f)
                        selectedOptionHint.invisible()
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        isEnabled = true
                        layoutFull.invisible()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                })
                layoutFull.startAnimation(scale)
                delay(200)
                layout.show()
            }
        }
    }

    fun startRecyclerAnimation() {
        mBinding.apply {
            optionsList.show()
            val scale = ScaleAnimation(1f, 1f, 0f, 1f)
            scale.duration = 300
            optionsList.startAnimation(scale)
        }
    }

    fun startRecyclerBackAnimation() {
        mBinding.apply {
            optionsList.invisible()
            val scale = ScaleAnimation(1f, 1f, 1f, 0f)
            scale.duration = 300
            mAdapter.notifyDataSetChanged()
            optionsList.startAnimation(scale)
        }
    }

    fun startSelectedOptionAnimation() {
        mBinding.apply {
            ObjectAnimator.ofFloat(selectedOption, "translationY", selectedOptionHint.y, selectedItemPosition)
                .apply {
                    duration = 300
                    start()
                }
        }
    }

    fun startSelectedOptionBackAnimation() {
        mBinding.apply {
            ObjectAnimator.ofFloat(selectedOption, "translationY", selectedItemPosition, selectedOptionHint.y)
                .apply {
                    duration = 300
                    start()
                }
        }
    }

    fun getSelectedItem() {

    }

    inner class OptionAdapter : BaseAdapter<Option, OptionAdapter.ViewHolder>(getDiffCallback()) {
        inner class ViewHolder(val binding: AdapterOptionItemBinding) : BaseViewHolder(binding)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(AdapterOptionItemBinding.inflate(parent.inflater(), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.apply {
                if (selectedItem?.title == getItem(position)?.title) root.text = "  " else root.text = getItem(position).title
                root.setOnClickListener {
                    selectedItemPosition = root.y + context.getDimensions(R.dimen.button_height) - 6.dpToPx()
                    selectedItem = getItem(position)
                    mBinding.selectedOption.text = getItem(position).title
                    startBackAnimation()
                }
            }
        }
    }

    data class Option(val title: String) : DifItem<Option> {
        override fun areItemsTheSame(second: Option): Boolean {
            return title == second.title
        }

        override fun areContentsTheSame(second: Option): Boolean {
            return title == second.title
        }

    }
}