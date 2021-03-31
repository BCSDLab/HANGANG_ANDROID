package `in`.hangang.hangang.di

import `in`.hangang.hangang.util.TimetableUtil
import `in`.hangang.hangang.util.file.FileUtil
import `in`.hangang.hangang.util.file.FileUtilLegacyImpl
import `in`.hangang.hangang.util.file.FileUtilNewImpl
import android.os.Build
import org.koin.dsl.module

val fileModule = module {
    single {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            FileUtilNewImpl(get())
        } else {
            FileUtilLegacyImpl(get())
        }
    }
}