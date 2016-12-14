package com.zhuoxin.entity;

import java.io.File;

/**
 * Created by Administrator on 2016/11/22.
 */

public class FileInfo {
    private File file; //原始文件
    private String iconName; //文件图标资源
    private String fileType; //文件类型
    private boolean isSelect; //添加标记，是否被选择、删除

    public FileInfo(File file, String iconName, String fileType) {
        this.file = file;
        this.iconName = iconName;
        this.fileType = fileType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
