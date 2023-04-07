library fetchme;

import 'dart:async';
import 'dart:math';

import 'package:fetchme/src/models.dart';
import 'package:flutter/services.dart';

export 'src/models.dart';

class Fetchme {
  static const MethodChannel _channel = MethodChannel('fetchme');
  static const EventChannel _eventChannel =
  EventChannel("net.omidn.fetchme/downloadProgressEventStream");

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> initialize({bool loggingEnabled = true,
    int autoRetryAttemps = 1,
    int concurrentDownloads = 3,
    int progressInterval = 1500}) async {
    await _channel.invokeMethod("initialize", {
      "loggingEnabled": loggingEnabled,
      "autoRetryAttempts": autoRetryAttemps,
      "concurrentDownloads": concurrentDownloads,
      "progressInterval": max(300, progressInterval),
    });
  }

  static Future<void> setSettings({bool loggingEnabled = true,
    int autoRetryAttemps = 1,
    int concurrentDownloads = 3,
    bool onlyWiFi = false,
    int progressInterval = 1500,
    bool onlySendFinishNotification = false}) async {
    await _channel.invokeMethod('setSettings', {
      "onlyWiFi": onlyWiFi,
      "onlySendFinishNotification": onlySendFinishNotification,
      "loggingEnabled": loggingEnabled,
      "autoRetryAttempts": autoRetryAttemps,
      "concurrentDownloads": concurrentDownloads,
      "progressInterval": progressInterval,
    });
  }

  static Stream getUpdateStream() {
    return _eventChannel.receiveBroadcastStream();
  }

  static Future<List<DownloadItem>> getAllDownloadItems() async {
    return (await _channel.invokeMethod("getAllDownloadItems") as List<dynamic>)
        .map((e) {
      return DownloadItem.fromMap(e);
    }).toList();
  }

  static Future<DownloadItem> getDownloadItem({required int id}) async {
    return DownloadItem.fromMap(await _channel.invokeMethod("getDownloadItem"));
  }

  static Future<int> enqueue(String url,
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

  static Future<void> pause({required int id}) async {
    try {
      await _channel.invokeMethod('pause', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  static Future<void> resume({required int id}) async {
    try {
      await _channel.invokeMethod('resume', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  static Future<void> cancel({required int id}) async {
    try {
      await _channel.invokeMethod('cancel', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  static Future<void> delete({required int id}) async {
    try {
      await _channel.invokeMethod('delete', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  static Future<void> remove({required int id}) async {
    try {
      await _channel.invokeMethod('remove', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  static Future<void> retry({required int id}) async {
    try {
      await _channel.invokeMethod('retry', {'id': id});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  static Future<void> pauseAll() async {
    await _channel.invokeMethod("pauseAll");
  }

  static Future<bool> openFile({required int id}) async {
    bool canOpen = await _channel.invokeMethod('openFile', {'id': id});
    return canOpen;
  }

  static Future<DownloadItem> updateLink(
      {required int id, required String newLink }) async {
    print("updating link to : " + newLink);
    return DownloadItem.fromMap(
        await _channel.invokeMethod(
            'updateRequest', {'id': id, 'newUrl': newLink}));
    }
}
