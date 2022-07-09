package net.omidn.fetchme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2core.DownloadBlock;

import java.util.List;

import io.flutter.plugin.common.EventChannel;

public class FetchListener implements com.tonyodev.fetch2.FetchListener {
    private EventChannel.EventSink eventSink;

    public FetchListener(EventChannel.EventSink eventChannel) {
        this.eventSink = eventChannel;
    }

    @Override
    public void onAdded(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onCancelled(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onCompleted(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onDeleted(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onDownloadBlockUpdated(@NonNull Download download, @NonNull DownloadBlock downloadBlock, int i) {

    }

    @Override
    public void onError(@NonNull Download download, @NonNull Error error, @Nullable Throwable throwable) {
        eventSink.error(String.valueOf(error.getValue()), error.toString(), DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onPaused(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onProgress(@NonNull Download download, long l, long l1) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onQueued(@NonNull Download download, boolean b) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onRemoved(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onResumed(@NonNull Download download) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onStarted(@NonNull Download download, @NonNull List<? extends DownloadBlock> list, int i) {
        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }

    @Override
    public void onWaitingNetwork(@NonNull Download download) {
//        eventSink.success(DownloadItemMapper.mapToDownloadItem(download));
    }
}
