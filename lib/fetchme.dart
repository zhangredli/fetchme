import 'dart:async';

import 'package:flutter/services.dart';

class Fetchme {
  static const MethodChannel _channel = MethodChannel('fetchme');
  static const EventChannel _eventChannel = EventChannel("net.omidn.fetchme/downloadProgressEventStream");


  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> initialize() async {
    await _channel.invokeMethod("initialize");
  }

  static Future<int> enqueue(
    String url,
    String savedDir,
    String? fileName, {
    bool showNotification = true,
    bool openFileFromNotification = true,
    bool requiresStorageNotLow = true,
  }) async {
    return await _channel.invokeMethod('enqueue', {
      'url': url,
      'saveDir': savedDir,
      'fileName': fileName,
      'showNotification': showNotification,
      'openFileFromNotification': openFileFromNotification,
      'requiresStorageNotLow': requiresStorageNotLow,
    });
  }

  static Stream<dynamic> getUpdateStream() {
    return _eventChannel.receiveBroadcastStream();
  }
}
