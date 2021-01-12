package `in`.hangang.hangang.di

import `in`.hangang.hangang.HangangApplication
import `in`.hangang.hangang.constant.*
import com.orhanobut.hawk.Hawk
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val netWorkModule = module {
    single(named(URL)) {
        if (HangangApplication.instance.isApplicationDebug)
            STAGE_SERVER_BASE_URL
        else
            PRODUCTION_SERVER_BASE_URL
    }

    single(named(NO_AUTH)) {
        OkHttpClient.Builder().apply {
            connectTimeout(1, TimeUnit.MINUTES)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (HangangApplication.instance.isApplicationDebug) {
                    HttpLoggingInterceptor.Level.BODY
                } else
                    HttpLoggingInterceptor.Level.HEADERS
            })
        }.build()
    }


    single(named(AUTH)) {
        OkHttpClient.Builder().apply {
            connectTimeout(1, TimeUnit.MINUTES)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (HangangApplication.instance.isApplicationDebug) {
                    HttpLoggingInterceptor.Level.BODY
                } else
                    HttpLoggingInterceptor.Level.HEADERS
            })
            addInterceptor { chain ->
                val token = Hawk.get(ACCESS_TOKEN, "")
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }
        }.build()
    }


    single(named(NO_AUTH)) {
        Retrofit.Builder()
            .client(get(named(NO_AUTH)))
            .baseUrl(get<String>(named(URL)))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single(named(AUTH)) {
        Retrofit.Builder()
            .client(get(named(AUTH)))
            .baseUrl(get<String>(named(URL)))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

//    single { get<Retrofit>().create(Service::class.java) }
}