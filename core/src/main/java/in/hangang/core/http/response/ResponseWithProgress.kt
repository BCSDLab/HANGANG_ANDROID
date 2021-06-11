package `in`.hangang.core.http.response

class ResponseWithProgress<T>(
    val percentage : Int,
    val response : T?
)