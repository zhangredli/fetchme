package net.omidn.fetchme;

import com.tonyodev.fetch2.Download;

public class DownloadItemMapper {

    public static Object mapToDownloadItem(Download download) {
        return new DownloadItem(download.getId(), download.getUrl(),
                download.getFile(), download.getDownloaded(),
                download.getTotal(), download.getStatus(), download.getDownloadedBytesPerSecond(), download.getDownloadOnEnqueue());
    }

}
