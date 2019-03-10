package com.example.ycy.musicplayer;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import adapter.FragmentAdapter;
import entity.Music;
import fragment.LocalFragment;
import fragment.NetworkFragment;
import utils.MusicUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener , LocalFragment.CallBackValue, NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "MainActivity";

    int p =0;
    private static boolean state = false;//播放状态
    TextView main_my_music_tv,main_online_music_tv;
    ViewPager main_viewpager;
    ImageView main_image,main_up,main_pause_play,main_next;

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
//                change();
                break;
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
//                change();
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

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
