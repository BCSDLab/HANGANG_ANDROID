package `in`.hangang.core.view.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewWithEmpty(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttr) {
    var emptyView: View? = null

    private val emptyObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            if (adapter != null && emptyView != null) {
                if (adapter!!.itemCount == 0) {
                    emptyView?.visibility = View.VISIBLE
                    this@RecyclerViewWithEmpty.visibility = View.GONE
                } else {
                    emptyView?.visibility = View.GONE
                    this@RecyclerViewWithEmpty.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()
    }
}
