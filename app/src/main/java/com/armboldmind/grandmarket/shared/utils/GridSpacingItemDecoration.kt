package com.armboldmind.grandmarket.shared.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position >= 0) outRect.apply {
            val column = position % spanCount
            left = column * spacing / spanCount
            right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) top = spacing
        } else outRect.apply { left = 0; right = 0; top = 0; bottom = 0 }
    }
}