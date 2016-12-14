package com.zhuoxin.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/11/11.
 */

public class AppInfo {
    public String appname;
    public Drawable appicon;
    public boolean isSystem;
    public String packageName;
    public String appversion;
    public boolean isDelete;

    public AppInfo(String appname, Drawable appicon, boolean isSystem, String packageName, String appversion,boolean isDelete) {
        this.appname = appname;
        this.appicon = appicon;
        this.isSystem = isSystem;
        this.packageName = packageName;
        this.appversion = appversion;
        //系统软件不能被删除
        if(isSystem){
            this.isDelete=false;
        }else{
            this.isDelete=isDelete;
        }
    }
}
