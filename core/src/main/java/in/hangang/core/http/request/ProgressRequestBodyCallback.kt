package `in`.hangang.core.http.request

open class ProgressRequestBodyCallback {
    open fun onProgress(percentage: Int) {}
    open fun onError(t: Throwable) {}
    open fun onFinish() {}
}