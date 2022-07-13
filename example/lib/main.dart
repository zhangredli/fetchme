import 'dart:async';

import 'package:fetchme/fetchme.dart';
import 'package:fetchme_example/utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  List<DownloadItem> itemList = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
    Fetchme.getUpdateStream().listen((event) {
      // print("the event object:" + event);
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await Fetchme.platformVersion ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text('Running on: $_platformVersion\n'),
              ElevatedButton(
                onPressed: () async {
                  // await Fetchme.initialize();
                  await prepare();
                  Fetchme.enqueue(
                      "https://dl.sultanmusic.ir/music/1401/4/2/Music%20Hale.mp3",
                      localPath,
                      "Hale.mp3");
                  itemList = await Fetchme.getAllDownloadItems();
                },
                child: const Text("Click"),
              ),
              Flexible(
                child: ListView.builder(
                  itemCount: itemList.length,
                  itemBuilder: (context, index) {
                    var downloadItem = itemList[index];
                    return Card(
                      elevation: 5,
                      child: Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Text(downloadItem.fileName),
                            SizedBox(height: 3),
                            LinearProgressIndicator(
                              minHeight: 2,
                              value: downloadItem.downloaded /
                                  downloadItem.total,
                            ),
                            Text(downloadItem.status.toString()),
                          ],
                        ),
                      ),
                    );
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
