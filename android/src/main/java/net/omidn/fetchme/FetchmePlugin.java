package net.omidn.fetchme;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.Func;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * FetchmePlugin
 */
public class FetchmePlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Context context;
    private Fetch fetchInstance;

    private EventChannel eventChannel;
    private EventChannel.EventSink updateEventSink;


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
                if (fetchInstance.getListenerSet().size() == 0) {
                    fetchInstance.addListener(new FetchListener(updateEventSink));
                }
                events.success("Started emitting events!");
            }

            @Override
            public void onCancel(Object arguments) {

            }
        });
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
        } else if(call.method.equals("remove")){
            remove(call, result);
        } else if (call.method.equals("retry")) {
            retry(call, result);
        } else if (call.method.equals("getAllDownloadItems")) {
            getAllDownloadItems(result);
        } else if (call.method.equals("getDownloadItem")) {
            getDownloadItem(call, result);
        } else {
            result.notImplemented();
        }
    }


    private Func<Error> errorFuncForResult(Result result) {
        return error -> result.error(error.getValue() + "", error.toString(), error.getThrowable());
    }

    private void retry(MethodCall call, Result result) {
        int downloadId = call.argument("id");

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
        boolean showNotification = methodCall.argument("showNotification");
        boolean openFileFromnotification = methodCall.argument("openFileFromNotification");
        boolean requiresStorageNotLow = methodCall.argument("requiresStorageNotLow");

        // creating the request
        Request request = new Request(url, saveDir + "/" + fileName);
        request.setNetworkType(NetworkType.ALL);
        fetchInstance.enqueue(request, success -> {
            Log.d("Fetchme", "success enqueueing the request!");
        }, error -> {
            Log.d("Fetchme", "error   " + error);
            Log.d("Fetchme", request.toString());
        });

        result.success(request.getId());
        Log.d("Fetchme", "Enqueued the url :" + url);
    }

    private void initialize(MethodCall methodCall, Result result) {
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this.context)
                .enableLogging(n(methodCall.argument("loggingEnabled"), true))
                .setAutoRetryMaxAttempts(n(methodCall.argument("autoRetryAttempts"), 1))
                .setDownloadConcurrentLimit(n(methodCall.argument("concurrentDownloads"), 3))
                .setProgressReportingInterval(n(methodCall.argument("progressInterval"), 1500))
                .build();
        fetchInstance = Fetch.Impl.getInstance(fetchConfiguration);
        Log.d(FetchmePlugin.class.getName(), "Fetchme initialized!");
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

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    private <T> T n(T t1, T t2) {
        if (t1 == null) return t2;
        return t1;
    }
}
