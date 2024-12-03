package net.omidn.fetchme;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationCompat.InboxStyle;
import androidx.core.app.NotificationCompat.Style;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.DownloadNotification;
import com.tonyodev.fetch2.DownloadNotification.ActionType;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchNotificationManager;
import com.tonyodev.fetch2.util.NotificationUtilsKt;

import net.omidn.fetchme.R.drawable;
import net.omidn.fetchme.R.string;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(
        mv = {1, 5, 1},
        k = 1,
        d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0010\b\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\b\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u000bH\u0016J\b\u0010\u001a\u001a\u00020\u0018H\u0016J\u0018\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0018\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020 H\u0016J\u0018\u0010!\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u0003H\u0016J\u0010\u0010\"\u001a\u00020\u00142\u0006\u0010#\u001a\u00020$H\u0016J\u0018\u0010%\u001a\u00020\u00142\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010&\u001a\u00020'H\u0002J\u0010\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u0014H&J&\u0010+\u001a\u00020\u001d2\u0006\u0010,\u001a\u00020\u000b2\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00100.2\u0006\u0010\u001f\u001a\u00020 H\u0016J\u0018\u0010/\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010,\u001a\u00020\u000bH\u0017J\b\u00100\u001a\u00020'H\u0016J\u0018\u00101\u001a\u00020\u00142\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u0010H\u0016J\b\u00102\u001a\u00020\u0018H\u0002J\u0010\u00103\u001a\u00020\u00182\u0006\u0010,\u001a\u00020\u000bH\u0016J\u0010\u00104\u001a\u0002052\u0006\u0010#\u001a\u00020$H\u0016J\b\u00106\u001a\u00020\u0018H\u0016J\u0010\u00107\u001a\u0002052\u0006\u0010\u001e\u001a\u00020\u0010H\u0016J\u0010\u00108\u001a\u0002052\u0006\u0010\u001e\u001a\u00020\u0010H\u0016J\b\u00109\u001a\u00020\u0018H\u0016J.\u0010:\u001a\u0002052\u0006\u0010,\u001a\u00020\u000b2\u0006\u0010;\u001a\u00020\u000e2\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00100.2\u0006\u0010\u0002\u001a\u00020\u0003H\u0016J \u0010<\u001a\u00020\u00182\u0006\u0010;\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u0003H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00100\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\u00020\u0014X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016¨\u0006="},
        d2 = {"Lnet/omidn/fetchme/MyFetchNotificationManager;", "Lcom/tonyodev/fetch2/FetchNotificationManager;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "broadcastReceiver", "Landroid/content/BroadcastReceiver;", "getBroadcastReceiver", "()Landroid/content/BroadcastReceiver;", "downloadNotificationExcludeSet", "", "", "downloadNotificationsBuilderMap", "", "Landroidx/core/app/NotificationCompat$Builder;", "downloadNotificationsMap", "Lcom/tonyodev/fetch2/DownloadNotification;", "notificationManager", "Landroid/app/NotificationManager;", "notificationManagerAction", "", "getNotificationManagerAction", "()Ljava/lang/String;", "cancelNotification", "", "notificationId", "cancelOngoingNotifications", "createNotificationChannels", "getActionPendingIntent", "Landroid/app/PendingIntent;", "downloadNotification", "actionType", "Lcom/tonyodev/fetch2/DownloadNotification$ActionType;", "getChannelId", "getDownloadNotificationTitle", "download", "Lcom/tonyodev/fetch2/Download;", "getEtaText", "etaInMilliSeconds", "", "getFetchInstanceForNamespace", "Lcom/tonyodev/fetch2/Fetch;", "namespace", "getGroupActionPendingIntent", "groupId", "downloadNotifications", "", "getNotificationBuilder", "getNotificationTimeOutMillis", "getSubtitleText", "initialize", "notify", "postDownloadUpdate", "", "registerBroadcastReceiver", "shouldCancelNotification", "shouldUpdateNotification", "unregisterBroadcastReceiver", "updateGroupSummaryNotification", "notificationBuilder", "updateNotification", "android.fetchme"}
)
public abstract class MyDefaultFetchNotificationManager implements FetchNotificationManager {
    public static long DEFAULT_NOTIFICATION_TIMEOUT_AFTER_RESET = 15552000000L * 2;

