package net.omidn.fetchme;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DefaultStorageResolver;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FetchmePlugin */
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

    eventChannel= new EventChannel(flutterPluginBinding.getBinaryMessenger(), "net.omidn.fetchme/downloadProgressEventStream");

    eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
      @Override
      public void onListen(Object arguments, EventChannel.EventSink events) {
        updateEventSink = events;
        initialize();
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
      initialize();
    }else if (call.method.equals("enqueue")) {
      enqueue(call, result);
    } else {
      result.notImplemented();
    }
  }

  private void enqueue(MethodCall methodCall, Result result) {

    String url = methodCall.argument("url");
    String saveDir = methodCall.argument("saveDir");
    String fileName = methodCall.argument("fileName");
    boolean showNotification = methodCall.argument("showNotification");
    boolean openFileFromnotification = methodCall.argument("openFileFromNotification");
    boolean requiresStorageNotLow = methodCall.argument("requiresStorageNotLow");

    // creating the request
    Request request = new Request(url, saveDir+"/"+fileName);
    request.setNetworkType(NetworkType.ALL);
    fetchInstance.enqueue(request, success -> {
      Log.d("Fetchme", "success enqueueing the request!");
    }, error -> {
      Log.d("Fetchme", "error   "+ error);
      Log.d("Fetchme", request.toString());
    });

    result.success(request.getId());
    Log.d("Fetchme", "Enqueued the url :"+ url);
  }

  private void initialize() {
    FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this.context)
            .enableLogging(true)
            .setAutoRetryMaxAttempts(1)
            .setDownloadConcurrentLimit(100)
            .build();
    fetchInstance = Fetch.Impl.getInstance(fetchConfiguration);
    fetchInstance.addListener(new FetchListener(updateEventSink));
    Log.d(FetchmePlugin.class.getName(), "Fetch initialized!");
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
