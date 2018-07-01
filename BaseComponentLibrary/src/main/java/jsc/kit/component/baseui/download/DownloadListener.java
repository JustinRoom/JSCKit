package jsc.kit.component.baseui.download;

import android.net.Uri;

/**
 * @author jiangshicheng
 */
public interface DownloadListener {
    /**
     * Observe downloading progress.
     *
     * @param downloadedBytes downloaded bytes
     * @param totalBytes total bytes
     * @param downStatus download status
     */
    public void onDownloadProgress(int downloadedBytes, int totalBytes, int downStatus);

    /**
     * Download successfully.
     *
     * @param uri file uri
     */
    public void onDownloadCompleted(Uri uri);
}
