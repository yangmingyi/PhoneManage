package com.zhuoxin.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zhuoxin.R;
import com.zhuoxin.base.ActionBarActivity;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener{
    RelativeLayout rl_start;
    RelativeLayout rl_notification;
    RelativeLayout rl_push;
    RelativeLayout rl_help;
    RelativeLayout rl_aboutus;
    ToggleButton tb_start;
    ToggleButton tb_notification;
    ToggleButton tb_push;

    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initActionBar(true, "系统设置", false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
        initData();
        mContext = this;
    }
    public void initView(){
        rl_start = (RelativeLayout) findViewById(R.id.rl_start);
        rl_notification = (RelativeLayout) findViewById(R.id.rl_notification);
        rl_push = (RelativeLayout) findViewById(R.id.rl_push);
        rl_help = (RelativeLayout) findViewById(R.id.rl_help);
        rl_aboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
        tb_start = (ToggleButton) findViewById(R.id.tb_start);
        tb_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //把状态保存到sp中
                getSharedPreferences("config",MODE_PRIVATE).edit().putBoolean("startWhenBootComplete",tb_start.isChecked()).commit();
            }
        });
        tb_notification = (ToggleButton) findViewById(R.id.tb_notification);
        tb_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //状态为选中，显示通知栏消息，未选中时，清空通知栏消息
                if (b){
                    Intent intent = new Intent(SettingsActivity.this,HomeActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification notification = new Notification.Builder(SettingsActivity.this)
                            .setContentTitle("虚假通知")
                            .setContentText("奥巴马请你共进晚餐")
                            .setSmallIcon(R.drawable.item_arrow_right)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.item_arrow_right))
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build();
                    NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(0,notification);
                }else{
                    NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(0);
                }

            }
        });
        tb_push = (ToggleButton) findViewById(R.id.tb_push);

        rl_start.setOnClickListener(this);
        rl_notification.setOnClickListener(this);
        rl_push.setOnClickListener(this);
        rl_help.setOnClickListener(this);
        rl_aboutus.setOnClickListener(this);

    }
    public void initData(){
        boolean startWhenBootComplete = getSharedPreferences("config",MODE_PRIVATE).getBoolean("startWhenBootComplete",false);
        tb_start.setChecked(startWhenBootComplete);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rl_start:
                //启动开机按钮，去设置对应的togglebutton，并且把开机状态进行保存
                tb_start.setChecked(!tb_start.isChecked());
                break;
            case R.id.rl_notification:
                //启动开机按钮，去设置对应的togglebutton，并且把开机状态进行保存
                tb_notification.setChecked(!tb_notification.isChecked());
                break;
            case R.id.rl_push:
                Toast.makeText(this,"后续版本见",Toast.LENGTH_LONG).show();
                break;
            case R.id.rl_help:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromSettings",true);
                startActivity(Guide_Activity.class,bundle);
                break;
            case R.id.rl_aboutus:
                startActivity(AboutActivity.class);
                break;
        }
    }
}
