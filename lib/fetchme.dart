
import 'dart:async';

import 'package:flutter/services.dart';

class Fetchme {
  static const MethodChannel _channel = MethodChannel('fetchme');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> initialize() async {
    await _channel.invokeMethod("initialize");

  }
}
