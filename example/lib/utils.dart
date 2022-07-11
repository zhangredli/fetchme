import 'dart:io';
import 'package:android_path_provider/android_path_provider.dart';
import 'package:device_info_plus/device_info_plus.dart';
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';

bool _permissionReady = false;
String localPath = '';

Future<void> prepare() async {
  _permissionReady = await _checkPermission();

  await prepareSaveDir();
}

Future<bool> _checkPermission() async {
  if (Platform.isIOS) return true;

  DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
  AndroidDeviceInfo androidInfo = await deviceInfo.androidInfo;
  if (Platform.isAndroid && androidInfo.version.sdkInt! <= 33) {
    print('requesting permission');
    final status = await Permission.storage.status;
    if (status != PermissionStatus.granted) {
      final result = await Permission.storage.request();
      print("Permission result   " + result.toString());
      if (result == PermissionStatus.granted) {
        return true;
      }
    } else {
      return true;
    }
  } else {
    return true;
  }
  return false;
}

Future<void> prepareSaveDir() async {
  localPath = (await _findLocalPath())!;
  final savedDir = Directory(localPath);
  bool hasExisted = await savedDir.exists();
  if (!hasExisted) {
    try {
      await savedDir.create(recursive: true);
    } catch (e) {
      print("Error creating directory: " + e.toString());
    }
  }
}

Future<String?> _findLocalPath() async {
  var externalStorageDirPath;
  if (Platform.isAndroid) {
    try {
      externalStorageDirPath = await AndroidPathProvider.downloadsPath;
    } catch (e) {
      final directory = await getExternalStorageDirectory();
      externalStorageDirPath = directory?.path;
    }
  } else if (Platform.isIOS) {
    externalStorageDirPath =
        (await getApplicationDocumentsDirectory()).absolute.path;
  }
  return externalStorageDirPath;
}
