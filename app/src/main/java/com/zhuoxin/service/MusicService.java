package com.zhuoxin.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class MusicService extends Service {
   MediaPlayer mediaPlayer = new MediaPlayer();
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AssetFileDescriptor afd=null;
        try {
             afd = getAssets().openFd("yinhua.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
           if(!mediaPlayer.isPlaying()){
               mediaPlayer.prepare();
               mediaPlayer.start();
           }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                afd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
       if(mediaPlayer.isPlaying()){
           mediaPlayer.stop();
       }
        super.onDestroy();
    }
}
