package net.omidn.fetchme;

import com.tonyodev.fetch2.Download;

import java.net.InterfaceAddress;

public class DownloadItemMapper {

    public static DownloadItem mapToDownloadItem(Download download) {
        return new DownloadItem(download.getId(), download.getUrl(),
                download.getFile(), download.getDownloaded(),
                download.getTotal(), download.getStatus(), download.getDownloadedBytesPerSecond(), download.getDownloadOnEnqueue());
    }

}
