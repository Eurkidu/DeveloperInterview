package com.allenliu.versionchecklib.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.allenliu.versionchecklib.core.http.HttpHeaders;
import com.allenliu.versionchecklib.core.http.HttpParams;
import com.allenliu.versionchecklib.core.http.HttpRequestMethod;
import com.allenliu.versionchecklib.utils.FileHelper;


/**
 * Created by allenliu on 2017/8/15.
 */

public class VersionParams implements Parcelable {
    private String requestUrl;
    private String apkName;
    public boolean onlyDownload = false;
    private String downloadAPKPath;
    private com.allenliu.versionchecklib.core.http.HttpHeaders httpHeaders;
    private long pauseRequestTime;
    private HttpRequestMethod requestMethod;
    private HttpParams requestParams;
    private Class<? extends VersionDialogActivity> customDownloadActivityClass;
    //    public boolean isForceUpdate;
    public boolean isForceRedownload;
    public boolean isSilentDownload;
    private Class<? extends AVersionService> service;

    private VersionParams() {
    }

    private VersionParams(String requestUrl, String downloadAPKPath, HttpHeaders httpHeaders, long pauseRequestTime, HttpRequestMethod requestMethod, HttpParams requestParams, Class customDownloadActivityClass, boolean isForceRedownload, boolean isSilentDownload, Class<? extends AVersionService> service) {
        this.requestUrl = requestUrl;
        this.downloadAPKPath = downloadAPKPath;
        this.httpHeaders = httpHeaders;
        this.pauseRequestTime = pauseRequestTime;
        this.requestMethod = requestMethod;
        this.requestParams = requestParams;
        this.customDownloadActivityClass = customDownloadActivityClass;
//        this.isForceUpdate = isForceUpdate;
        this.isForceRedownload = isForceRedownload;
        this.isSilentDownload = isSilentDownload;
        this.service = service;
        if (this.service == null) {
            throw new RuntimeException("you must define your service which extends AVService.");
        }
        if (requestUrl == null) {
            throw new RuntimeException("requestUrl is needed.");
        }
    }

    public Class<? extends AVersionService> getService() {
        return service;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getApkName() {
        return apkName;
    }

    public String getDownloadAPKPath() {
        return downloadAPKPath;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public long getPauseRequestTime() {
        return pauseRequestTime;
    }

    public HttpRequestMethod getRequestMethod() {
        return requestMethod;
    }

    public HttpParams getRequestParams() {
        return requestParams;
    }

    public Class getCustomDownloadActivityClass() {
        return customDownloadActivityClass;
    }

//    public boolean isForceUpdate() {
//        return isForceUpdate;
//    }

    public boolean isForceRedownload() {
        return isForceRedownload;
    }

    public boolean isSilentDownload() {
        return isSilentDownload;
    }

    public static class Builder {

        VersionParams params;

        public Builder() {
            params = new VersionParams();
            params.downloadAPKPath = FileHelper.getDownloadApkCachePath();
            params.pauseRequestTime = 1000 * 30;
            params.requestMethod = HttpRequestMethod.GET;
            params.customDownloadActivityClass = VersionDialogActivity.class;
//            this.isForceUpdate = false;
            params.isForceRedownload = false;
            params.isSilentDownload = false;
        }

        public Builder setRequestUrl(String requestUrl) {
            params.requestUrl = requestUrl;
            return this;
        }

        public Builder setDownloadAPKPath(String downloadAPKPath) {
            params.downloadAPKPath = downloadAPKPath;
            return this;
        }

        public Builder setHttpHeaders(HttpHeaders httpHeaders) {
            params.httpHeaders = httpHeaders;
            return this;
        }

        public Builder setOnlyDownload(boolean download) {
            params.onlyDownload = download;
            return this;
        }

        public Builder setPauseRequestTime(long pauseRequestTime) {
            params.pauseRequestTime = pauseRequestTime;
            return this;
        }

        public Builder setRequestMethod(HttpRequestMethod requestMethod) {
            params.requestMethod = requestMethod;
            return this;
        }

        public Builder setRequestParams(HttpParams requestParams) {
            params.requestParams = requestParams;
            return this;
        }

        public Builder setApkName(String apkName) {
            params.apkName = apkName;
            return this;
        }

        public Builder setCustomDownloadActivityClass(Class customDownloadActivityClass) {
            params.customDownloadActivityClass = customDownloadActivityClass;
            return this;
        }

//        public Builder setForceUpdate(boolean forceUpdate) {
//            isForceUpdate = forceUpdate;
//            return this;
//        }

        public Builder setForceRedownload(boolean forceRedownload) {
            params.isForceRedownload = forceRedownload;
            return this;
        }

        public Builder setSilentDownload(boolean silentDownload) {
            params.isSilentDownload = silentDownload;
            return this;
        }

        public Builder setService(Class<? extends AVersionService> service) {
            params.service = service;
            return this;
        }

        public VersionParams build() {
            return params;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.requestUrl);
        dest.writeString(this.apkName);
        dest.writeByte(this.onlyDownload ? (byte) 1 : (byte) 0);
        dest.writeString(this.downloadAPKPath);
        dest.writeSerializable(this.httpHeaders);
        dest.writeLong(this.pauseRequestTime);
        dest.writeInt(this.requestMethod == null ? -1 : this.requestMethod.ordinal());
        dest.writeSerializable(this.requestParams);
        dest.writeSerializable(this.customDownloadActivityClass);
        dest.writeByte(this.isForceRedownload ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSilentDownload ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.service);
    }

    protected VersionParams(Parcel in) {
        this.requestUrl = in.readString();
        this.apkName = in.readString();
        this.onlyDownload = in.readByte() != 0;
        this.downloadAPKPath = in.readString();
        this.httpHeaders = (HttpHeaders) in.readSerializable();
        this.pauseRequestTime = in.readLong();
        int tmpRequestMethod = in.readInt();
        this.requestMethod = tmpRequestMethod == -1 ? null : HttpRequestMethod.values()[tmpRequestMethod];
        this.requestParams = (HttpParams) in.readSerializable();
        this.customDownloadActivityClass = (Class<? extends VersionDialogActivity>) in.readSerializable();
        this.isForceRedownload = in.readByte() != 0;
        this.isSilentDownload = in.readByte() != 0;
        this.service = (Class<? extends AVersionService>) in.readSerializable();
    }

    public static final Creator<VersionParams> CREATOR = new Creator<VersionParams>() {
        @Override
        public VersionParams createFromParcel(Parcel source) {
            return new VersionParams(source);
        }

        @Override
        public VersionParams[] newArray(int size) {
            return new VersionParams[size];
        }
    };
}
