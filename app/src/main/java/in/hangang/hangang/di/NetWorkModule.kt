package `in`.hangang.hangang.di

import `in`.hangang.hangang.HangangApplication
import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.util.LogUtil
import com.orhanobut.hawk.Hawk
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val netWorkModule = module {
    factory(named(URL)) {
        if (HangangApplication.instance.isApplicationDebug)
            STAGE_SERVER_BASE_URL
        else
            PRODUCTION_SERVER_BASE_URL
    }

    factory(named(NO_AUTH)) {
        OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (HangangApplication.instance.isApplicationDebug) {
                    HttpLoggingInterceptor.Level.BODY
                } else
                    HttpLoggingInterceptor.Level.HEADERS
            })
        }.build()
    }


    factory(named(AUTH)) {
        OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (HangangApplication.instance.isApplicationDebug) {
                    HttpLoggingInterceptor.Level.BODY
                } else
                    HttpLoggingInterceptor.Level.HEADERS
            })
            addInterceptor { chain ->
                val accessToken = Hawk.get(ACCESS_TOKEN, "")
                val refreshToken = Hawk.get(REFRESH_TOKEN, "")
                LogUtil.d("access token : $accessToken")
                val newRequest: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $accessToken")
                        .build()
                chain.proceed(newRequest)
            }
        }.build()
    }

    factory(named(REFRESH_AUTH)) {
        OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (HangangApplication.instance.isApplicationDebug) {
                    HttpLoggingInterceptor.Level.BODY
                } else
                    HttpLoggingInterceptor.Level.HEADERS
            })
            addInterceptor { chain ->
                val refreshToken = Hawk.get(REFRESH_TOKEN, "")
                LogUtil.d("refresh token : $refreshToken")
                val newRequest: Request = chain.request().newBuilder()
                        .addHeader("RefreshToken", "Bearer $refreshToken")
                        .build()
                chain.proceed(newRequest)
            }
        }.build()
    }


    single(named(NO_AUTH)) {
        Retrofit.Builder()
                .client(get(named(NO_AUTH)))
                .baseUrl(get<String>(named(URL)))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
    }

    single(named(AUTH)) {
        Retrofit.Builder()
                .client(get(named(AUTH)))
                .baseUrl(get<String>(named(URL)))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
    }

    single(named(REFRESH_AUTH)) {
        Retrofit.Builder()
                .client(get(named(REFRESH_AUTH)))
                .baseUrl(get<String>(named(URL)))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
    }

    single(named(NO_AUTH)) {
        provideNoAuthApi(get(named(NO_AUTH)))
    }

    single(named(AUTH)) {
        provideAuthApi(get(named(AUTH)))
    }

    single(named(REFRESH_AUTH)) {
        provideAuthApi(get(named(REFRESH_AUTH)))
    }
}

fun provideNoAuthApi(retrofit: Retrofit): NoAuthApi = retrofit.create(NoAuthApi::class.java)

fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)