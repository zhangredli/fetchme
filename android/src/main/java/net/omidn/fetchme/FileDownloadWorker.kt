package net.omidn.fetchme

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tonyodev.fetch2.DefaultFetchNotificationManager
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileDownloadWorker(val context: Context, workerParameters: WorkerParameters): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val fetch = FetchmePlugin.fetchInstance;
        if(fetch != null) {
            while (suspendCoroutine { continuation ->
                    fetch.hasActiveDownloads(true) { result ->
                        continuation.resume(result)
                    }
                }) { //loop whiles fetch is downloading in the background to keep worker thread alive
                try {
                    Thread.sleep(2000) //let the thread sleep being that fetch is using its own thread.
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            FetchmePlugin.workManager = null;
        }
        return Result.success()
    }
}