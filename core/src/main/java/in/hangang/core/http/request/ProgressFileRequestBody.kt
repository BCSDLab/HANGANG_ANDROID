package `in`.hangang.core.http.request

import `in`.hangang.core.util.getSize
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.lang.Exception

class ProgressFileRequestBody(
    private val context: Context,
    private val uri: Uri,
    private val mimeType: String?,
    private val callback: ProgressRequestBodyCallback
) : RequestBody() {

    private fun progressUpdater(uploadedBytes: Long, totalBytes: Long) = Runnable {
        callback.onProgress((100 * uploadedBytes.toDouble() / totalBytes).toInt())
    }

    override fun contentType(): MediaType? = mimeType?.let { MediaType.parse(mimeType) }
    override fun contentLength(): Long = uri.getSize(context) ?: 0L

    override fun writeTo(sink: BufferedSink) {
        val contentLength = contentLength()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = context.contentResolver.openInputStream(uri) ?: return
        val handler = Handler(Looper.getMainLooper())

        var uploadedBytes = 0L

        try {
            var read: Int

            while (fileInputStream.read(buffer).also { read = it } != -1) {

                // update progress on UI thread
                handler.post(progressUpdater(uploadedBytes, contentLength))
                uploadedBytes += read
                sink.write(buffer, 0, read)
            }

            handler.post {
                callback.onFinish()
            }
        } catch (e: Exception) {
            handler.post {
                callback.onError(e)
            }
        } finally {
            fileInputStream.close()
        }
    }

    companion object {
        const val DEFAULT_BUFFER_SIZE = 2048
    }
}