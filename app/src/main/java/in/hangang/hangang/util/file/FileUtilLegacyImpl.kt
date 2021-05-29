package `in`.hangang.hangang.util.file

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import io.reactivex.rxjava3.core.Single
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class FileUtilLegacyImpl(private val context: Context) : FileUtil {
    override fun saveImageToPictures(
            bitmap: Bitmap,
            fileName: String,
            mineType: String,
            directory: String,
            mediaContentUri: Uri
    ): Single<Uri> {
        return Single.create { subscriber ->
            var outputStream: OutputStream? = null
            try {
                val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                val file = File(path, fileName)
                outputStream = FileOutputStream(file)

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                subscriber.onSuccess(file.toUri())
            } catch (e: Exception) {
                subscriber.onError(e)
            } finally {
                outputStream?.close()
            }
        }
    }
}