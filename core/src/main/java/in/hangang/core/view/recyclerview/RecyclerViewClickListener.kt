package `in`.hangang.core.view.recyclerview

import android.view.View

interface RecyclerViewClickListener {
    fun onClick(view: View, position: Int, item: Any)
}
