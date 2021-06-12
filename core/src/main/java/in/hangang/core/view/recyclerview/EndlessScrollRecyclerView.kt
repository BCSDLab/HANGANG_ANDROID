package `in`.hangang.core.view.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class EndlessScrollRecyclerView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RecyclerViewWithEmpty(context, attributeSet, defStyleAttr) {

    interface EndlessScrollCallback {
        fun onScrollEnd()
    }

    //이 값이 true 일때만 무한 스크롤 기능 작동
    var endlessScrollable = true

    var endlessScrollCallback: EndlessScrollCallback? = null

    init {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == SCROLL_STATE_IDLE) {
                    if (endlessScrollable) endlessScrollCallback?.onScrollEnd()
                }
            }
        })
    }

}