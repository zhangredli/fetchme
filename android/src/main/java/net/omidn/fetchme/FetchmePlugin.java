package net.omidn.fetchme;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchNotificationManager;
import com.tonyodev.fetch2.HttpUrlConnectionDownloader;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.database.DownloadInfo;
import com.tonyodev.fetch2.database.FetchDatabaseManagerWrapper;
import com.tonyodev.fetch2.fetch.FetchImpl;
import com.tonyodev.fetch2core.Downloader;
import com.tonyodev.fetch2core.Extras;
import com.tonyodev.fetch2core.Func;
import com.tonyodev.fetch2core.HandlerWrapper;
import com.tonyodev.fetch2okhttp.OkHttpDownloader;
import com.tonyodev.fetch2.fetch.FetchModulesBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import kotlin.Unit;
import okhttp3.OkHttpClient;

/**
 * FetchmePlugin
 */
public class FetchmePlugin implements FlutterPlugin, MethodCallHandler {
    FetchNotificationManager notificationManager;

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Context context;
    public static Fetch fetchInstance;
    public static WorkManager workManager;
    FetchConfiguration.Builder fetchConfigBuilder;
    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    private EventChannel eventChannel;
    private EventChannel.EventSink updateEventSink;

    private FetchListener fetchListener;
    private FetchDatabaseManagerWrapper fetchDatabaseManagerWrapper;
    private HandlerWrapper handlerWrapper;
    private Handler uiHandler;


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "fetchme");
        channel.setMethodCallHandler(this);
        this.context = flutterPluginBinding.getApplicationContext();

        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "net.omidn.fetchme/downloadProgressEventStream");

        eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object arguments, EventChannel.EventSink events) {
                updateEventSink = events;
                fetchListener = new FetchListener(updateEventSink, fetchInstance);
                fetchInstance.addListener(fetchListener);
            }

            @Override
            public void onCancel(Object arguments) {

            }
        });
        notificationManager = new MyDefaultFetchNotificationManager(context) {
            @NonNull
            @Override
            public Fetch getFetchInstanceForNamespace(@NonNull String namespace) {
                return fetchInstance;
            }
        };
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("initialize")) {
            initialize(call, result);
        } else if (call.method.equals("enqueue")) {
            enqueue(call, result);
        } else if (call.method.equals("resume")) {
            resume(call, result);
        } else if (call.method.equals("pause")) {
            pause(call, result);
        } else if (call.method.equals("cancel")) {
            cancel(call, result);
        } else if (call.method.equals("delete")) {
            delete(call, result);
        } else if (call.method.equals("remove")) {
            remove(call, result);
        } else if (call.method.equals("retry")) {
            retry(call, result);
        } else if (call.method.equals("getAllDownloadItems")) {
            getAllDownloadItems(result);
        } else if (call.method.equals("getDownloadItem")) {
            getDownloadItem(call, result);
        } else if (call.method.equals("updateRequest")) {
            updateRequest(call, result);
        } else if (call.method.equals("openFile")) {
            openDownloadedFile(call, result);
        } else if (call.method.equals("setSettings")) {
            setSettings(call, result);
        } else if (call.method.equals("pauseAll")) {
            pauseAll(result);
        } else {
            result.notImplemented();
        }

    }

    private void setSettings(MethodCall methodCall, Result result) {
        if(fetchInstance == null) {
            Log.d(FetchmePlugin.class.getName(), "Fetch 初始化!");
            Boolean onlyWiFi = methodCall.argument("onlyWiFi");
            NetworkType networkType = onlyWiFi ? NetworkType.WIFI_ONLY : NetworkType.ALL;
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            fetchConfigBuilder = new FetchConfiguration.Builder(this.context)
                    .enableLogging(n(methodCall.argument("loggingEnabled"), true))
                    .setAutoRetryMaxAttempts(n(methodCall.argument("autoRetryAttempts"), 0))
                    .setDownloadConcurrentLimit(n(methodCall.argument("concurrentDownloads"), 3))
                    .setProgressReportingInterval(n(methodCall.argument("progressInterval"), 1500))
                    .setGlobalNetworkType(networkType)
                    .setNotificationManager(notificationManager).setHttpDownloader(
                            new CustomHttpUrlConnectionDownloader(Downloader.FileDownloaderType.PARALLEL)
//                            new OkHttpDownloader(
//                                    okHttpClient,
//                                    Downloader.FileDownloaderType.PARALLEL
//                            )
                    );
//                .setHttpDownloader(new OkHttpDownloader(okHttpClient));
            FetchConfiguration fetchConfiguration = fetchConfigBuilder
                    .build();
            FetchModulesBuilder.Modules modules = FetchModulesBuilder.INSTANCE.buildModulesFromPrefs(fetchConfiguration);
            fetchDatabaseManagerWrapper = modules.getFetchDatabaseManagerWrapper();
            handlerWrapper = modules.getHandlerWrapper();
            uiHandler = modules.getUiHandler();
            fetchInstance = FetchImpl.newInstance(modules);
            //fetchInstance = Fetch.Impl.getInstance(fetchConfiguration);
            fetchInstance.pauseAll();
        }
        Log.d(FetchmePlugin.class.getName(), "Fetch reset configuration!");
        result.success(null);
    }

    private void updateRequest(MethodCall call, Result result) {
        android.util.Log.d("Fetchme", "updateRequest: called");
        int id = call.argument("id");
        String newUrl = call.argument("newUrl");
        try {
            handlerWrapper.post(() -> {
                try {
                    DownloadInfo download = fetchDatabaseManagerWrapper.get(id);
                    download.setUrl(newUrl);
                    fetchDatabaseManagerWrapper.update(download);
                    uiHandler.post(()-> {
                        result.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
                    });
                } catch (Exception e) {
                    result.error("updateRequest error", e.getMessage(), e.toString());
                }
                return Unit.INSTANCE;
            });
        } catch (Exception e) {
            result.error("updateRequest error", e.getMessage(), e.toString());
        }
//        fetchInstance.getDownload(call.argument("id"), download -> {
//            // url error.
//            android.util.Log.d("Fetchme", "updateRequest: called");
//            String newUrl = call.argument("newUrl");
//            fetchDatabaseManagerWrapper.get(call.argument("id"))
//            Request request = new Request(newUrl, download.getFile());
//            //update other request fields.
//            fetchInstance.updateRequest(download.getId(), request, true, new Func<Download>() {
//                @Override
//                public void call(@NotNull Download newDownload) {
//                    android.util.Log.d("Fetchme", "download id: new(" + newDownload.getId() + ")  old(" + download.getId() + ")");
//                    result.success(DownloadItemMapper.mapToDownloadItem(newDownload).toMap());
//                }
//            }, new Func<Error>() {
//                @Override
//                public void call(@NotNull Error err) {
//                    result.error(err.getValue() + "", err.name(), err);
//                    //failed to update
//                }
//            });
//        });
    }

    private static class MYCD extends OkHttpDownloader {

    }

    private Func<Error> errorFuncForResult(Result result) {
        return error -> result.error(error.getValue() + "", error.toString(), error.getThrowable());
    }

    private void retry(MethodCall call, Result result) {
        int downloadId = call.argument("id");
        Log.d("Fethcme", "fetch retry: " + downloadId);

        fetchInstance.retry(downloadId, download -> {
            result.success(null);
        }, errorFuncForResult(result));
    }

    private void delete(MethodCall call, Result result) {
        int downloadId = call.argument("id");

        fetchInstance.delete(downloadId, download -> {
            result.success(null);
        }, errorFuncForResult(result));
    }

    private void remove(MethodCall call, Result result) {
        int downloadId = call.argument("id");

        fetchInstance.remove(downloadId, download -> {
            result.success(null);
        }, errorFuncForResult(result));
    }

    private void cancel(MethodCall call, Result result) {
        int downloadId = call.argument("id");

        fetchInstance.cancel(downloadId, download -> {
            result.success(null);
        }, errorFuncForResult(result));
    }

    private void pause(MethodCall call, Result result) {
        int downloadId = call.argument("id");

        fetchInstance.pause(downloadId, download -> {
            result.success(null);
        }, errorFuncForResult(result));
    }

    private void resume(MethodCall call, Result result) {
        int downloadId = call.argument("id");

        fetchInstance.resume(downloadId, download -> {
            result.success(null);
        }, errorFuncForResult(result));
    }

    private void enqueue(MethodCall methodCall, Result result) {

        String url = methodCall.argument("url");
        String saveDir = methodCall.argument("saveDir");
        String fileName = methodCall.argument("fileName");
        String title = methodCall.argument("title");
        // creating the request
        Request request = new Request(url, saveDir + "/" + fileName);
        Map<String, String> extras = new HashMap<>();
        extras.put("title",title);
        request.setExtras(new Extras(extras));
        request.setDownloadOnEnqueue(false);
        request.setNetworkType(fetchInstance.getFetchConfiguration().getGlobalNetworkType());
        fetchInstance.enqueue(request, success -> {
            Log.d("Fetchme", "success enqueueing the request!");
            result.success(success.getId());
        }, error -> {
            Log.d("Fetchme", "error   " + error);
            Log.d("Fetchme", request.toString());
        });

        Log.d("Fetchme", "Enqueued the url :" + url);
    }

    private void scheduleBackgroundDownloader() {
        if(workManager == null) {
            Log.d("Fetchme", "初始化Fetch Worker");
            Data inputData = (new Data.Builder()).build();
            Constraints constraints = (new Constraints.Builder())
                    .setRequiresStorageNotLow(true)
                    .setRequiresBatteryNotLow(true)
                    .build();
            Class<FileDownloadWorker> fileDownloadWorkerClass = FileDownloadWorker.class;
            PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(fileDownloadWorkerClass,
                    15, TimeUnit.MINUTES) //CHANGE THIS TIME TO A SHORTER TIME FOR TESTING
                    .setConstraints(constraints)
                    .addTag("FileDownloadWorker")
                    .setInputData(inputData)
                    .build();
            workManager = WorkManager.getInstance(context);
            workManager.enqueue(workRequest);
        }
    }

    private void initialize(MethodCall methodCall, Result result) {

        fetchConfigBuilder = new FetchConfiguration.Builder(this.context)
                .enableLogging(n(methodCall.argument("loggingEnabled"), true))
                .setAutoRetryMaxAttempts(n(methodCall.argument("autoRetryAttempts"), 0))
                .setDownloadConcurrentLimit(n(methodCall.argument("concurrentDownloads"), 3))
                .setProgressReportingInterval(n(methodCall.argument("progressInterval"), 1500))
                .setNotificationManager(notificationManager);
//                .setHttpDownloader(new OkHttpDownloader(okHttpClient));

        fetchInstance = Fetch.Impl.getInstance(fetchConfigBuilder.build());
        Log.d(FetchmePlugin.class.getName(), "Fetchme initialized!");
        fetchInstance.pauseAll();
        result.success(null);
    }

    public void getAllDownloadItems(Result result) {
        fetchInstance.getDownloads(allDownloads -> {
            List<Map<String, Object>> dList = new ArrayList<>();
            for (Download d : allDownloads) {
                dList.add(DownloadItemMapper.mapToDownloadItem(d).toMap());
            }
            result.success(dList);
        });
    }

    public void getDownloadItem(MethodCall call, Result result) {
        Integer id = call.argument("id");
        fetchInstance.getDownload(id, download -> {
            result.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
        });
        result.error("404", "Download with id " + id + " nor found!",
                null);
    }

    public void openDownloadedFile(MethodCall call, Result result) {
        Integer id = call.argument("id");
        Download download;
        fetchInstance.getDownload(id, result1 -> {
            if (result1 == null) {
                result.error("DOWNLOAD_NOT_FOUND", "Downloaded file was not found!", null);
                return;
            }
            String saveFilePath = result1.getFile();
            Intent intent = IntentUtils.validatedFileIntent(context, saveFilePath, null);
            if (intent != null) {
                context.startActivity(intent);
                result.success(true);
            } else {
                result.success(false);
            }
        });

    }

    public void pauseAll(Result result) {
        fetchInstance.pauseAll();
        result.success(null);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        if(fetchListener != null) {
            fetchInstance.removeListener(fetchListener);
        }
        scheduleBackgroundDownloader();
    }

    private <T> T n(T t1, T t2) {
        if (t1 == null) return t2;
        return t1;
    }
}
