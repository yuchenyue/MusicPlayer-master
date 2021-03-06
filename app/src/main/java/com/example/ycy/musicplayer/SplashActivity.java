package com.example.ycy.musicplayer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import services.MusicService;
import utils.MyApplication;


public class SplashActivity extends FragmentActivity {

    private static final String TAG = "SplashActivity";
    private Button button_splash;
    private ImageView splash_imageView;
    private boolean InMainActivity = false;
    private static final int START_ACTIVITY = 0x1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
    }



    private void init() {
        //启动服务
        startService(new Intent(this, MusicService.class));
        //点击跳过按钮直接进入主界面
        button_splash = findViewById(R.id.button_splash);
        button_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InMainActivity = true;
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        });
        splash_imageView = findViewById(R.id.splash_imageView);
        handler.sendEmptyMessageDelayed(START_ACTIVITY, 3000);
    }

    /**
     * 延时3s自动进入MainActivity
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //如果InMainActivity == false，则进入MainActivity，为了避免重复进入MainActivity
            if (InMainActivity == false) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case START_ACTIVITY:
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
            }
        }
    };
}
