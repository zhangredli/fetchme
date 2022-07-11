package net.omidn.fetchme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2core.DownloadBlock;

import java.util.List;

import io.flutter.Log;
import io.flutter.plugin.common.EventChannel;

public class FetchListener implements com.tonyodev.fetch2.FetchListener {
    private EventChannel.EventSink eventSink;

    public FetchListener(EventChannel.EventSink eventChannel) {
        this.eventSink = eventChannel;
        Log.d("Fetchme", "Fetch listener is construcetd!");
    }

    @Override
    public void onAdded(@NonNull Download download) {
        Log.d("Fetchme", "onAdded()");
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
    }

    @Override
    public void onCancelled(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
    }

    @Override
    public void onCompleted(@NonNull Download download) {
        Log.d("Fetchme", "onCompleted()");

        eventSink.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
    }

    @Override
    public void onDeleted(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
    }

    @Override
    public void onDownloadBlockUpdated(@NonNull Download download, @NonNull DownloadBlock downloadBlock, int i) {

    }

    @Override
    public void onError(@NonNull Download download, @NonNull Error error, @Nullable Throwable throwable) {
        Log.d("Fetchme", "onError()");
        eventSink.error(String.valueOf(error.getValue()), error.toString(), DownloadItemMapper.mapToDownloadItem(download).toMap());
    }

    @Override
    public void onPaused(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
    }

    @Override
    public void onProgress(@NonNull Download download, long l, long l1) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
    }

    @Override
    public void onQueued(@NonNull Download download, boolean b) {
        Log.d("Fetchme", "onQueued()");
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
    }

    @Override
    public void onRemoved(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
    }

    @Override
    public void onResumed(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download).toMap());
    }

    @Override
    public void onStarted(@NonNull Download download, @NonNull List<? extends DownloadBlock> list, int i) {
        Log.d("Fetchme", "onStarted()");
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download).toString());
    }

    @Override
    public void onWaitingNetwork(@NonNull Download download) {
//        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }
}
