package `in`.hangang.hangang.di

import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.data.source.local.TimeTableLocalDataSource
import `in`.hangang.hangang.data.source.local.UserLocalDataSource
import `in`.hangang.hangang.data.source.remote.TimeTableRemoteDataSource
import `in`.hangang.hangang.data.source.remote.UserRemoteDataSource
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository(get() as UserLocalDataSource, get() as UserRemoteDataSource) }
    single { TimeTableRepository(get() as TimeTableLocalDataSource, get() as TimeTableRemoteDataSource) }
}