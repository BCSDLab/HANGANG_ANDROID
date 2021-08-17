package `in`.hangang.hangang.util

import `in`.hangang.hangang.R
import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.constant.ACCESS_TOKEN
import `in`.hangang.hangang.constant.REFRESH_AUTH
import `in`.hangang.hangang.constant.REFRESH_TOKEN
import `in`.hangang.hangang.ui.login.LoginActivity
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.widget.Toast
import androidx.core.os.HandlerCompat
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.subjects.PublishSubject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent
import retrofit2.Retrofit

class TokenAuthenticator(
    private val context: Context
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        try {
            if (response.code() != 401) return null

            val tokenResult = getRefreshedJwtTokenResult()

            tokenResult.onFailure {
                goToLoginActivity()
                return null
            }

            tokenResult.onSuccess { accessToken ->
                if (accessToken.isEmpty()) {
                    goToLoginActivity()
                    return null
                }
                return getRequest(response, accessToken)
            }
        } catch (e: Exception) {
            goToLoginActivity()
            return null
        }

        return null
    }

    private fun getRefreshedJwtTokenResult(): Result<String> {
        val publishSubject: PublishSubject<Result<String>> = PublishSubject.create()
        val retrofit: Retrofit = KoinJavaComponent.get(Retrofit::class.java, named(REFRESH_AUTH))
        retrofit.create(AuthApi::class.java)
            .refreshToken()
            .retry(5L)
            .subscribe(
                { token ->
                    if (token.accessToken == null || token.refreshToken == null) {
                        publishSubject.onNext(Result.failure(Throwable("Token Empty")))
                        publishSubject.onComplete()
                        return@subscribe
                    }
                    LogUtil.d("new access token : ${token.accessToken}")
                    LogUtil.d("new refresh token : ${token.refreshToken}")
                    Hawk.put(ACCESS_TOKEN, token.accessToken)
                    Hawk.put(REFRESH_TOKEN, token.refreshToken)
                    publishSubject.onNext(Result.success(token.accessToken!!))
                    publishSubject.onComplete()
                },
                { error ->
                    LogUtil.e("token update error : $error")
                    publishSubject.onNext(Result.failure(Throwable("Token refresh failed")))
                    publishSubject.onComplete()
                })
        return publishSubject.blockingSingle()
    }

    private fun getRequest(response: Response, token: String): Request {
        return response.request()
            .newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", "Bearer $token")
            .build()
    }

    private fun goToLoginActivity(){
        val handler = HandlerCompat.createAsync(Looper.getMainLooper())
        Intent(context.applicationContext, LoginActivity::class.java).run {
            handler.post{
                Toast.makeText(
                    context.applicationContext,
                    context.getString(R.string.token_expired),
                    Toast.LENGTH_SHORT
                ).show()
            }
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(this)
        }

    }

    companion object {
        const val TAG = "TokenAuthenticator"
    }
}
