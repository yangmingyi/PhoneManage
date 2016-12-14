package com.zhuoxin.activity;

import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuoxin.R;
import com.zhuoxin.base.ActionBarActivity;
import com.zhuoxin.biz.FileManager;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;


public class FileManagerActivity extends ActionBarActivity {
    @InjectView(R.id.tv_file_manager)
    TextView tv_file_manager;
    @InjectViews({R.id.pb_anyFile,R.id.pb_txtFile,R.id.pb_videoFile,R.id.pb_audioFile,R.id.pb_imageFile,R.id.pb_zipFile,R.id.pb_apkFile})
    List<ProgressBar> pbList;
    @InjectViews({R.id.iv_anyFile,R.id.iv_txtFile,R.id.iv_videoFile,R.id.iv_audioFile,R.id.iv_imageFile,R.id.iv_zipFile,R.id.iv_apkFile})
    List<ImageView> ivList;
    String fileType[] = {"所有文件","文档文件","视频文件","音频文件","图像文件","压缩文件","apk文件"};
    FileManager fileManager;
    Thread fileManagerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manage);
        ButterKnife.inject(this);
        initActionBar(true, "文件管理", false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //获取fileManage，并设置侦听事件
        fileManager = FileManager.getFileManager();
        fileManager.setSearchListener(new FileManager.SearchListener() {
            @Override
            public void searching(final long size) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_file_manager.setText("已找到：" + Formatter.formatFileSize(FileManagerActivity.this, size));
                    }
                });
            }

            @Override
            public void end(final boolean endFlag) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (endFlag) {
                           for (int i = 0; i <pbList.size();i++){
                               final int temp = i ;
                               pbList.get(i).setVisibility(View.GONE);
                               ivList.get(i).setVisibility(View.VISIBLE);
                               ivList.get(i).setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Bundle bundle = new Bundle();
                                       bundle.putString("fileType",fileType[temp]);
                                       startActivity(FileActivity.class,bundle);
                                   }
                               });
                           }
                            Toast.makeText(FileManagerActivity.this, "查找完毕", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });
        asyncSearchCardFile();
    }



    @Override
    protected void onStart() {
        super.onStart();
        tv_file_manager.setText("已找到：" + Formatter.formatFileSize(FileManagerActivity.this, FileManager.getFileManager().getAnyFileSize()));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileManagerThread.interrupt();
        fileManagerThread =null;
        fileManager.isSearching =false;
    }

    private void asyncSearchCardFile() {
        fileManagerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //查询SD卡文件
                fileManager.searchSDCardFile();
            }
        });
        fileManagerThread.start();
    }

}
