package `in`.hangang.core.util

fun String.toSHA256() = StringUtil.hashString(this, StringUtil.SHA256)
fun String.toMD5() = StringUtil.hashString(this, StringUtil.MD5)