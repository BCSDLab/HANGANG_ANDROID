package `in`.hangang.core.view.appbar

import `in`.hangang.core.R
import `in`.hangang.core.view.appbar.interfaces.OnAppBarButtonClickListener
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.contains
import androidx.fragment.app.Fragment

open class BaseAppBar @JvmOverloads constructor(
        context: Context,
        private val attributeSet: AttributeSet? = null,
        private val defStyleAttr: Int = 0,
        private val defStyleRes: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    //Inflate Layout and views in layout
    private val view: View = LayoutInflater.from(context).inflate(R.layout.layout_app_bar_base, this, true)
    private val container = view.findViewById<ConstraintLayout>(R.id.app_bar_container)
    protected val dividerContainer = view.findViewById<FrameLayout>(R.id.app_bar_divider_container)
    private val textViewTitle = view.findViewById<TextView>(R.id.app_bar_title)
    private val backButton = view.findViewById<ImageButton>(R.id.app_bar_back_button).apply {
        setOnClickListener {
            if (backButtonOnClickListener == null) {
                (context as? Activity)?.onBackPressed()
            } else
                backButtonOnClickListener!!.onClick(it)
        }
    }
    private val leftContainer = view.findViewById<LinearLayout>(R.id.app_bar_container_left)
    private val rightContainer = view.findViewById<LinearLayout>(R.id.app_bar_container_right)
    private val divider = view.findViewById<View>(R.id.app_bar_divider)

    var onAppBarButtonButtonClickListener: OnAppBarButtonClickListener? = null

    var title = ""
        set(value) {
            textViewTitle.text = value
            field = value
        }

    var centerTitle = true
        set(value) {
            if (value) {
                setCenterTitleConstraintSet()
            } else {
                setLeftTitleConstraintSet()
            }

            field = value
        }

    var showBackButton = true
        set(value) {
            backButton.visibility = if (value) View.VISIBLE else View.GONE
            field = value
        }

    open var showDivider = true
        set(value) {
            divider.visibility = if (value) View.VISIBLE else View.GONE
            field = value
        }

    var backButtonOnClickListener: OnClickListener? = null

    init {
        context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.BaseAppBar,
                defStyleAttr,
                defStyleRes
        ).apply {
            showBackButton = getBoolean(R.styleable.BaseAppBar_showBackButton, true)
            centerTitle = getBoolean(R.styleable.BaseAppBar_centerTitle, true)
            showDivider = getBoolean(R.styleable.BaseAppBar_showDivider, true)
            title = getString(R.styleable.BaseAppBar_titleText) ?: ""

            recycle()
        }
    }

    fun addViewInLeft(view: View, index: Int = -1) {
        if(view.layoutParams == null) view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, context.appBarHeight)
        if (index > -1) leftContainer.addView(view, index)
        else leftContainer.addView(view)

        view.setOnClickListener {
            onAppBarButtonButtonClickListener?.onClickViewInLeftContainer(view, leftContainer.indexOfChild(view))
        }
    }

    fun addViewInRight(view: View, index: Int = -1) {
        if(view.layoutParams == null) view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, context.appBarHeight)
        if (index > -1) rightContainer.addView(view, index)
        else rightContainer.addView(view)

        view.setOnClickListener {
            onAppBarButtonButtonClickListener?.onClickViewInRightContainer(view, rightContainer.indexOfChild(view))
        }
    }

    fun removeViewInLeft(view: View) {
        if (leftContainer.contains(view)) {
            leftContainer.removeView(view)
        }
    }

    fun removeViewInLeft(index: Int) {
        leftContainer.removeViewAt(index)
    }

    fun removeViewInRight(view: View) {
        if (rightContainer.contains(view)) {
            rightContainer.removeView(view)
        }
    }

    fun removeViewInRight(index: Int) {
        rightContainer.removeViewAt(index)
    }

    inline fun setOnAppBarButtonClickListener(
            crossinline onclickViewInLeftContainer : (view: View, index: Int) -> (Unit) = {_, _ ->},
            crossinline onClickViewInRightContainer : (view: View, index: Int) -> (Unit) = {_, _ ->}
    ) {
        onAppBarButtonButtonClickListener = object : OnAppBarButtonClickListener {
            override fun onClickViewInLeftContainer(view: View, index: Int) {
                onclickViewInLeftContainer(view, index)
            }

            override fun onClickViewInRightContainer(view: View, index: Int) {
                onClickViewInRightContainer(view, index)
            }
        }
    }

    private fun setCenterTitleConstraintSet() {
        ConstraintSet().apply {
            clone(container)
            clear(R.id.app_bar_title)
            connect(R.id.app_bar_title, ConstraintSet.TOP, R.id.app_bar_container, ConstraintSet.TOP)
            connect(R.id.app_bar_title, ConstraintSet.BOTTOM, R.id.app_bar_container, ConstraintSet.BOTTOM)
            connect(R.id.app_bar_title, ConstraintSet.START, R.id.app_bar_container, ConstraintSet.START)
            connect(R.id.app_bar_title, ConstraintSet.END, R.id.app_bar_container, ConstraintSet.END)
            applyTo(container)
        }
        textViewTitle.gravity = Gravity.CENTER
    }

    private fun setLeftTitleConstraintSet() {
        ConstraintSet().apply {
            clone(container)
            clear(R.id.app_bar_title)
            connect(R.id.app_bar_title, ConstraintSet.TOP, R.id.app_bar_container, ConstraintSet.TOP)
            connect(R.id.app_bar_title, ConstraintSet.BOTTOM, R.id.app_bar_container, ConstraintSet.BOTTOM)
            connect(R.id.app_bar_title, ConstraintSet.START, R.id.app_bar_container_left, ConstraintSet.END)
            connect(R.id.app_bar_title, ConstraintSet.END, R.id.app_bar_container, ConstraintSet.END)
            applyTo(container)
        }
        textViewTitle.gravity = Gravity.CENTER_VERTICAL or Gravity.START
    }
}

