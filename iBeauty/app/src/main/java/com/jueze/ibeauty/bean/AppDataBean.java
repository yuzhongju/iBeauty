package com.jueze.ibeauty.bean;

import android.graphics.drawable.Drawable;

public class AppDataBean {

    private String appName;
    private String packageName;
    private Drawable appIcon;
    private String appSize;
    private int versionCode;
    private String versionName;

    private String firstInstallDate;
    private String lastUpdateDate;

    private int targetSdkVersion;
    private int minSdkVersion;
    private String dataDir;
    private String sourceDir;
    private int uid;
    private int flags;

    private String mainActivity;
    
    public AppDataBean(){
        
    }
    public AppDataBean(String appname, String packageName, Drawable appicon, String appsize, int versioncode, String versionname, String fid, String lud, int tsv, int msv, String datadir, String sourcedir, int uid, int flags, String ma){
        this.appName = appname;
        this.packageName = packageName;
        this.appIcon = appicon;
        this.appSize = appsize;
        this.versionCode = versioncode;
        this.versionName = versionname;
        this.firstInstallDate = fid;
        this.lastUpdateDate = lud;
        this.targetSdkVersion = tsv;
        this.minSdkVersion = msv;
        this.dataDir = datadir;
        this.sourceDir = sourcedir;
        this.uid = uid;
        this.flags = flags;
        this.mainActivity = ma;
        
    }
   

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
    
    public void setAppIcon(Drawable appIcon){
        this.appIcon = appIcon;
    }
    
    public Drawable getAppIcon(){
        return appIcon;
    }
    
    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setFirstInstallDate(String firstInstallDate) {
        this.firstInstallDate = firstInstallDate;
    }

    public String getFirstInstallDate() {
        return firstInstallDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setTargetSdkVersion(int targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setMinSdkVersion(int minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public int getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getFlags() {
        return flags;
    }

    public void setMainActivity(String mainActivity) {
        this.mainActivity = mainActivity;
    }

    public String getMainActivity() {
        return mainActivity;
    }
    
}
