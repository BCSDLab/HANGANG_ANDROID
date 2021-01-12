package `in`.hangang.hangang.util

import `in`.hangang.core.base.viewmodel.ViewModelBase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

fun <T> Single<T>.withThread(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.handleUpdateAccessToken(): Single<T> {
    return this.compose(retryOnNotAuthorized<T>())
}

fun <T> Single<T>.handleHttpException(): Single<T> {
    return this.handleUpdateAccessToken()
        .doOnError {
            if (it !is HttpException) return@doOnError
            // TODO -> HTTP exception handle
            println(it)
            when (it.code()) {

            }
        }
}

fun <T> Single<T>.handleProgress(viewModel: ViewModelBase): Single<T> {
    return this.doOnSubscribe { viewModel.isLoading.postValue(true) }
        .doOnError { viewModel.isLoading.postValue(false) }
        .doFinally { viewModel.isLoading.postValue(false) }
}

private fun <T> retryOnNotAuthorized(): SingleTransformer<T, T> {
    return SingleTransformer<T, T> { upstream ->
        upstream.onErrorResumeNext { throwable ->
            if (throwable is HttpException && throwable.code() == 401) {
                // TODO -> 토큰 업데이트 API 수정
                Single.just("새로운 토큰")
                    .doOnSuccess { token -> LogUtil.d("token : $token") }
                    .flatMap {
                        Completable.complete().andThen(upstream)
                    }
            } else
                Single.error(throwable)
        }
    }
}


fun main() {
    Single.just("sample text")
        .map {
            throw HttpException(
                Response.error<String>(
                    401,
                    ResponseBody.create(
                        MediaType.parse("application/json"),
                        "{\"key\":[\"somestuff\"]}"
                    )
                )
            )
        }
        .handleHttpException()
        .observeOn(Schedulers.computation())
        .subscribeOn(Schedulers.io())
        .subscribe(
            { onSuccess -> print("$onSuccess") },
            { throwable -> print("$throwable") }
        )

    Thread.sleep(2000L)
}
