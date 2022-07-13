class DownloadTaskStatus {
  final int _value;

  const DownloadTaskStatus(int value) : _value = value;

  int get value => _value;

  static DownloadTaskStatus from(int value) =>
      DownloadTaskStatus(value);

  static const none = DownloadTaskStatus(0);
  static const queued = DownloadTaskStatus(1);
  static const running = DownloadTaskStatus(2);
  static const complete = DownloadTaskStatus(3);
  static const failed = DownloadTaskStatus(4);
  static const canceled = DownloadTaskStatus(5);
  static const paused = DownloadTaskStatus(6);

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
  final DownloadTaskStatus status;
  final int downloadedBytesPerSecond;
  final bool startDownloadImmediately;


  DownloadItem({required this.id, required this.url, required this.fileName, required this.downloaded, required this.total,
      required this.status, required this.downloadedBytesPerSecond,
      required this.startDownloadImmediately});


  Map<String, Object> toMap() {
    Map<String, Object> map = {};

    map.putIfAbsent('id', () => id);
    map.putIfAbsent("url", () => url);
    map.putIfAbsent("fileName", () => fileName);
    map.putIfAbsent("downloaded", () => downloaded);
    map.putIfAbsent("total", () => total);
    map.putIfAbsent("DownloadTaskStatus", () => status.value);
    map.putIfAbsent("downloadedBytesPerSecond", () => downloadedBytesPerSecond);
    map.putIfAbsent("startDownloadImmediately", () => startDownloadImmediately);

    return map;
  }

  static DownloadItem fromMap(Map<dynamic, dynamic > map){
    return DownloadItem(
        id: map['id'] as int,
        url: map['url'],
        fileName: map['fileName'],
        downloaded: map['downloaded'],
        total: map['total'] as int,
        status: DownloadTaskStatus.from(map['status'] as int),
        downloadedBytesPerSecond: map['downloadedBytesPerSecond'] as int,
        startDownloadImmediately: map['startDownloadImmediately'] as bool);
  }
}