    private static final String EXTRA_NAMESPACE = "com.tonyodev.fetch2.extra.NAMESPACE";
    private static final String EXTRA_DOWNLOAD_ID = "com.tonyodev.fetch2.extra.DOWNLOAD_ID";
    private static final String EXTRA_DOWNLOAD_NOTIFICATIONS = "con.tonyodev.fetch2.extra.DOWNLOAD_NOTIFICATIONS";
    private static final String EXTRA_NOTIFICATION_ID = "com.tonyodev.fetch2.extra.NOTIFICATION_ID";
    private static final String EXTRA_NOTIFICATION_GROUP_ID = "com.tonyodev.fetch2.extra.NOTIFICATION_GROUP_ID";
    private static final String EXTRA_ACTION_TYPE = "com.tonyodev.fetch2.extra.ACTION_TYPE";
    private static final String EXTRA_GROUP_ACTION = "com.tonyodev.fetch2.extra.GROUP_ACTION";

    private static final int ACTION_TYPE_INVALID = -1;
    private static final int ACTION_TYPE_PAUSE = 0;
    private static final int ACTION_TYPE_RESUME = 1;
    private static final int ACTION_TYPE_DELETE = 2;
    private static final int ACTION_TYPE_CANCEL = 4;
    private static final int ACTION_TYPE_RETRY = 5;
    private static final int ACTION_TYPE_PAUSE_ALL = 6;
    private static final int ACTION_TYPE_RESUME_ALL = 7;
    private static final int ACTION_TYPE_CANCEL_ALL = 8;
    private static final int ACTION_TYPE_DELETE_ALL = 9;
    private static final int ACTION_TYPE_RETRY_ALL = 10;

    private final Context context;
    private final NotificationManager notificationManager;
    private final Map<Integer, DownloadNotification> downloadNotificationsMap;
    private final Map<Integer, NotificationCompat.Builder> downloadNotificationsBuilderMap;
    private final Set<Integer> downloadNotificationExcludeSet;
    @NotNull
    private final String notificationManagerAction;

    @NotNull
    public String getNotificationManagerAction() {
        return this.notificationManagerAction;
    }

    @NotNull
    public BroadcastReceiver getBroadcastReceiver() {
        return (BroadcastReceiver) (new BroadcastReceiver() {
            public void onReceive(@Nullable Context context, @Nullable Intent intent) {
                NotificationUtilsKt.onDownloadNotificationActionTriggered(context, intent, (FetchNotificationManager) MyDefaultFetchNotificationManager.this);
            }
        });
    }

    private final void initialize() {
        this.registerBroadcastReceiver();
        this.createNotificationChannels(this.context, this.notificationManager);
    }

    public void registerBroadcastReceiver() {
        this.context.registerReceiver(this.getBroadcastReceiver(), new IntentFilter(this.getNotificationManagerAction()));
    }

    public void unregisterBroadcastReceiver() {
        this.context.unregisterReceiver(this.getBroadcastReceiver());
    }

