package `in`.hangang.hangang.di

import `in`.hangang.hangang.data.source.TimetableRepository
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.data.source.local.TimetableLocalDataSource
import `in`.hangang.hangang.data.source.local.UserLocalDataSource
import `in`.hangang.hangang.data.source.remote.TimetableRemoteDataSource
import `in`.hangang.hangang.data.source.remote.UserRemoteDataSource
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository(get() as UserLocalDataSource, get() as UserRemoteDataSource) }
    single { TimetableRepository(get() as TimetableLocalDataSource, get() as TimetableRemoteDataSource) }
}