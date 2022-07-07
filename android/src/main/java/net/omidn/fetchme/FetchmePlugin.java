package net.omidn.fetchme;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.Request;

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


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "fetchme");
    channel.setMethodCallHandler(this);
    this.context = flutterPluginBinding.getApplicationContext();

    eventChannel= new EventChannel(flutterPluginBinding.getBinaryMessenger(), "net.omidn.fetchme/downloadProgressEventStream");

    eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
      @Override
      public void onListen(Object arguments, EventChannel.EventSink events) {

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
      initialize(result);
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
    Request request = new Request(url, fileName);

    fetchInstance.enqueue(request, null, null);
    result.success(request.getId());

  }

  private void initialize(@NonNull Result result) {
    FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this.context)
            .enableLogging(true)
            .setAutoRetryMaxAttempts(1)
            .build();
    fetchInstance = Fetch.Impl.getInstance(fetchConfiguration);
    Log.d(FetchmePlugin.class.getName(), "Fetch initialized!");
    result.success(null);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
