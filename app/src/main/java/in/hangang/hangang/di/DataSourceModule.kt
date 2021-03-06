package `in`.hangang.hangang.di

import `in`.hangang.hangang.constant.AUTH
import `in`.hangang.hangang.constant.NO_AUTH
import `in`.hangang.hangang.constant.REFRESH_AUTH
import `in`.hangang.hangang.data.source.local.LectureBankLocalDataSource
import `in`.hangang.hangang.data.source.local.LectureLocalDataSource
import `in`.hangang.hangang.data.source.local.TimeTableLocalDataSource
import `in`.hangang.hangang.data.source.local.UserLocalDataSource
import `in`.hangang.hangang.data.source.remote.LectureBankRemoteDataSource
import `in`.hangang.hangang.data.source.remote.LectureRemoteDataSource
import `in`.hangang.hangang.data.source.remote.TimeTableRemoteDataSource
import `in`.hangang.hangang.data.source.remote.UserRemoteDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    single { UserRemoteDataSource(get(named(NO_AUTH)), get(named(AUTH)), get(named(REFRESH_AUTH))) }
    single { UserLocalDataSource() }
    single { LectureRemoteDataSource(get(named(NO_AUTH)), get(named(AUTH))) }
    single { LectureLocalDataSource() }
    single { TimeTableRemoteDataSource(get(named(AUTH))) }
    single { TimeTableLocalDataSource() }
    single { LectureBankRemoteDataSource(get(), get(named(AUTH))) }
    single { LectureBankLocalDataSource() }
}