//App bar height (?android:attr/actionBarSize)
private val Context.appBarHeight: Int
    get() = with(TypedValue()) {
        if (theme.resolveAttribute(android.R.attr.actionBarSize, this, true))
            TypedValue.complexToDimension(this.data, resources.displayMetrics).toInt()
        else 0
    }

fun Context.appBarImageButton(drawable: Drawable, width: Int = appBarHeight, height: Int = appBarHeight) = ImageButton(this).apply {
    layoutParams = ViewGroup.LayoutParams(width, height)
    background = null
    setImageDrawable(drawable)
}

fun Context.appBarImageButton(@DrawableRes resId: Int, width: Int = appBarHeight, height: Int = appBarHeight) = ImageButton(this).apply {
    layoutParams = ViewGroup.LayoutParams(width, height)
    background = null
    setImageResource(resId)
}

fun Context.appBarTextButton(text: CharSequence, width: Int = appBarHeight, height: Int = appBarHeight) = Button(this).apply {
    layoutParams = ViewGroup.LayoutParams(width, height)
    background = null
    this.text = text
    gravity = Gravity.CENTER
    textSize = 14f
}

fun Fragment.appBarImageButton(drawable: Drawable,
                               width: Int = activity?.appBarHeight
                                       ?: ViewGroup.LayoutParams.WRAP_CONTENT,
                               height: Int = activity?.appBarHeight
                                       ?: ViewGroup.LayoutParams.WRAP_CONTENT) =
        activity!!.appBarImageButton(drawable, width, height)

fun Fragment.appBarImageButton(@DrawableRes resId: Int,
                               width: Int = activity?.appBarHeight
                                       ?: ViewGroup.LayoutParams.WRAP_CONTENT,
                               height: Int = activity?.appBarHeight
                                       ?: ViewGroup.LayoutParams.WRAP_CONTENT) =
        activity!!.appBarImageButton(resId, width, height)

fun Fragment.appBarTextButton(text: CharSequence,
                              width: Int = activity?.appBarHeight
                                      ?: ViewGroup.LayoutParams.WRAP_CONTENT,
                              height: Int = activity?.appBarHeight
                                      ?: ViewGroup.LayoutParams.WRAP_CONTENT) =
        activity!!.appBarTextButton(text, width, height)