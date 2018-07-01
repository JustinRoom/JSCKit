package jsc.kit.component.baseui.download;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

public class DownloadEntity {

    private String mimeType;
    private String url;
    private File destinationDirectory;
    /**the path within the external directory, including the destination filename*/
    private String subPath;
    private String title;
    private String desc;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getDestinationDirectory() {
        return destinationDirectory;
    }

    /**
     * Set the local destination for the downloaded file. Must be a file URI to a path on
     * external storage, and the calling application must have the WRITE_EXTERNAL_STORAGE
     * permission.
     * @param destinationDirectory the directory to save downloaded file
     * @see android.app.DownloadManager.Request#setDestinationUri(Uri)
     */
    public void setDestinationDirectory(@NonNull File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
