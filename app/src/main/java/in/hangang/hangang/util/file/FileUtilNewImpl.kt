package `in`.hangang.hangang.util.file

import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.uploadfile.DownloadStatus
import android.app.DownloadManager
import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.io.OutputStream

class FileUtilNewImpl(private val context: Context) : FileUtil(context) {
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
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
                    put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
                }

                val uri = context.contentResolver.insert(mediaContentUri, contentValues)
                outputStream = context.contentResolver.openOutputStream(uri!!)

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                subscriber.onSuccess(uri)
            } catch (e: Exception) {
                subscriber.onError(e)
            } finally {
                outputStream?.close()
            }
        }
    }
}