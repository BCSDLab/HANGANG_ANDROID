package `in`.hangang.core.view.star

import `in`.hangang.core.R
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout

class StarView @JvmOverloads constructor(
    context: Context,
    private val attributeSet: AttributeSet? = null,
    private val defStyleAttr: Int = 0,
    private val defStyleRes: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    //Inflate Layout and views in layout
    private val view: View =
        LayoutInflater.from(context).inflate(R.layout.layout_star_view, this, true)
    private val one = view.findViewById<ImageView>(R.id.start_1)
    private val two = view.findViewById<ImageView>(R.id.start_2)
    private val three = view.findViewById<ImageView>(R.id.start_3)
    private val four = view.findViewById<ImageView>(R.id.start_4)
    private val five = view.findViewById<ImageView>(R.id.start_5)
    private val half = view.findViewById<ImageView>(R.id.start_half)
    var rating = 0f
    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.StarView,
            defStyleAttr,
            defStyleRes
        ).apply {
            rating = getFloat(R.styleable.StarView_rating, 0f)
            drawStar(rating)
            recycle()
        }
    }
    fun setRatingStar(number: Float){
        drawStar(number)
    }
    fun drawStar(number: Float){
        when(number){
            0.5f -> {
                half.visibility = View.VISIBLE
                one.visibility = View.INVISIBLE
                two.visibility = View.INVISIBLE
                three.visibility = View.INVISIBLE
                four.visibility = View.INVISIBLE
                five.visibility = View.INVISIBLE
            }
            1f -> {
                half.visibility = View.INVISIBLE
                one.visibility = View.VISIBLE
                two.visibility = View.INVISIBLE
                three.visibility = View.INVISIBLE
                four.visibility = View.INVISIBLE
                five.visibility = View.INVISIBLE
            }
            1.5f -> {
                half.visibility = View.VISIBLE
                one.visibility = View.INVISIBLE
                two.visibility = View.VISIBLE
                three.visibility = View.INVISIBLE
                four.visibility = View.INVISIBLE
                five.visibility = View.INVISIBLE
            }
            2f -> {
                half.visibility = View.INVISIBLE
                one.visibility = View.VISIBLE
                two.visibility = View.VISIBLE
                three.visibility = View.INVISIBLE
                four.visibility = View.INVISIBLE
                five.visibility = View.INVISIBLE
            }
            2.5f -> {
                half.visibility = View.VISIBLE
                one.visibility = View.INVISIBLE
                two.visibility = View.VISIBLE
                three.visibility = View.VISIBLE
                four.visibility = View.INVISIBLE
                five.visibility = View.INVISIBLE
            }
            3f -> {
                half.visibility = View.INVISIBLE
                one.visibility = View.VISIBLE
                two.visibility = View.VISIBLE
                three.visibility = View.VISIBLE
                four.visibility = View.INVISIBLE
                five.visibility = View.INVISIBLE
            }
            3.5f -> {
                half.visibility = View.VISIBLE
                one.visibility = View.INVISIBLE
                two.visibility = View.VISIBLE
                three.visibility = View.VISIBLE
                four.visibility = View.VISIBLE
                five.visibility = View.INVISIBLE
            }
            4f -> {
                half.visibility = View.INVISIBLE
                one.visibility = View.VISIBLE
                two.visibility = View.VISIBLE
                three.visibility = View.VISIBLE
                four.visibility = View.VISIBLE
                five.visibility = View.INVISIBLE
            }
            4.5f -> {
                half.visibility = View.VISIBLE
                one.visibility = View.INVISIBLE
                two.visibility = View.VISIBLE
                three.visibility = View.VISIBLE
                four.visibility = View.VISIBLE
                five.visibility = View.VISIBLE
            }
            5f -> {
                half.visibility = View.INVISIBLE
                one.visibility = View.VISIBLE
                two.visibility = View.VISIBLE
                three.visibility = View.VISIBLE
                four.visibility = View.VISIBLE
                five.visibility = View.VISIBLE
            }
        }
    }
}