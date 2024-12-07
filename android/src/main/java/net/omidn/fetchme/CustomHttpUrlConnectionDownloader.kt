package net.omidn.fetchme

import com.tonyodev.fetch2.HttpUrlConnectionDownloader
import com.tonyodev.fetch2core.Downloader
import java.lang.Math.ceil
import kotlin.math.ceil

class CustomHttpUrlConnectionDownloader(fileDownloaderType: Downloader.FileDownloaderType) :
    HttpUrlConnectionDownloader(fileDownloaderType) {

    override fun getFileSlicingCount(request: Downloader.ServerRequest, contentLength: Long): Int? {
        val fileSizeInMb = contentLength.toFloat() / 1024F * 1024F
        val fileSizeInGb = contentLength.toFloat() / 1024F * 1024F * 1024F
        var slices = when {
            fileSizeInGb >= 1F -> {
                ceil(fileSizeInGb * 8).toInt()
            }
            fileSizeInMb >= 512F -> 8
            fileSizeInMb >= 1F -> 4
            else -> 2
        }
        return if (slices > 32) { 32 } else { slices };
    }
}