package `in`.hangang.hangang.di

import `in`.hangang.hangang.util.file.FileUtil
import android.os.Build
import android.os.FileUtils
import org.koin.dsl.module

val fileModule = module {
    single {
        FileUtil(get())
    }
}