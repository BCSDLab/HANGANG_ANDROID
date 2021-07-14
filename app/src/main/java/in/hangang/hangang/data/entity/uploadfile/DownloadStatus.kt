package `in`.hangang.hangang.data.entity.uploadfile

data class DownloadStatus(
    val uploadFile: UploadFile,
    val totalBytes: Int,
    val downloadedBytes: Int,
    val status: Int,
    val reason: Int
)