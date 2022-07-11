package net.omidn.fetchme;


import com.tonyodev.fetch2.Status;

import java.util.HashMap;
import java.util.Map;

public class DownloadItem {
    private int id;
    private String url;
    private String fileName;
    private long downloaded;
    private long total;
    private Status status;
    private long downloadedBytesPerSecond;
    private boolean startDownloadImmediately;

    public DownloadItem(int id, String url, String fileName, long downloaded, long total, Status status, long downloadedBytesPerSecond, boolean startDownloadImediately) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.downloaded = downloaded;
        this.total = total;
        this.status = status;
        this.downloadedBytesPerSecond = downloadedBytesPerSecond;
        this.startDownloadImmediately = startDownloadImediately;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public long getTotal() {
        return total;
    }

    public Status getStatus() {
        return status;
    }

    public long getDownloadedBytesPerSecond() {
        return downloadedBytesPerSecond;
    }

    public boolean isStartDownloadImmediately() {
        return startDownloadImmediately;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("url", url);
        map.put("fileName", fileName);
        map.put("downloaded", downloaded);
        map.put("total", total);
        map.put("status", status);
        map.put("downloadedBytesPerSecond", downloadedBytesPerSecond);
        map.put("startDownloadImmediately", startDownloadImmediately);

        return map;
    }
}
