package `in`.hangang.hangang.util

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.view.edittext.EditTextWithError
import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.constant.ACCESS_TOKEN
import `in`.hangang.hangang.constant.REFRESH_AUTH
import `in`.hangang.hangang.constant.REFRESH_TOKEN
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get
import retrofit2.HttpException
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

fun <T> Single<T>.withThread(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.handleUpdateAccessToken(): Single<T> {
    return this.compose(retryOnNotAuthorized<T>())
}

// TODO : 공통 HttpException 처리
fun <T> Single<T>.handleHttpException(): Single<T> {
    return this.handleUpdateAccessToken()
        .doOnError {
            if (it !is HttpException) return@doOnError
            LogUtil.e("handle http exception : ${it.code()}")
            when (it.code()) {

            }
        }
}

fun Completable.toSingleConvert(): Single<Boolean> {
    return this.toSingleDefault(true)
        .onErrorReturnItem(false)
}

fun <T> Single<T>.handleProgress(viewModel: ViewModelBase): Single<T> {
    return this.doOnSubscribe { viewModel.isLoading.postValue(true) }
        .doOnError { viewModel.isLoading.postValue(false) }
        .doOnSuccess { viewModel.isLoading.postValue(false) }
}

fun EditTextWithError.debounce(time: Long = 500L, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Observable<String> {
    return Observable.create { emitter: ObservableEmitter<String>? ->
        this.addTextChangedListener {
            emitter?.onNext(it.toString())
        }
    }.debounce(time, timeUnit)
}


private fun <T> retryOnNotAuthorized(): SingleTransformer<T, T> {
    return SingleTransformer<T, T> { upstream ->
        upstream.onErrorResumeNext { throwable ->
            if (throwable is HttpException && throwable.code() == 401) {
                val retrofit: Retrofit = get(Retrofit::class.java, named(REFRESH_AUTH))
                retrofit.create(AuthApi::class.java)
                    .refreshToken()
                    .doOnSuccess { token ->
                        LogUtil.d("new access token : ${token.accessToken}")
                        LogUtil.d("new refresh token : ${token.refreshToken}")
                        Hawk.put(ACCESS_TOKEN, token.accessToken)
                        Hawk.put(REFRESH_TOKEN, token.refreshToken)
                    }
                    .doOnError { error -> LogUtil.e("token update error : $error") }
                    .flatMap {
                        Completable.complete().andThen(upstream)
                    }
            } else
                Single.error(throwable)
        }
    }
}