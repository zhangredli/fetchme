class DownloadTaskStatus {
  final int _value;

  const DownloadTaskStatus(int value) : _value = value;

  int get value => _value;

  static DownloadTaskStatus from(int value) => DownloadTaskStatus(value);

  static const none = DownloadTaskStatus(0);
  static const queued = DownloadTaskStatus(1);
  static const running = DownloadTaskStatus(2);
  static const paused = DownloadTaskStatus(3);
  static const complete = DownloadTaskStatus(4);
  static const failed = DownloadTaskStatus(6);
  static const canceled = DownloadTaskStatus(5);

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is DownloadTaskStatus && other._value == _value;
  }

  @override
  int get hashCode => _value.hashCode;

  @override
  String toString() => 'DownloadTaskStatus($_value)';
}

class DownloadItem {
  final int id;
  final String url;
  final String fileName;
  final int downloaded;
  final int total;
  final int etaInMilliSeconds;
  final DownloadTaskStatus status;
  final int downloadedBytesPerSecond;
  final bool startDownloadImmediately;

  DownloadItem(
      {required this.id,
      required this.url,
      required this.fileName,
      required this.downloaded,
      required this.total,
      required this.etaInMilliSeconds,
      required this.status,
      required this.downloadedBytesPerSecond,
      required this.startDownloadImmediately});

  Map<String, Object> toMap() {
    Map<String, Object> map = {};

    map.putIfAbsent('id', () => id);
    map.putIfAbsent("url", () => url);
    map.putIfAbsent("fileName", () => fileName);
    map.putIfAbsent("downloaded", () => downloaded);
    map.putIfAbsent("total", () => total);
    map.putIfAbsent("etaInMilliSeconds", () => etaInMilliSeconds);
    map.putIfAbsent("DownloadTaskStatus", () => status.value);
    map.putIfAbsent("downloadedBytesPerSecond", () => downloadedBytesPerSecond);
    map.putIfAbsent("startDownloadImmediately", () => startDownloadImmediately);

    return map;
  }

  static DownloadItem fromMap(Map<dynamic, dynamic> map) {
    return DownloadItem(
        id: map['id'] as int,
        url: map['url'],
        fileName: map['fileName'],
        downloaded: map['downloaded'],
        total: map['total'] as int,
        etaInMilliSeconds: map['etaInMilliSeconds'] as int,
        status: DownloadTaskStatus.from(map['status'] as int),
        downloadedBytesPerSecond: map['downloadedBytesPerSecond'] as int,
        startDownloadImmediately: map['startDownloadImmediately'] as bool);
  }
}

enum FetchError {
  unknown,
  none,
  fileNotCreated,
  connectionTimedOut,
  unknownHost,
  httpNotFound,
  writePermissionDenied,
  noStorageSpace,
  noNetworkConnection,
  emptyResponseFromServer,
  requestAlreadyExist,
  downloadNotFound,
  fetchDatabaseError,
  requestWithIdAlreadyExist,
  requestWithFilePathAlreadyExist,
  requestNotSuccessful,
  unknownIoError,
  fileNotFound,
  fetchFileServerUrlInvalid,
  invalidContentHash,
  failedToUpdateRequest,
  failedToAddCompletedDownload,
  fetchFileServerInvalidResponse,
  requestDoesNotExist,
  enqueueNotSuccessful,
  completedNotAddedSuccessfully,
  enqueuedRequestsAreNotDistinct,
  failedToRenameIncompleteDownloadFile,
  failedToRenameFile,
  fileAllocationFailed,
  httpConnectionNotAllowed
}

extension _ErrorExt on FetchError {
  FetchError getByCode(int code) {
    switch (code) {
      case -1:
        return FetchError.unknown;
      case 0:
        return FetchError.none;
      case 1:
        return FetchError.fileNotFound;
      case 2:
        return FetchError.connectionTimedOut;
      case 3:
        return FetchError.unknownHost;
      case 4:
        return FetchError.httpNotFound;
      case 5:
        return FetchError.writePermissionDenied;
      case 6:
        return FetchError.noStorageSpace;
      case 7:
        return FetchError.noNetworkConnection;
      case 8:
        return FetchError.emptyResponseFromServer;
      case 9:
        return FetchError.requestAlreadyExist;
      case 10:
        return FetchError.downloadNotFound;
      case 11:
        return FetchError.fetchDatabaseError;
      case 13:
        return FetchError.requestWithIdAlreadyExist;
      case 14:
        return FetchError.requestWithFilePathAlreadyExist;
      case 15:
        return FetchError.requestNotSuccessful;
      case 16:
        return FetchError.unknownIoError;
      case 17:
        return FetchError.fileNotFound;
      case 19:
        return FetchError.fetchFileServerUrlInvalid;
      case 20:
        return FetchError.invalidContentHash;
      case 21:
        return FetchError.failedToUpdateRequest;
      case 22:
        return FetchError.failedToAddCompletedDownload;
      case 23:
        return FetchError.fetchFileServerInvalidResponse;
      case 24:
        return FetchError.requestDoesNotExist;
      case 25:
        return FetchError.enqueueNotSuccessful;
      case 26:
        return FetchError.completedNotAddedSuccessfully;
      case 27:
        return FetchError.enqueuedRequestsAreNotDistinct;
      case 28:
        return FetchError.failedToRenameIncompleteDownloadFile;
      case 29:
        return FetchError.failedToRenameFile;
      case 30:
        return FetchError.fileAllocationFailed;
      case 31:
        return FetchError.httpConnectionNotAllowed;
      default:
        return FetchError.unknown;
    }
  }
}
