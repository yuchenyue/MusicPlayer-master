package com.example.ycy.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import services.MusicService;


public class SplashActivity extends FragmentActivity {

    private static final String TAG = "SplashActivity";
    private Button button_splash;
    private boolean InMainActivity = false;
    private static final int START_ACTIVITY = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        //权限申请
        quanxian();
        //启动服务
        startService(new Intent(this, MusicService.class));
        button_splash = findViewById(R.id.button_splash);
        button_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InMainActivity = true;
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        });
        handler.sendEmptyMessageDelayed(START_ACTIVITY,3000);
    }
    //申请权限
    private void quanxian() {
        try {
            if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

        }else {
            //回调实现
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //延时自动进入MainActivity
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //如果InMainActivity == false，则进入MainActivity，为了避免重复进入MainActivity
            if (InMainActivity == false) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case START_ACTIVITY:
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                        break;
                }
            }
        }
    };

}
