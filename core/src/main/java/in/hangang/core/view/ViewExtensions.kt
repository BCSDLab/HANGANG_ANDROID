package `in`.hangang.core.view

import android.widget.TextView
import org.koin.core.qualifier._q
import java.lang.IllegalStateException

fun TextView.setMultiEllipsizeText(
    ellipsizedTexts: Array<CharSequence>,
    ellipsizedTextWeights : Array<Double>
) {
    if(ellipsizedTexts.size != ellipsizedTextWeights.size) throw IllegalStateException("ellipsizedTexts and ellipsizedTextWeights size must be same.")

    val textPair = ellipsizedTexts.mapIndexed { index, charSequence ->
        charSequence to ellipsizedTextWeights[index]
    }

    val ellipsizedTextsWidth = measuredWidth - textPair.filter { it.second < 0 }.sumByDouble { paint.measureText(it.first.toString()).toDouble() }

    val texts = mutableListOf<String>()
    val weightSum = ellipsizedTextWeights.filter { it >= 0 }.sum()
    textPair.forEach {
        if(it.second < 0) texts.add(it.first.toString())
        else {

        }
    }
}