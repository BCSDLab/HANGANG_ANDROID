package `in`.hangang.hangang.util.file

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import io.reactivex.rxjava3.core.Single

interface FileUtil {
    fun saveImageToPictures(
            bitmap: Bitmap,
            fileName: String,
            mineType: String = "image/jpeg",
            directory: String = Environment.DIRECTORY_PICTURES,
            mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    ): Single<Uri>
}