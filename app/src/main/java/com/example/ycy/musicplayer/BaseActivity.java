package com.example.ycy.musicplayer;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;

import services.MusicService;

/**
 * Created by Administrator on 2019/1/17.
 */

public class BaseActivity extends AppCompatActivity {

    public static boolean isBound = false;
    public MusicService musicService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //绑定Service
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
            musicService = myBinder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isBound = false;
        }
    };
    //绑定服务
    public void bindMusicService(){
        if (!isBound){
            Intent intent = new Intent(this,MusicService.class);
            bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE);
            isBound = true;
        }
    }
    //解绑服务
    public void unbindMusicService(){
        if (isBound){
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    public static int getScreenWidth(Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
    public static int getScreenHeight(Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

}
