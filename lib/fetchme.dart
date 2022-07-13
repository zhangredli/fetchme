import 'dart:async';

import 'package:fetchme/src/models.dart';
import 'package:flutter/services.dart';

class Fetchme {
  static const MethodChannel _channel = MethodChannel('fetchme');
  static const EventChannel _eventChannel =
      EventChannel("net.omidn.fetchme/downloadProgressEventStream");

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> initialize() async {
    await _channel.invokeMethod("initialize");
  }

  static Stream<dynamic> getUpdateStream() {
    return _eventChannel.receiveBroadcastStream();
  }

  static Future<List> getAllDownloadItems() async {
    return await _channel.invokeMethod("getAllDownloadItems");
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

  static Future<void> pause(int id) async {

    try {
      await _channel.invokeMethod('pause', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  static Future<void> resume(int id) async {

    try {
      await _channel.invokeMethod('resume', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }
  static Future<void> cancel(int id) async {

    try {
      await _channel.invokeMethod('cancel', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }
  static Future<void> delete(int id) async {

    try {
      await _channel.invokeMethod('delete', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  static Future<void> retry(int id) async {

    try {
      await _channel.invokeMethod('resume', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }
}
