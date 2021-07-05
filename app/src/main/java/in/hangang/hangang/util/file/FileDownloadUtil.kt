package `in`.hangang.hangang.util.file

object FileDownloadUtil {

    /*fun downloadFile(context: Context, fileUtil: FileUtil, uploadFile: UploadFile, url: String) : Observable<DownloadStatus> {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadRequest = DownloadManager.Request(Uri.parse(url))
            .setTitle(uploadFile.fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setDestinationUri(fileUtil.getDownloadPathUri(uploadFile.fileName))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadId = downloadManager.enqueue(downloadRequest)

        val onDownloadComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if(intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                    if(id == downloadId) {
                        val query = DownloadManager.Query().setFilterById(id)
                        val cursor = downloadManager.query(query)
                        if(!cursor.moveToFirst()) return

                        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        if(status == DownloadManager.STATUS_SUCCESSFUL) {
                            Hawk.put(uploadFile.id.toString(), fileUtil.getDownloadPathUri(uploadFile.fileName))
                        }
                    }
                }
            }
        }

        context.registerReceiver(onDownloadComplete, IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        })

        return Observable.create {
            val query = DownloadManager.Query()
            query.setFilterById(downloadId)
            val cursor = downloadManager.query(query)
            if(!cursor.moveToFirst()) it.onError(IllegalArgumentException("Wrong downloadId"))

            var status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            var reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
            var downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            var totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            while(status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_RUNNING) {
                it.onNext(DownloadStatus(
                    uploadFile = uploadFile,
                    totalBytes = totalBytes,
                    downloadedBytes = downloadedBytes,
                    status = status,
                    reason = reason
                ))

                Thread.sleep(1000)
                status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            }

            it.onNext(DownloadStatus(
                uploadFile = uploadFile,
                totalBytes = totalBytes,
                downloadedBytes = downloadedBytes,
                status = status,
                reason = reason
            ))

            it.onComplete()
        }
    }*/
}