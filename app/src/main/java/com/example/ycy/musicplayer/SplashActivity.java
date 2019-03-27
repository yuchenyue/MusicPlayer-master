package com.example.ycy.musicplayer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import services.MusicService;


public class SplashActivity extends FragmentActivity {

    private static final String TAG = "SplashActivity";
    private Button button_splash;
    private boolean InMainActivity = false;
    private static final int START_ACTIVITY = 0x1;
    private final int mRequestCode = 1;
    String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET};
    List<String> mPermissionList = new ArrayList<>();
    String mPackName = "crazystudy.com.crazystudy";
    AlertDialog mPermissionDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        quanxian();
        //启动服务
        startService(new Intent(this, MusicService.class));



    }

    /**
     * 申请权限
     */
    private void quanxian() {
        mPermissionList.clear();
        try {
            for (int i = 0; i < permissions.length;i++){
                if (ContextCompat.checkSelfPermission(SplashActivity.this,
                        permissions[i])!= PackageManager.PERMISSION_GRANTED ) {
                    mPermissionList.add(permissions[i]);
                    if (mPermissionList.size() > 0){
                        ActivityCompat.requestPermissions(SplashActivity.this,permissions,mRequestCode);
                    }
                }else {
                    init();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(){
        //点击跳过按钮直接进入主界面
        button_splash = findViewById(R.id.button_splash);
        button_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InMainActivity = true;
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
        handler.sendEmptyMessageDelayed(START_ACTIVITY, 3000);
    }

    /**
     * 权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;
        if (mRequestCode == requestCode) {
            for (int i = 0;i<grantResults.length;i++){
                if (grantResults[i] == -1){
                    hasPermissionDismiss=true;
                    break;
                }
            }
        }
        if (hasPermissionDismiss){
            showPermissionDialog();
        }else {
            init();
        }
    }
    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.
                                    ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                            SplashActivity.this.finish();
                        }
                    })
                    .create();
        }
    }
    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
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
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                        break;
                }
            }
        }
    };

}
