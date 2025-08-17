package net.omidn.fetchme

import com.tonyodev.fetch2.HttpUrlConnectionDownloader
import com.tonyodev.fetch2core.Downloader
import io.flutter.Log

class CustomHttpUrlConnectionDownloader(fileDownloaderType: Downloader.FileDownloaderType) :
    HttpUrlConnectionDownloader(fileDownloaderType) {

    override fun getFileSlicingCount(request: Downloader.ServerRequest, contentLength: Long): Int? {
        val fileSizeInMb = contentLength.toFloat() / (1024F * 1024F)
        val fileSizeInGb = contentLength.toFloat() / (1024F * 1024F * 1024F)
        var slices = when {
            fileSizeInGb >= 2F -> 8
            fileSizeInGb >= 1F -> 6
            fileSizeInMb >= 512F -> 4
            fileSizeInMb >= 64F -> 2
            else -> 1
        }
        Log.d("Fetchme", "files slices: $slices")
        return if (slices > 8) { 8 } else { slices };
    }
}