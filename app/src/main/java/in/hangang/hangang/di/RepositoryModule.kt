package `in`.hangang.hangang.di

import `in`.hangang.hangang.data.source.local.LectureLocalDataSource
import `in`.hangang.hangang.data.source.local.TimeTableLocalDataSource
import `in`.hangang.hangang.data.source.local.UserLocalDataSource
import `in`.hangang.hangang.data.source.remote.LectureRemoteDataSource
import `in`.hangang.hangang.data.source.remote.TimeTableRemoteDataSource
import `in`.hangang.hangang.data.source.remote.UserRemoteDataSource
import `in`.hangang.hangang.data.source.repository.LectureRepository
import `in`.hangang.hangang.data.source.repository.TimeTableRepository
import `in`.hangang.hangang.data.source.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository(get() as UserLocalDataSource, get() as UserRemoteDataSource) }
    single { LectureRepository(get() as LectureLocalDataSource, get() as LectureRemoteDataSource) }
    single { TimeTableRepository(get() as TimeTableLocalDataSource, get() as TimeTableRemoteDataSource) }
}