package `in`.hangang.core.util

import android.text.Editable

fun String.toSHA256() = StringUtil.hashString(this, StringUtil.SHA256)
fun String.toMD5() = StringUtil.hashString(this, StringUtil.MD5)
fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)