    public void createNotificationChannels(@NotNull Context context, @NotNull NotificationManager notificationManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(notificationManager, "notificationManager");
        if (VERSION.SDK_INT >= 26) {
            String var10000 = context.getString(string.fetch_notification_default_channel_id);
            Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri…ation_default_channel_id)");
            String channelId = var10000;
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel == null) {
                var10000 = context.getString(string.fetch_notification_default_channel_name);
                Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri…ion_default_channel_name)");
                String channelName = var10000;
                channel = new NotificationChannel(channelId, (CharSequence) channelName, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
        }

    }

    @NotNull
    public String getChannelId(int notificationId, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        String var10000 = context.getString(string.fetch_notification_default_channel_id);
        Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri…ation_default_channel_id)");
        return var10000;
    }

    public boolean updateGroupSummaryNotification(int groupId, @NotNull Builder notificationBuilder, @NotNull List<? extends DownloadNotification> downloadNotifications, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(notificationBuilder, "notificationBuilder");
        Intrinsics.checkNotNullParameter(downloadNotifications, "downloadNotifications");
        Intrinsics.checkNotNullParameter(context, "context");
        InboxStyle style = new InboxStyle();
        Iterator<? extends DownloadNotification> var7 = downloadNotifications.iterator();

        while (var7.hasNext()) {
            DownloadNotification downloadNotification = (DownloadNotification) var7.next();
            String contentTitle = this.getSubtitleText(context, downloadNotification);
            style.addLine((CharSequence) ("" + downloadNotification.getTotal() + ' ' + contentTitle));
        }

        notificationBuilder.setPriority(0)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle((CharSequence) context.getString(string.fetch_notification_default_channel_name))
                .setContentText((CharSequence) "")
                .setStyle((Style) style)
                .setOnlyAlertOnce(true)
                .setGroup(String.valueOf(groupId))
                .setGroupSummary(true);
        return false;
    }

    public void updateNotification(@NotNull Builder notificationBuilder,
                                   @NotNull DownloadNotification downloadNotification, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(notificationBuilder, "notificationBuilder");
        Intrinsics.checkNotNullParameter(downloadNotification, "downloadNotification");
        Intrinsics.checkNotNullParameter(context, "context");
        int smallIcon = downloadNotification.isDownloading() ? android.R.drawable.stat_sys_download : 17301634;
        notificationBuilder.setPriority(0)
                .setSmallIcon(smallIcon)
                .setContentTitle((CharSequence) downloadNotification.getTitle())
                .setContentText((CharSequence) this.getSubtitleText(context, downloadNotification))
                .setOngoing(downloadNotification.isOnGoingNotification())
                .setGroup(String.valueOf(downloadNotification.getGroupId()))
                .setGroupSummary(false);
        if (!downloadNotification.isFailed() && !downloadNotification.isCompleted()) {
            boolean progressIndeterminate = downloadNotification.getProgressIndeterminate();
            int maxProgress = downloadNotification.getProgressIndeterminate() ? 0 : 100;
            int progress = Math.max(downloadNotification.getProgress(), 0);
            notificationBuilder.setProgress(maxProgress, progress, progressIndeterminate);
        } else {
            notificationBuilder.setProgress(0, 0, false);
        }

        if (downloadNotification.isDownloading()) {
            notificationBuilder.setTimeoutAfter(this.getNotificationTimeOutMillis())
                    .addAction(drawable.fetch_notification_pause,
                            (CharSequence) context.getString(string.fetch_notification_download_pause),
                            this.getActionPendingIntent(downloadNotification, ActionType.PAUSE)).addAction(drawable.fetch_notification_cancel,
                    (CharSequence) context.getString(string.fetch_notification_download_cancel),
                    this.getActionPendingIntent(downloadNotification, ActionType.CANCEL));
        } else if (downloadNotification.isPaused()) {
            notificationBuilder.setTimeoutAfter(this.getNotificationTimeOutMillis()).addAction(drawable.fetch_notification_resume, (CharSequence) context.getString(string.fetch_notification_download_resume), this.getActionPendingIntent(downloadNotification, ActionType.RESUME)).addAction(drawable.fetch_notification_cancel, (CharSequence) context.getString(string.fetch_notification_download_cancel), this.getActionPendingIntent(downloadNotification, ActionType.CANCEL));
        } else if (downloadNotification.isQueued()) {
            notificationBuilder.setTimeoutAfter(this.getNotificationTimeOutMillis());
        } else {
            notificationBuilder.setTimeoutAfter(DEFAULT_NOTIFICATION_TIMEOUT_AFTER_RESET);
        }

    }

    @NotNull
    public PendingIntent getActionPendingIntent(@NotNull DownloadNotification downloadNotification, @NotNull ActionType actionType) {
        synchronized (downloadNotificationsMap) {
            Intent intent = new Intent(notificationManagerAction);
            intent.putExtra(EXTRA_NAMESPACE, downloadNotification.getNamespace());
            intent.putExtra(EXTRA_DOWNLOAD_ID, downloadNotification.getNotificationId());
            intent.putExtra(EXTRA_NOTIFICATION_ID, downloadNotification.getNotificationId());
            intent.putExtra(EXTRA_GROUP_ACTION, false);
            intent.putExtra(EXTRA_NOTIFICATION_GROUP_ID, downloadNotification.getGroupId());
            int action;
            switch (actionType) {
                case CANCEL:
                    action = ACTION_TYPE_CANCEL;
                    break;
                case DELETE:
                    action = ACTION_TYPE_DELETE;
                    break;
                case RESUME:
                    action = ACTION_TYPE_RESUME;
                    break;
                case PAUSE:
                    action = ACTION_TYPE_PAUSE;
                    break;
                case RETRY:
                    action = ACTION_TYPE_RETRY;
                    break;
                default:
                    action = ACTION_TYPE_INVALID;
            }
            intent.putExtra(EXTRA_ACTION_TYPE, action);
            if (VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                return PendingIntent.getBroadcast(context, downloadNotification.getNotificationId() + action, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            }
            return PendingIntent.getBroadcast(context, downloadNotification.getNotificationId() + action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

    }

    @NotNull
    public PendingIntent getGroupActionPendingIntent(int groupId, @NotNull List<? extends DownloadNotification> downloadNotifications, @NotNull ActionType actionType) {
        synchronized (downloadNotificationsMap) {
            Intent intent = new Intent(notificationManagerAction);
            intent.putExtra(EXTRA_NOTIFICATION_GROUP_ID, groupId);
            intent.putExtra(EXTRA_DOWNLOAD_NOTIFICATIONS, new ArrayList<DownloadNotification>(downloadNotifications));
            intent.putExtra(EXTRA_GROUP_ACTION, true);
            int action;
            switch (actionType) {
                case CANCEL_ALL:
                    action = ACTION_TYPE_CANCEL_ALL;
                    break;
                case DELETE_ALL:
                    action = ACTION_TYPE_DELETE_ALL;
                    break;
                case RESUME_ALL:
                    action = ACTION_TYPE_RESUME_ALL;
                    break;
                case PAUSE_ALL:
                    action = ACTION_TYPE_PAUSE_ALL;
                    break;
                case RETRY_ALL:
                    action = ACTION_TYPE_RETRY_ALL;
                    break;
                default:
                    action = ACTION_TYPE_INVALID;
            }
            intent.putExtra(EXTRA_ACTION_TYPE, action);
            if (VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                return PendingIntent.getBroadcast(context, groupId + action, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            }
            return PendingIntent.getBroadcast(context, groupId + action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

    }

    public void cancelNotification(int notificationId) {
        synchronized (downloadNotificationsMap) {
            notificationManager.cancel(notificationId);
            downloadNotificationsBuilderMap.remove(notificationId);
            downloadNotificationExcludeSet.remove(notificationId);
            DownloadNotification downloadNotification = downloadNotificationsMap.get(notificationId);
            if (downloadNotification != null) {
                downloadNotificationsMap.remove(notificationId);
                notify(downloadNotification.getGroupId());
            }
        }

    }

    public void cancelOngoingNotifications() {
        synchronized (downloadNotificationsMap) {
            Iterator<DownloadNotification> iterator = downloadNotificationsMap.values().iterator();
            DownloadNotification downloadNotification;
            while (iterator.hasNext()) {
                downloadNotification = iterator.next();
                if (!downloadNotification.isFailed() && !downloadNotification.isCompleted()) {
                    notificationManager.cancel(downloadNotification.getNotificationId());
                    downloadNotificationsBuilderMap.remove(downloadNotification.getNotificationId());
                    downloadNotificationExcludeSet.remove(downloadNotification.getNotificationId());
                    iterator.remove();
                    notify(downloadNotification.getGroupId());
                }
            }
        }

    }

    public void notify(int groupId) {
        synchronized (downloadNotificationsMap) {
            List<DownloadNotification> groupedDownloadNotifications = new ArrayList<>();
            for (DownloadNotification dn : downloadNotificationsMap.values()) {
                if (dn.getGroupId() == groupId) {
                    groupedDownloadNotifications.add(dn);
                }
            }

            NotificationCompat.Builder groupSummaryNotificationBuilder = getNotificationBuilder(groupId, groupId);
            boolean useGroupNotification = updateGroupSummaryNotification(groupId, groupSummaryNotificationBuilder, groupedDownloadNotifications, context);
            int notificationId;
            NotificationCompat.Builder notificationBuilder;
            for (DownloadNotification downloadNotification : groupedDownloadNotifications) {
                if (shouldUpdateNotification(downloadNotification)) {
                    notificationId = downloadNotification.getNotificationId();
                    notificationBuilder = getNotificationBuilder(notificationId, groupId);
                    updateNotification(notificationBuilder, downloadNotification, context);
                    notificationManager.notify(notificationId, notificationBuilder.build());
                    switch (downloadNotification.getStatus()) {

                        case COMPLETED:
                        case FAILED:
                            downloadNotificationExcludeSet.add(downloadNotification.getNotificationId());
                        default:
                    }
                }
            }
            if (useGroupNotification) {
                notificationManager.notify(groupId, groupSummaryNotificationBuilder.build());
            }
        }

    }

    public boolean shouldUpdateNotification(@NotNull DownloadNotification downloadNotification) {
        Intrinsics.checkNotNullParameter(downloadNotification, "downloadNotification");
        return !this.downloadNotificationExcludeSet.contains(downloadNotification.getNotificationId());
    }

    public boolean shouldCancelNotification(@NotNull DownloadNotification downloadNotification) {
        Intrinsics.checkNotNullParameter(downloadNotification, "downloadNotification");
        return downloadNotification.isPaused();
    }

    public boolean postDownloadUpdate(@NotNull Download download) {
        synchronized (downloadNotificationsMap) {
            if (downloadNotificationsMap.size() > 50) {
                downloadNotificationsBuilderMap.clear();
                downloadNotificationsMap.clear();
            }
            DownloadNotification downloadNotification = (DownloadNotification) downloadNotificationsMap.get(download.getId());
            if(downloadNotification == null) downloadNotification = new DownloadNotification();

            downloadNotification.setStatus(download.getStatus());
            downloadNotification.setProgress(download.getProgress());
            downloadNotification.setNotificationId(download.getId());
            downloadNotification.setGroupId(download.getGroup());
            downloadNotification.setEtaInMilliSeconds(download.getEtaInMilliSeconds());
            downloadNotification.setDownloadedBytesPerSecond(download.getDownloadedBytesPerSecond());
            downloadNotification.setTotal(download.getTotal());
            downloadNotification.setDownloaded(download.getDownloaded());
            downloadNotification.setNamespace(download.getNamespace());
            downloadNotification.setTitle(getDownloadNotificationTitle(download));
            downloadNotificationsMap.put(download.getId(), downloadNotification);
            if (downloadNotificationExcludeSet.contains(downloadNotification.getNotificationId())
                    && !downloadNotification.isFailed() && !downloadNotification.isCompleted()) {
                downloadNotificationExcludeSet.remove(downloadNotification.getNotificationId());
            }
            if (downloadNotification.isCancelledNotification() || shouldCancelNotification(downloadNotification)) {
                cancelNotification(downloadNotification.getNotificationId());
            } else {
                notify(download.getGroup());
            }
            return true;
        }
    }

    @SuppressLint({"RestrictedApi"})
    @NotNull
    public Builder getNotificationBuilder(int notificationId, int groupId) {
        synchronized (downloadNotificationsMap) {
            NotificationCompat.Builder notificationBuilder = downloadNotificationsBuilderMap.get(notificationId);
            if (notificationBuilder == null) {

                notificationBuilder = new NotificationCompat.Builder(context, getChannelId(notificationId, context));
            }
            downloadNotificationsBuilderMap.put(notificationId, notificationBuilder);
            notificationBuilder
                    .setGroup("" + notificationId)
                    .setStyle(null)
                    .setProgress(0, 0, false)
                    .setContentTitle(null)
                    .setContentText(null)
                    .setContentIntent(null)
                    .setGroupSummary(false)
                    .setTimeoutAfter(DEFAULT_NOTIFICATION_TIMEOUT_AFTER_RESET)
                    .setOngoing(false)
                    .setGroup(groupId + "")
                    .setOnlyAlertOnce(true)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .mActions.clear();
            return notificationBuilder;
        }

    }

    public long getNotificationTimeOutMillis() {
        return 10000L;
    }

    @NotNull
    public abstract Fetch getFetchInstanceForNamespace(@NotNull String var1);

    @NotNull
    public String getDownloadNotificationTitle(@NotNull Download download) {
        Intrinsics.checkNotNullParameter(download, "download");
        String title = download.getExtras() != null ? download.getExtras().getString("title","") : "";
        if(!title.isEmpty()) {
            return "Quest助手 - 资源下载：" + title;
        }
        String var10000 = download.getFileUri().getLastPathSegment();
        if (var10000 == null) {
            Uri var2 = Uri.parse(download.getUrl());
            Intrinsics.checkNotNullExpressionValue(var2, "Uri.parse(download.url)");
            var10000 = var2.getLastPathSegment();
        }

        if (var10000 == null) {
            var10000 = download.getUrl();
        }

        return var10000;
    }

    @NotNull
    public String getSubtitleText(@NotNull Context context, @NotNull DownloadNotification downloadNotification) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(downloadNotification, "downloadNotification");
        String var10000;
        if (downloadNotification.isCompleted()) {
            var10000 = context.getString(string.fetch_notification_download_complete);
            Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri…cation_download_complete)");
        } else if (downloadNotification.isFailed()) {
            var10000 = context.getString(string.fetch_notification_download_failed);
            Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri…fication_download_failed)");
        } else if (downloadNotification.isPaused()) {
            var10000 = context.getString(string.fetch_notification_download_paused);
            Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri…fication_download_paused)");
        } else if (downloadNotification.isQueued()) {
            var10000 = context.getString(string.fetch_notification_download_starting);
            Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri…cation_download_starting)");
        } else if (downloadNotification.getEtaInMilliSeconds() < 0L) {
            var10000 = context.getString(string.fetch_notification_download_downloading);
            Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri…ion_download_downloading)");
        } else {
            var10000 = "下载进度："+this.getProgress(context,downloadNotification.getTotal(),downloadNotification.getDownloaded()) + "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0速度：" + this.getSpeedText(context,downloadNotification.getDownloadedBytesPerSecond()) + "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0剩余：" + this.getEtaText(context, downloadNotification.getEtaInMilliSeconds());
        }

        return var10000;
    }

    private final String getEtaText(Context context, long etaInMilliSeconds) {
        long seconds = etaInMilliSeconds / (long) 1000;
        long hours = seconds / (long) 3600;
        seconds -= hours * (long) 3600;
        long minutes = seconds / (long) 60;
        seconds -= minutes * (long) 60;
        String var10000;
        if (hours > 0L) {
            var10000 = context.getString(string.fetch_notification_download_eta_hrs, new Object[]{hours, minutes, seconds});
            Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri… hours, minutes, seconds)");
        } else if (minutes > 0L) {
            var10000 = context.getString(string.fetch_notification_download_eta_min, new Object[]{minutes, seconds});
            Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri…ta_min, minutes, seconds)");
        } else {
            var10000 = context.getString(string.fetch_notification_download_eta_sec, new Object[]{seconds});
            Intrinsics.checkNotNullExpressionValue(var10000, "context.getString(R.stri…ownload_eta_sec, seconds)");
        }

        return var10000;
    }

    private final String getSpeedText(Context context, long downloadedBytesPerSecond) {
        long mSpeed = downloadedBytesPerSecond / (long) (1024 * 1024);
        long kSpeed = downloadedBytesPerSecond / (long) 1024;
        String var10000;
        if (mSpeed > 1L) {
            var10000 = String.valueOf(mSpeed) + "MB/s";
        } else if (kSpeed > 1L) {
            var10000 = String.valueOf(kSpeed) + "KB/s";
        } else {
            var10000 = String.valueOf(downloadedBytesPerSecond) + "B/s";;
        }
        return var10000;
    }

    private final String getProgress(Context context, long total,long download) {
        if(total == 0) {
            return "0%";
        }
        int progress = (int)((((double)download) / total) * 100);
        return String.valueOf(progress)+"%";
    }

    public MyDefaultFetchNotificationManager(@NotNull Context context) {
        super();
        Intrinsics.checkNotNullParameter(context, "context");
        Context var10001 = context.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(var10001, "context.applicationContext");
        this.context = var10001;
        Object var3 = context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (var3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type android.app.NotificationManager");
        } else {
            this.notificationManager = (NotificationManager) var3;
            this.downloadNotificationsMap = new LinkedHashMap<>();
            this.downloadNotificationsBuilderMap = new LinkedHashMap<>();
            boolean var2 = false;
            this.downloadNotificationExcludeSet = new LinkedHashSet<>();
            this.notificationManagerAction = "DEFAULT_FETCH2_NOTIFICATION_MANAGER_ACTION_" + System.currentTimeMillis();
            this.initialize();
        }
    }
}
