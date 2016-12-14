package com.zhuoxin.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhuoxin.R;
import com.zhuoxin.adapter.SoftwareAdapter;
import com.zhuoxin.base.ActionBarActivity;
import com.zhuoxin.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class SoftwareActivity extends ActionBarActivity {
    List<AppInfo> appInfoList = new ArrayList<AppInfo>();
    SoftwareAdapter adapter;
    ListView ll_software;
    ProgressBar pb_softmgr_loading;
    String appType;
    CheckBox cb_deleteall;
    Button btn_delete;
    //广播接收者
    BroadcastReceiver receiver;
    //Handler
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            //处理逻辑，根据不同的Massage进行处理
            int flag = message.what;
            switch (flag){
                case  1:
                    pb_softmgr_loading.setVisibility(View.GONE);
                    ll_software.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    break;
                case  2:
                    Toast.makeText(SoftwareActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appType = getIntent().getBundleExtra("bundle").getString("appType","all");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        String softwareName = getIntent().getBundleExtra("bundle").getString("softwareName","所有软件");

        initActionBar(true, softwareName, false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();
        //动态创建
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                saveAppInfo();
                adapter.notifyDataSetChanged();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");//协议类型  http  tel
        registerReceiver(receiver,filter);//需要有反注册
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void initView() {
        ll_software = (ListView) findViewById(R.id.ll_software);
        pb_softmgr_loading = (ProgressBar) findViewById(R.id.pb_softmgr_loading);
        cb_deleteall = (CheckBox) findViewById(R.id.cb_deleteall);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        adapter = new SoftwareAdapter(appInfoList, this);
        ll_software.setAdapter(adapter);
        saveAppInfo();//调用保存软件信息的方法
        cb_deleteall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                for (int i =0;i <appInfoList.size();i++){
                    if (appType.equals("all")){
                        if (!appInfoList.get(i).isSystem){
                            appInfoList.get(i).isDelete = isChecked;
                        }
                    }else if (appType.equals("system")){
                        appInfoList.get(i).isDelete = false;
                    }else {
                        appInfoList.get(i).isDelete = isChecked;
                    }
                }
                //把最新数据传给adapterbing刷新
                adapter.notifyDataSetChanged();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //循环取出列表中的App,如果是isDelete,调用删除方法
                for (AppInfo info:appInfoList){
                    if (info.isDelete){
                        if(!info.packageName.equals(getPackageName())){
                            //调用删除方法
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:"+info.packageName));
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }

    private void saveAppInfo() {
        pb_softmgr_loading.setVisibility(View.VISIBLE);
        ll_software.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                appInfoList.clear();
                //获取所有安装包的信息（获得包管理、获得已安装包（包管理的可卸载包和活动包））
                List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES);
                //循环获取所有软件信息
                for (PackageInfo info : packageInfoList) {
                    ApplicationInfo applicationInfo = info.applicationInfo;
                    //创建apptype；
                    boolean isSystem;
                    //判断是否为系统应用（通过位运算完成）
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        isSystem = true;
                    } else {
                        isSystem = false;
                    }
                    //获取应用图标
                    Drawable appicon = getPackageManager().getApplicationIcon(applicationInfo);
                    //获取应用名称
                    String appname = (String) getPackageManager().getApplicationLabel(applicationInfo);
                    String packageName = info.packageName;
                    String appversion = info.versionName;
                    //判断当前页面要展示的数据
                    if (SoftwareActivity.this.appType.equals("all")){
                        AppInfo appInfo = new AppInfo(appname,appicon,isSystem,packageName,appversion,false);
                        appInfoList.add(appInfo);
                    }else if (SoftwareActivity.this.appType.equals("system")){
                        if (isSystem){
                            AppInfo appInfo = new AppInfo(appname,appicon,isSystem,packageName,appversion,false);
                            appInfoList.add(appInfo);
                        }
                    }else{
                        if (!isSystem) {
                            AppInfo appInfo = new AppInfo(appname,appicon,isSystem,packageName,appversion,false);
                            appInfoList.add(appInfo);
                        }
                    }

                }
                //runonUIThread
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                */
                //Handler机制
                Message msg = new Message();
                msg.what=1;//设置标记
                handler.sendMessage(msg);


            }
        }).start();

    }
}
