package `in`.hangang.core.view

import android.app.Activity
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.MenuRes
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

fun View.showPopupMenu(@MenuRes menuId: Int) : PopupMenu {
    return PopupMenu(context, this).apply {
        inflate(menuId)
        show()
    }
}