package com.zhuoxin.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.BatteryManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuoxin.R;
import com.zhuoxin.base.ActionBarActivity;
import com.zhuoxin.biz.MemoryManage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PhoneStateActivity extends ActionBarActivity {
    //依赖注入ButterKnife
    //通过注解来找控件
    ProgressBar pb_phonestate_battery;
    ImageView iv_phonestate_battery;
    TextView  tv_phonestate_battery;
    TextView  tv_brand1;
    TextView tv_version1;
    TextView tv_cputype;
    TextView tv_cpucore;
    TextView tv_totalram;
    TextView tv_freeram;
    TextView tv_screen;
    TextView tv_camera;
    TextView tv_base;
    TextView tv_root;

    //广播接受者，接受电量
    BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_state);
        initData();
        initActionBar(true, "手机状态", false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
    }
    public void initData(){
        pb_phonestate_battery = (ProgressBar) findViewById(R.id.pb_phonestate_battery);
        iv_phonestate_battery = (ImageView) findViewById(R.id.iv_phonestate_battery);
        tv_phonestate_battery = (TextView) findViewById(R.id.tv_phonestate_battery);
        tv_brand1 = (TextView) findViewById(R.id.tv_brand1);
        tv_version1 = (TextView) findViewById(R.id.tv_version1);
        tv_cputype = (TextView) findViewById(R.id.tv_cputype);
        tv_cpucore = (TextView) findViewById(R.id.tv_cpucore);
        tv_totalram = (TextView) findViewById(R.id.tv_totalram);
        tv_freeram = (TextView) findViewById(R.id.tv_freeram);
        tv_screen = (TextView) findViewById(R.id.tv_screen);
        tv_camera = (TextView) findViewById(R.id.tv_camera);
        tv_base = (TextView) findViewById(R.id.tv_base);
        tv_root = (TextView) findViewById(R.id.tv_root);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,100);
                int percent = (int) (100.0*level/scale);
                if (percent == 100){
                    iv_phonestate_battery.setBackgroundColor(getResources().getColor(R.color.BackgroundColor,null));
                }else{
                    iv_phonestate_battery.setBackgroundColor(getResources().getColor(R.color.PiechartColor,null));

                }
                pb_phonestate_battery.setProgress(percent);
                tv_phonestate_battery.setText(percent+"%");
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,filter);
        //设备名称，系统版本
        tv_brand1.setText("设备名称："+ Build.BRAND);
        tv_version1.setText("系统版本：Android"+Build.VERSION.RELEASE);
        //CPU型号和核心数
        tv_cputype.setText("CPU型号："+getCPUName());
        tv_cpucore.setText("CPU核心数："+getCPUCore());
        //获取运行内存
        tv_totalram.setText("全部内存："+ MemoryManage.tatolRAMLongString(this));
        tv_freeram.setText("获取剩余内存："+ MemoryManage.availabRAMleString(this));
        //获取屏幕和相机分辨率
        tv_screen.setText("屏幕分辨率："+getSreen());
        tv_camera.setText("相机分辨率："+getCamera());
        //基带版本，是否Root
        tv_base.setText("基带版本："+Build.VERSION.INCREMENTAL);
        tv_root.setText("是否Root："+isRoot());

    }

    /**
     * CPU名称
     * @return
     */
    private String getCPUName(){
        FileReader fr=null;
        BufferedReader br=null;
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            //读取文件中的信息，找到modelname，切割获取右侧名字
            String msg = " ";
            while ((msg = br.readLine())!=null){
                if (msg.contains("model name")){
                    return  msg.split(":")[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * CPU核心数
     * @return
     */
    private int getCPUCore(){
        FileReader fr = null;
        BufferedReader br = null;
        int core = 0;
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            //读取文件中的信息，找到modelname，切割获取右侧名字
            String msg = " ";
            while ((msg = br.readLine())!=null){
                if (msg.contains("processor")){
                    core ++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return core;
    }

    /**
     * 获取屏幕分辨率
     * @return
     */
    private String getSreen(){
        WindowManager manager = getWindowManager();
        Display display =manager.getDefaultDisplay();
        Point point =new Point();
        display.getSize(point);
        return  point.y + "*" +point.x;
    }

    /**
     * 获取相机分辨率
     * @return
     */
    private String getCamera(){
        String s =null;
        if (checkSelfPermission(Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED)){
            //如果有权限，获取相机信息
            android.hardware.Camera camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
            //一般情况0位置就是最大尺寸
            s = sizes.get(0).height+"*"+sizes.get(0).width;
            camera.release();
            return s;
        }else{
            requestPermissions(new String[]{Manifest.permission.CAMERA},0);
        }
        return s;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //如果有权限，获取相机信息
            android.hardware.Camera camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
            //一般情况0位置就是最大尺寸
            String s = sizes.get(0).height+"*"+sizes.get(0).width;
            camera.release();
            tv_camera.setText("相机分辨率："+s);
        }else{
            Toast.makeText(PhoneStateActivity.this,"请重新获取权限",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断是否Root
     * @return
     */
    private boolean isRoot(){
        if (new File("/System/bin/su").exists()&&new File("/System/xbin/su").exists()){
            return true;
        }else{
            return false;
        }
    }
}
