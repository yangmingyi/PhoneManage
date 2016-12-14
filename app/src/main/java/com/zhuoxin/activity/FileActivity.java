package com.zhuoxin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.zhuoxin.R;
import com.zhuoxin.adapter.FileAdapter;
import com.zhuoxin.base.ActionBarActivity;
import com.zhuoxin.biz.FileManager;
import com.zhuoxin.entity.FileInfo;
import com.zhuoxin.utils.FileTypeUtil;

import java.util.ArrayList;
import java.util.List;

public class FileActivity extends ActionBarActivity {
    String fileType;
    //获取数据
    ListView lv_file;
    FileAdapter fileAdapter;
    List<FileInfo> fileInfoList;
    FileManager fm = FileManager.getFileManager();

    Button btn_file_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        fileType = getIntent().getBundleExtra("bundle").getString("fileType");
        initActionBar(true, fileType, false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lv_file = (ListView) findViewById(R.id.lv_file);
        btn_file_delete = (Button) findViewById(R.id.btn_file_dalete);
        getFileList();
        fileAdapter = new FileAdapter(fileInfoList,this);
        lv_file.setAdapter(fileAdapter);
        lv_file.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mime = FileTypeUtil.getMIMEType(fileInfoList.get(i).getFile());
                //通过隐式跳转打开对应文件
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(fileInfoList.get(i).getFile()),mime);
                startActivity(intent);
            }
        });
        lv_file.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        fileAdapter.isScroll = false;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        fileAdapter.isScroll = true;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        btn_file_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FileInfo> tempList = new ArrayList<FileInfo>();
                tempList.addAll(fileAdapter.getDateList());
                for (FileInfo f : tempList){
                    if (f.isSelect()){
                        fm.getAnyFileList().remove(f);
                        switch (f.getFileType()){
                            case  FileTypeUtil.TYPE_TXT:
                                fm.getTxtFileList().remove(f);
                                break;
                            case  FileTypeUtil.TYPE_VIDEO:
                                fm.getVideoFileList().remove(f);
                                break;
                            case  FileTypeUtil.TYPE_AUDIO:
                                fm.getAudioFileList().remove(f);
                                break;
                            case  FileTypeUtil.TYPE_IMAGE:
                                fm.getImageFileList().remove(f);
                                break;
                            case  FileTypeUtil.TYPE_ZIP:
                                fm.getZipFileList().remove(f);
                                break;
                            case  FileTypeUtil.TYPE_APK:
                                fm.getApkFileList().remove(f);
                                break;
                        }

                    //改变大小
                    long totalSize = fm.getAnyFileSize();
                    fm.setAnyFileSize(totalSize-f.getFile().length());
                    f.getFile().delete();}
                }
                fileAdapter.notifyDataSetChanged();
                Toast.makeText(FileActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFileList(){
        switch (fileType){

            case "文档文件":
                fileInfoList = FileManager.getFileManager().getTxtFileList();
                break;
            case "视频文件":
                fileInfoList = FileManager.getFileManager().getVideoFileList();
                break;
            case "音频文件":
                fileInfoList = FileManager.getFileManager().getAudioFileList();
                break;
            case "图像文件":
                fileInfoList = FileManager.getFileManager().getImageFileList();
                break;
            case "压缩文件":
                fileInfoList = FileManager.getFileManager().getZipFileList();
                break;
            case "apk文件":
                fileInfoList = FileManager.getFileManager().getApkFileList();
                break;
            default:
                fileInfoList = FileManager.getFileManager().getAnyFileList();
                break;


        }
    }
}
