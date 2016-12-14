package com.zhuoxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zhuoxin.adapter.SoftwareAdapter;
import com.zhuoxin.service.MusicService;

public class RebootCompelteReceiver extends BroadcastReceiver {
    //接收到广播后的操作
    @Override
    public void onReceive(Context context, Intent intent) {
        //只有选中了开机选项时，才会执行以下操作
        boolean start = context.getSharedPreferences("config",context.MODE_APPEND).getBoolean("startWhenBootComplete",false);
        if (start){
            Toast.makeText(context,"重启成功",Toast.LENGTH_LONG).show();
            Intent musicIntent = new Intent();
            musicIntent.setClass(context, MusicService.class);
            context.startService(musicIntent);
        }

    }
}
