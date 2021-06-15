package `in`.hangang.hangang.data.entity.lecturebank

import `in`.hangang.hangang.data.entity.uploadfile.UploadFile

data class DownloadFileInfo(
    val uploadFile: UploadFile,
    val downloadUrl: String
)