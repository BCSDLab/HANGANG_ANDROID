package `in`.hangang.core.http.request

import `in`.hangang.core.util.getSize
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.orhanobut.logger.Logger
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.Exception

class ProgressFileRequestBody(
    private val context: Context,
    private val uri: Uri,
    private val mimeType: String?,
    private val callback: ProgressRequestBodyCallback
) : RequestBody() {

    private var wrote = false

    override fun contentType(): MediaType? = mimeType?.let { MediaType.parse(mimeType) }
    override fun contentLength(): Long = uri.getSize(context) ?: 0L

    override fun writeTo(sink: BufferedSink) {
        val handler = Handler(Looper.getMainLooper())
        val fileInputStream : InputStream
        try {
            val contentLength = contentLength()
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            fileInputStream = context.contentResolver.openInputStream(uri)!!

            var uploadedBytes = 0L

            var read: Int

            while (fileInputStream.read(buffer).also { read = it } != -1) {
                setProgress(handler, uploadedBytes, contentLength)
                uploadedBytes += read
                sink.write(buffer, 0, read)
            }

            fileInputStream.close()
            finish(handler)
        } catch (e: Exception) {
            handler.post {
                callback.onError(e)
            }
        }
    }

    @Synchronized
    fun finish(handler: Handler) {
        handler.post {
            if (!wrote) {
                callback.onFinish()
                wrote = true
            }
        }
    }

    @Synchronized
    fun setProgress(handler: Handler, uploadedBytes: Long, contentLength: Long) {
        handler.post {
            if (!wrote) {
                callback.onProgress((100 * uploadedBytes.toDouble() / contentLength).toInt())
            }
        }
    }

    companion object {
        const val DEFAULT_BUFFER_SIZE = 2048
    }
}