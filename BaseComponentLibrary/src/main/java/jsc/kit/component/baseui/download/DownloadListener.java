package jsc.kit.component.baseui.download;

import android.net.Uri;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public interface DownloadListener {
    /**
     * Observe downloading progress.
     *
     * @param downloadedBytes downloaded bytes
     * @param totalBytes      total bytes
     * @param downStatus      download status
     */
    public void onDownloadProgress(int downloadedBytes, int totalBytes, int downStatus);

    /**
     * Download successfully.
     *
     * @param uri file uri
     */
    public void onDownloadCompleted(Uri uri);
}
