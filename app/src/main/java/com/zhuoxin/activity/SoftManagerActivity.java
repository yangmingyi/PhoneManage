package com.zhuoxin.activity;



import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuoxin.R;
import com.zhuoxin.base.ActionBarActivity;
import com.zhuoxin.view.PiechartView;

import java.io.File;


public class SoftManagerActivity extends ActionBarActivity implements View.OnClickListener{
    PiechartView pv_softmgr;
    ProgressBar pb_softmgr;
    TextView tv_softmgr;
    RelativeLayout rl_allsoftware;
    RelativeLayout rl_systemsoftware;
    RelativeLayout rl_usersoftware;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        initView();
        onRequestPerssmissionAndShowMenory();
        initActionBar(true, "软件信息", false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    public void initView(){
        pv_softmgr = (PiechartView) findViewById(R.id.pv_softmgr);
        pb_softmgr = (ProgressBar) findViewById(R.id.pb_softmgr);
        tv_softmgr = (TextView) findViewById(R.id.tv_softmgr);
        rl_allsoftware = (RelativeLayout) findViewById(R.id.rl_allsoftware);
        rl_systemsoftware = (RelativeLayout) findViewById(R.id.rl_systemsoftware);
        rl_usersoftware = (RelativeLayout) findViewById(R.id.rl_usersoftware);
        //设置单机事件
        rl_allsoftware.setOnClickListener(this);
        rl_systemsoftware.setOnClickListener(this);
        rl_usersoftware.setOnClickListener(this);

    }
    public void onRequestPerssmissionAndShowMenory(){
        //动态权限申请
        int permissionState = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionState == PackageManager.PERMISSION_GRANTED){
            //获取数据后展示信息
            pv_softmgr.showPeichart(250);
        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //获取数据后展示信息
            showMemory();
        }else{
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("权限获取").setMessage("请跳转到哦设置界面手动分配权限").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:"+getPackageName()));
                    startActivity(intent);

                }
            }).setNegativeButton("CEMCEL",null).create();
            dialog.show();

        }
    }
    private void showMemory(){
        //获取手机中的SD卡
        File file = Environment.getExternalStorageDirectory();
        ///获取总大小和已用大小
        long total = file.getTotalSpace();
        long used = total-file.getFreeSpace();
        //把数据展示出来
        int angle = (int) (360.0*used/total);
        pv_softmgr.showPeichart(angle);
        pb_softmgr.setProgress((int)(100*used/total));
        String totalStr = Formatter.formatFileSize(this,total);
        String freeStr = Formatter.formatFileSize(this,file.getFreeSpace());
        tv_softmgr.setText("可用空间："+freeStr+"/"+totalStr);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Bundle bundle =new Bundle();
        switch (id){

            case R.id.rl_allsoftware:
                bundle.putString("appType","all");
                bundle.putString("softwareName","所有软件");
                startActivity(SoftwareActivity.class,bundle);
                break;
            case R.id.rl_systemsoftware:
                bundle.putString("appType","system");
                bundle.putString("softwareName","系统软件");
                startActivity(SoftwareActivity.class,bundle);
                break;
            case R.id.rl_usersoftware:
                bundle.putString("appType","user");
                bundle.putString("softwareName","用户软件");
                startActivity(SoftwareActivity.class,bundle);
                break;
        }
    }
}
