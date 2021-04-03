package `in`.hangang.hangang.di

import `in`.hangang.hangang.constant.AUTH
import `in`.hangang.hangang.constant.NO_AUTH
import `in`.hangang.hangang.constant.REFRESH_AUTH
import `in`.hangang.hangang.data.source.local.TimeTableLocalDataSource
import `in`.hangang.hangang.data.source.local.UserLocalDataSource
import `in`.hangang.hangang.data.source.remote.TimeTableRemoteDataSource
import `in`.hangang.hangang.data.source.remote.UserRemoteDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    single { UserRemoteDataSource(get(named(NO_AUTH)), get(named(AUTH)), get(named(REFRESH_AUTH))) }
    single { UserLocalDataSource() }
    single { TimeTableRemoteDataSource(get(named(NO_AUTH)), get(named(AUTH)), get(named(REFRESH_AUTH))) }
    single { TimeTableLocalDataSource() }


}