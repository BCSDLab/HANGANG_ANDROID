package `in`.hangang.hangang.util

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject

fun <I, O> ComponentActivity.startActivityForResult(
    activityResultContract: ActivityResultContract<I, O>,
    input : I
) : Single<O> {
    val publishSubject = PublishSubject.create<O>()

    registerForActivityResult(
        activityResultContract
    ) {
        publishSubject.onNext(it)
        publishSubject.onComplete()
    }.launch(input)

    return publishSubject.singleOrError()
}