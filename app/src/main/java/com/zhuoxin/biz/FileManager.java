package com.zhuoxin.biz;

import com.zhuoxin.entity.FileInfo;
import com.zhuoxin.utils.FileTypeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于查找文件，使用单例模式
 * Created by Administrator on 2016/11/22.
 */

public class FileManager {
    //单例设计模式
    private static FileManager fileManager = new FileManager();
    private FileManager(){

    }
    public static FileManager getFileManager(){
        return fileManager;
    }
    //设置内置、外置SD卡路径
    public static File inSDCardDir = null;
    public static File outSDCardDir = null;
    //静态代码块初始化成员变量
    static {
        if (MemoryManage.getPhoneInSDCardPath() !=null){
            inSDCardDir = new File(MemoryManage.getPhoneInSDCardPath());
        }
        if (MemoryManage.getPhoneOutSDCardPath() !=null) {
            outSDCardDir = new File(MemoryManage.getPhoneOutSDCardPath());
        }
    }
    //设置查找状态，判断是否停止
    public boolean isSearching = false;
    //建立所有文件的List，存储查到的文件，文件总大小
    List<FileInfo> anyFileList = new ArrayList<FileInfo>();
    long anyFileSize = 0; //所有文件大小
    List<FileInfo> txtFileList = new ArrayList<FileInfo>();
    long txtFileSize = 0;//文档文件大小
    List<FileInfo> videoFileList = new ArrayList<FileInfo>();
    long videoFileSize = 0;//视频文件大小
    List<FileInfo> audioFileList = new ArrayList<FileInfo>();
    long audioFileSize = 0;//音频文件大小
    List<FileInfo> imageFileList = new ArrayList<FileInfo>();
    long imageFileSize = 0;//图像文件大小
    List<FileInfo> zipFileList = new ArrayList<FileInfo>();
    long zipFileSize = 0;//压缩文件大小
    List<FileInfo> apkFileList = new ArrayList<FileInfo>();
    long apkFileSize = 0;//apk文件大小

    public List<FileInfo> getAnyFileList() {
        return anyFileList;
    }

    public long getAnyFileSize() {
        return anyFileSize;
    }
    public void setAnyFileSize(long size){
        anyFileSize = size;
    }

    public List<FileInfo> getTxtFileList() {
        return txtFileList;
    }

    public long getTxtFileSize() {
        return txtFileSize;
    }

    public List<FileInfo> getVideoFileList() {
        return videoFileList;
    }

    public long getVideoFileSize() {
        return videoFileSize;
    }

    public List<FileInfo> getAudioFileList() {
        return audioFileList;
    }

    public long getAudioFileSize() {
        return audioFileSize;
    }

    public List<FileInfo> getImageFileList() {
        return imageFileList;
    }

    public long getImageFileSize() {
        return imageFileSize;
    }

    public List<FileInfo> getZipFileList() {
        return zipFileList;
    }

    public long getZipFileSize() {
        return zipFileSize;
    }

    public List<FileInfo> getApkFileList() {
        return apkFileList;
    }

    public long getApkFileSize() {
        return apkFileSize;
    }

    private void initData(){
       isSearching = false;
        //建立所有文件的List，存储查到的文件，文件总大小
        anyFileList.clear();
        anyFileSize = 0; //所有文件大小
        txtFileList.clear();
        txtFileSize = 0;//文档文件大小
        videoFileList.clear();
       videoFileSize = 0;//视频文件大小
        audioFileList.clear();
        audioFileSize = 0;//音频文件大小
       imageFileList.clear();
       imageFileSize = 0;//图像文件大小
       zipFileList.clear();
         zipFileSize = 0;//压缩文件大小
        apkFileList.clear();
        apkFileSize = 0;//apk文件大小
    }


    //查找文件
    public void searchSDCardFile(){
        if (isSearching){
            return;
        }else {

            //判断anyFileList是否有数据，清空亲重新查找
            if (anyFileList.size() >0){
                initData();
            }
            // /调用查找方法，分别查找内置、外置SD卡的文件
            searchFile(inSDCardDir,true);
            searchFile(outSDCardDir,false);
        }


    }
    //根据指定文件夹来查找类容
    public void searchFile(File file,boolean endFlag){
        //判断文件是否合法
        if (file == null || !file.canRead() || !file.exists()){
            return;
        }
        //递归查询
        if(file.isDirectory()){
            File files[] = file.listFiles();
            if (files != null || files.length>0){
                for (File f:files){
                    searchFile(f,false);
                }
            }else{
                return;
            }
            //结束的时候调用
            if (endFlag){
                isSearching = false;
                if (listener !=null){
                    listener.end(endFlag);
                }
            }

        }else{
            String iconAndType[] = FileTypeUtil.getFileIconAndTypeName(file);
            FileInfo fileInfo = new FileInfo(file,iconAndType[0],iconAndType[1]);
            anyFileList.add(fileInfo);
            anyFileSize += file.length();
            if (fileInfo.getFileType().equals(FileTypeUtil.TYPE_TXT)){
                txtFileList.add(fileInfo);
                txtFileSize += file.length();
            }else if(fileInfo.getFileType().equals(FileTypeUtil.TYPE_VIDEO)){
                videoFileList.add(fileInfo);
                videoFileSize += file.length();
            }else if (fileInfo.getFileType().equals(FileTypeUtil.TYPE_AUDIO)){
                audioFileList.add(fileInfo);
                audioFileSize += file.length();
            }else if (fileInfo.getFileType().equals(FileTypeUtil.TYPE_IMAGE)){
                imageFileList.add(fileInfo);
                imageFileSize += file.length();
            }else if (fileInfo.getFileType().equals(FileTypeUtil.TYPE_ZIP)){
                zipFileList.add(fileInfo);
                zipFileSize += file.length();
            }else if (fileInfo.getFileType().equals(FileTypeUtil.TYPE_APK)){
                apkFileList.add(fileInfo);
                apkFileSize += file.length();
            }
            //如果有listener，没查找一个文件，就调用它的一个方法，用来更新UI界面
            if (listener!=null){
                listener.searching(anyFileSize);
            }
        }

    }

    //接口回调
    public interface SearchListener{
        void searching(long size);
        void end(boolean endFlag);
    }
    //定义接口对象
    public SearchListener listener = null;
    //定义设置接口的方法
    public void setSearchListener(SearchListener listener){
        this.listener = listener;
    }

}
