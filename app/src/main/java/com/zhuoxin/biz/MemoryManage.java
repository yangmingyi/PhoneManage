package com.zhuoxin.biz;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;

import com.zhuoxin.process.ProcessManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取运行内存的相关信息
 *
 * @author 杨明义
 * @version 1.0
 * @since 2016年11月21日 10:28:24
 * Created by Administrator on 2016/11/21.
 */

public class MemoryManage {
    /**
     * 获取memoryInfo
     *
     * @param context
     * @return
     */
    private static ActivityManager.MemoryInfo getMemoryInfo(Context context) {
        //获取运行内存大小和当前大小以及剩余大小
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        //创建memoryInfo
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //获取数据，并保存到memoryInfo中
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    /**
     * 获取当前手机的总共运行内存大小，单位为bit
     *
     * @param context 上下文环境
     * @return long类型的运行内存总大小
     */
    public static long tatolRAMLong(Context context) {
        return getMemoryInfo(context).totalMem;
    }
    public static String tatolRAMLongString(Context context) {
        return Formatter.formatFileSize(context,tatolRAMLong(context));
    }
    /**
     * 获取当前手机的可用运行内存大小，单位为bit
     *
     * @param context 上下文环境
     * @return long类型的运行内存可用大小
     */
    public static long availabRAMleLong(Context context) {
        return getMemoryInfo(context).availMem;

    }public static String availabRAMleString(Context context) {
        return Formatter.formatFileSize(context,availabRAMleLong(context));

    }
    /**
     * 获取当前手机的剩余可用运行内存大小，单位为bit
     *
     * @param context 上下文环境
     * @return long类型的运行内存剩余可用大小
     */
    public static long usedRAMLong(Context context){
        return tatolRAMLong(context)-availabRAMleLong(context);
    }
    public static String usedRAMString(Context context){
        return Formatter.formatFileSize(context,usedRAMLong(context));
    }
    public static int usedPercent(Context context){
        return (int) (100*usedRAMLong(context)/tatolRAMLong(context));
    }
    //获取进程
    public static List<ActivityManager.RunningAppProcessInfo> getRunning(Context context){
        List<ActivityManager.RunningAppProcessInfo> rapi = ProcessManager.getRunningAppProcessInfo(context);
        List<ActivityManager.RunningAppProcessInfo> temInfos = new ArrayList<ActivityManager.RunningAppProcessInfo>();
        for (int i = 0;i<rapi.size();i++){
            if(!rapi.get(i).processName.contains("android")){
                Log.e("进程信息",rapi.get(i).processName);
                temInfos.add(rapi.get(i));
            }
        }
        return temInfos;
    }
    //杀死进程
    public static void killRunning(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = getRunning(context);
        for (int i = 0;i<list.size();i++){
            if (!list.get(i).processName.equals(context.getPackageName())){
                am.killBackgroundProcesses(list.get(i).processName);
            }
        }
    }
    //获取内置SD卡路径
    public static String getPhoneInSDCardPath(){
        //获取SD卡的状态，判断是否SD卡
        String sdcardState = Environment.getExternalStorageState();
        //如果有，取出路径
        if (sdcardState.equals(Environment.MEDIA_MOUNTED)){
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            return null;
        }
    }
    //获取外置SD卡路径
    public static String getPhoneOutSDCardPath(){
        //获取SD卡的状态，判断是否有外置SD卡。“SECONDARY_STORAGE”
        Map<String,String>sdMap = System.getenv();
        if(sdMap.containsKey("SECONDARY_STORAGE")){
            //如果有，取出路径
            String paths =sdMap.get("SECONDARY_STORAGE");//取值
            String path = paths.split(":")[0];
            if (path == null){
                return null;
            }else{
                return path;
            }
        }else {
            return null;
        }
    }


}
