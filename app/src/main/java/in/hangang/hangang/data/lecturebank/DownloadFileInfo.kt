package `in`.hangang.hangang.data.lecturebank

import `in`.hangang.hangang.data.uploadfile.UploadFile

data class DownloadFileInfo(
    val uploadFile: UploadFile,
    val downloadUrl: String
)