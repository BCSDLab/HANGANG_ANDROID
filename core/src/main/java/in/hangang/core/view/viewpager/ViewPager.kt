package `in`.hangang.core.view.viewpager

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class ViewPager(context: Context, attributeSet: AttributeSet) : ViewPager(context, attributeSet) {

    var pagingEnabled = true

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.ViewPager,
            0, 0
        ).apply {
            pagingEnabled = getBoolean(R.styleable.ViewPager_pagingEnabled, true)

            recycle()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (pagingEnabled)
            super.onInterceptTouchEvent(ev)
        else {
            if (ev != null && ev.actionMasked == MotionEvent.ACTION_MOVE) {

            } else {
                if (super.onInterceptTouchEvent(ev)) {
                    super.onTouchEvent(ev)
                }
            }
            return false
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (ev != null) {
            if (pagingEnabled) {
                super.onTouchEvent(ev)
            } else {
                ev.actionMasked != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev)
            }
        } else {
            super.onTouchEvent(ev)
        }
    }
}