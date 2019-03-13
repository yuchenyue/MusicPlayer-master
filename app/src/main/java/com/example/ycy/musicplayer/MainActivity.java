package com.example.ycy.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import adapter.FragmentAdapter;
import entity.Music;
import entity.NetMusic;
import fragment.LocalFragment;
import fragment.NetworkFragment;
import okhttp3.internal.Util;
import utils.MusicUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener , LocalFragment.CallBackValue, NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";
    private static final int SUBACTIVITY = 1;//子Activity回传标记
    public static final String ACTION_UPDATEUI = "action.updateUI";
    BroadcastReceiver broadcastReceiver;
    int p =0;
    private static boolean state = false;//播放状态
    TextView main_my_music_tv,main_online_music_tv;//本地音乐、在线音乐
    ViewPager main_viewpager;
    TextView main_musicName,main_author;
    ImageView main_image;//底部常驻栏图片
    ImageView main_up;//上一首
    ImageView main_pause_play;//暂停播放
    ImageView main_next;//下一首
    TextView tv_name;//侧滑界面昵称
    List<Fragment> fragmentList;
    List<Music> musics;
    private static boolean isExit = false;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //侧滑
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //动态注册广播


        //初始化控件
        initView();
        main_my_music_tv.setOnClickListener(this);
        main_online_music_tv.setOnClickListener(this);
        //创建Fragment集合并添加
        fragmentList = new ArrayList<>();
        fragmentList.add(new LocalFragment());
        fragmentList.add(new NetworkFragment());
        //设置适配器
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragmentList,musics);
        main_viewpager.setAdapter(fragmentAdapter);
    }
    //初始化控件
    private void initView() {

        main_musicName = (TextView) findViewById(R.id.main_musicName);
        main_author = (TextView) findViewById(R.id.main_author);
        main_my_music_tv = (TextView) findViewById(R.id.main_my_music_tv);
        main_online_music_tv = (TextView) findViewById(R.id.main_online_music_tv);
        main_viewpager = (ViewPager) findViewById(R.id.main_viewpager);
        main_image = (ImageView) findViewById(R.id.main_image);
        main_image.setImageDrawable(getResources().getDrawable(R.drawable.music));
        main_up = (ImageView) findViewById(R.id.main_up);
        main_pause_play = (ImageView) findViewById(R.id.main_pause_play);
        main_next = (ImageView) findViewById(R.id.main_next);
        main_image.setOnClickListener(this);
        main_up.setOnClickListener(this);
        main_pause_play.setOnClickListener(this);
        main_next.setOnClickListener(this);

    }
    //本地、网络选择
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_my_music_tv:
                Log.d(TAG,"本地音乐");
                main_viewpager.setCurrentItem(0);
                break;
            case R.id.main_online_music_tv:
                Log.d(TAG,"网络音乐");
                main_viewpager.setCurrentItem(1);
                break;
            //主界面上一首键
            case R.id.main_up:
                if (musicService.isPlaying()) {
                    musicService.up();
                    state = true;
                } else if (musicService.isPause()) {
                    musicService.up();
                    state = true;
                    main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                }
                Log.d(TAG,"up");
                break;
            //主界面暂停播放键
            case R.id.main_pause_play:
                if (musicService.isPlaying()) {
                    musicService.pause();
                    Log.d(TAG,"pasue");
                    state = false;
                    main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_stop));
                } else {
                    if (musicService.isPause()) {
                        musicService.start();
                        Log.d(TAG,"play");
                        state = true;
                        main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                    } else {
                        musicService.play(0);
                        Log.d(TAG,"play0");
                        state = true;
                        main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                    }
                }
                break;
            //主界面下一首键
            case R.id.main_next:
                if (musicService.isPlaying()) {
                    musicService.next();
                    state = true;
                } else if (musicService.isPause()) {
                    musicService.next();
                    state = true;
                    main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                }
                Log.d(TAG,"next");

                break;
            case R.id.main_image:
                Log.d(TAG,"点击了图片");
                Intent intent = new Intent(this,PlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("state",state);
                bundle.putInt("po",p);
                intent.putExtras(bundle);
                startActivity(intent);
                Log.i(TAG,"----" + p + state);
            default:
                break;
        }
    }


    //按两次返回键退出程序
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }
    private void exit(){
        if (!isExit){
            isExit = true;
            Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0,2000);
        }else{
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    //实现Fragment回调方法，实现与Fragment进行传值
    @Override
    public void SendMessageValue(int strValue) {
        p = strValue;//fragment传来的位置position
        Log.i(TAG,"从LocalFragment点击的item传递过来的position---" + strValue);
    }

    @Override
    public void SendMessageValue(boolean strValue_1) {
        state = strValue_1;
        Log.i(TAG,"从MainActivity传递过来的播放状态---" + strValue_1);
    }


    /**
     * 侧滑
     * @param
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.robot) {
            //对话机器人
            Intent intent = new Intent(this,RobotActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            //修改信息
            Intent intent = new Intent(this,Main2Activity.class);
            startActivityForResult(intent,SUBACTIVITY);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //子activity带回的信息更新
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case SUBACTIVITY:
                Uri uriData = data.getData();
                tv_name = (TextView) findViewById(R.id.tv_name);
                tv_name.setText(uriData.toString());
        }
    }

    //广播接收器

}
