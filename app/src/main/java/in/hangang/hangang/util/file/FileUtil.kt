package `in`.hangang.hangang.util.file

import `in`.hangang.core.util.requireQ
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.uploadfile.DownloadStatus
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import android.app.DownloadManager
import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.core.Observable
import java.io.FileOutputStream

class FileUtil(
    private val context: Context
) {
    fun saveImageToPictures(
        bitmap: Bitmap,
        fileName: String
    ) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/*jpeg")
            requireQ {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: return

        val parcelFileDescriptor =
            context.contentResolver.openFileDescriptor(uri, "w", null) ?: return

        val fileOutputStream = FileOutputStream(parcelFileDescriptor.fileDescriptor)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.close()

        requireQ {
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(uri, contentValues, null, null)
        }
    }

    fun downloadFile(
        url: String,
        uploadFile: UploadFile,
    ): Observable<DownloadStatus> {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(uploadFile.fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uploadFile.fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadId = downloadManager.enqueue(request)

        val intentFilter = IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        }
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId == id) {
                    when (intent.action) {
                        DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                            val query = DownloadManager.Query().apply { setFilterById(downloadId) }
                            val cursor = downloadManager.query(query)
                            if (!cursor.moveToFirst()) return
                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                Toast.makeText(
                                    context,
                                    context.getString(
                                        R.string.download_complete_toast_message,
                                        uploadFile.fileName
                                    ),
                                    Toast.LENGTH_SHORT
                                ).show()
                                Hawk.put(
                                    uploadFile.id.toString(),
                                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIAPROVIDER_URI))
                                )
                            }
                        }
                    }
                }
            }
        }, intentFilter)

        return Observable.create {
            val query = DownloadManager.Query().apply { setFilterById(downloadId) }
            var cursor = downloadManager.query(query)

            if (!cursor.moveToFirst())
                it.onError(IllegalStateException("Wrong downloadId"))

            var status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            var reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
            var totalBytes =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            var downloadedBytes =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))

            do {
                it.onNext(DownloadStatus(uploadFile, totalBytes, downloadedBytes, status, reason))

                cursor = downloadManager.query(query)

                if (!cursor.moveToFirst())
                    break

                Thread.sleep(500)

                status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                totalBytes =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                downloadedBytes =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            } while (status == DownloadManager.STATUS_RUNNING || status == DownloadManager.STATUS_PENDING)

            it.onNext(DownloadStatus(uploadFile, totalBytes, downloadedBytes, status, reason))
            it.onComplete()
        }
    }
}