package `in`.hangang.hangang.util

fun <E> List<E>.toApiString() = joinToString(prefix = "[", postfix = "]")