package `in`.hangang.hangang.util

fun <E> List<E>.toApiString() = joinToString(prefix = "[", postfix = "]")

fun <K, V: Collection<E>, E> Map<K, V>.toValuesMutableList() = this.flatMap {
    it.value
}

fun <K, V: Collection<E>, E> Map<K, V>.toValuesList() = this.toValuesMutableList().toList()