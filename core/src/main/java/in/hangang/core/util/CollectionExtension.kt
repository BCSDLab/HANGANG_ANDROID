package `in`.hangang.core.util

fun <E> MutableCollection<E>.init(collection: Collection<E>) {
    this.clear()
    this.addAll(collection)
}