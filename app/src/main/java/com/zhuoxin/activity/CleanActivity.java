package com.zhuoxin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zhuoxin.R;
import com.zhuoxin.adapter.FileAdapter;
import com.zhuoxin.base.ActionBarActivity;
import com.zhuoxin.biz.MemoryManage;
import com.zhuoxin.db.DBManager;
import com.zhuoxin.entity.FileInfo;
import com.zhuoxin.utils.FileTypeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectViews;
import butterknife.OnClick;

public class CleanActivity extends ActionBarActivity {

    ListView lv_clean;
    //获取数据库路径
    List<String> filePathList = new ArrayList<String>();
    List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
    FileAdapter fileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);
        ButterKnife.inject(this);
        lv_clean = (ListView) findViewById(R.id.lv_clean);
        initActionBar(true, "手机清理", false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        File targetFile = new File(getFilesDir(),"clearpath.db");
        filePathList.addAll(DBManager.getFilePath(targetFile));
        //把文件路径取出，在手机上查找，如果有就显示出来，放入adapter中
        fileAdapter = new FileAdapter(fileInfoList,this);
        for (String path:filePathList){
            //SD卡路径和app路径
            File appFile = new File(MemoryManage.getPhoneInSDCardPath()+path);
            //判断该文件是否存在
            if (appFile.exists()){
                FileInfo fileInfo = new FileInfo(appFile,"icon_file",FileTypeUtil.TYPE_ANY);
                fileAdapter.getDateList().add(fileInfo);
            }
        }
        lv_clean.setAdapter(fileAdapter);
    }
    @OnClick(R.id.btn_clean)
    public void OnClick(View view){
        //获取并克隆一份数据
        List<FileInfo> tempList = new ArrayList<FileInfo>();
        tempList.addAll(fileAdapter.getDateList());
        //从临时数据中读取每一个文件——》是否删除，如果删除，就执行操作
        for (int i = 0; i<tempList.size();i++){
            if (tempList.get(i).isSelect()){
                fileAdapter.getDateList().remove(i);
                deleteFile(tempList.get(i).getFile());

            }
        }
        fileAdapter.notifyDataSetChanged();
    }
    //删除文件夹
    private void deleteFile(File file){
        //如果是文件直接删除
        if (file.isFile()){
            file.delete();
        }else{
            //如果是文件夹，取出数据，判断是否为空
            File files[] = file.listFiles();
            //如果为空直接删除
            if (files != null){
                if (file.length()<=0){
                    file.delete();
                    return;
                }else{
                    //不为空，一次递归
                    for (File f : files){
                        deleteFile(f);
                    }
                }
            }
            file.delete();

        }
    }
}
