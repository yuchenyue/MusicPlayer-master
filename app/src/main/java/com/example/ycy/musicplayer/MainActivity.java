package com.example.ycy.musicplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import adapter.FragmentAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import entity.Music;
import fragment.LetworkFragment;
import fragment.LocalFragment;
import fragment.NetworkFragment;
import manage.ExitApplication;
import manage.FileProviderUtils;
import manage.SystemProgramUtils;
import utils.MusicUtil;
import utils.Theme;

public class MainActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, Popwindow.OnItemClickListener {

    public static final String TAG = "MainActivity";
    private static final int SUBACTIVITY = 4;//子Activity回传标记
    private Popwindow popwindow;
//    //相册请求码
//    private static final int ALBUM_REQUEST_CODE = 1;
//    //相机请求码
//    private static final int CAMERA_REQUEST_CODE = 2;
//    //剪裁请求码
//    private static final int CROP_REQUEST_CODE = 3;
    private File tempFile;
    public Uri cropImageUri;

    public MyReceiver receiver = null;//广播
    int p;
    private static int state = 2;//播放状态
    ViewPager main_viewpager;
    TextView main_musicName, main_author;
    CircleImageView main_image;//底部常驻栏图片
    ImageView main_up;//上一首
    ImageView main_pause_play;//暂停播放
    ImageView main_next;//下一首
    CircleImageView iv_touxiang;

    public RelativeLayout design_bottom_sheet;
    TabLayout tablayout;
    private String[] titles = {"本地音乐","每日推荐歌单", "搜索"};

    TextView tv_name;//侧滑界面昵称
    List<Fragment> fragmentList;
    List<Music> musics;
    private static boolean isExit = false;
    Handler mHandler = new Handler() {
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

        //绑定服务
        bindMusicService();

        //动态注册广播
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("services.MusicService");
        registerReceiver(receiver, filter);
        Log.i(TAG, "MainActivity--create");

        //初始化控件
        initView();
        //创建Fragment集合并添加
        addTabToTabLayout();
        fragmentList = new ArrayList<>();
        fragmentList.add(new LocalFragment());
        fragmentList.add(new LetworkFragment());
        fragmentList.add(new NetworkFragment());
        //设置适配器
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addTitlesAndFragments(titles,fragmentList);
        main_viewpager.setAdapter(fragmentAdapter);
        tablayout.setupWithViewPager(main_viewpager);
//        musics = MusicUtil.getmusics(this);

        //底部弹出框
        popwindow = new Popwindow(this);
        popwindow.setOnItemClickListener(this);
    }

    /**
     * Description：给TabLayout添加tab
     */
    private void addTabToTabLayout() {
        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab().setText(titles[i]));
        }
    }
    //初始化控件
    private void initView() {
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        design_bottom_sheet = (RelativeLayout) findViewById(R.id.design_bottom_sheet);
        main_musicName = (TextView) findViewById(R.id.main_musicName);
        main_author = (TextView) findViewById(R.id.main_author);
        main_viewpager = (ViewPager) findViewById(R.id.main_viewpager);
        main_image = (CircleImageView) findViewById(R.id.main_image);
        main_image.setImageDrawable(getResources().getDrawable(R.drawable.app));
        main_up = (ImageView) findViewById(R.id.main_up);
        main_pause_play = (ImageView) findViewById(R.id.main_pause_play);
        main_next = (ImageView) findViewById(R.id.main_next);
        main_image.setOnClickListener(this);
        main_up.setOnClickListener(this);
        main_pause_play.setOnClickListener(this);
        main_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //主界面上一首键
            case R.id.main_up:
                if (musicService.isPlaying()) {
                    musicService.up();
                    state = 1;
                } else if (musicService.isPause()) {
                    musicService.up();
                    state = 1;
                    main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                }
                Log.d(TAG, "up");
                break;
            //主界面暂停播放键
            case R.id.main_pause_play:
                if (musicService.isPlaying()) {
                    musicService.pause();
                    Log.d(TAG, "pasue");
                    state = 1;
                    main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_stop));
                } else {
                    if (musicService.isPause()) {
                        musicService.start();
                        Log.d(TAG, "play");
                        state = 1;
                        main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                    } else {
                        musicService.play(0);
                        Log.d(TAG, "play0");
                        state = 1;
                        main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                    }
                }
                break;
            //主界面下一首键
            case R.id.main_next:
                if (musicService.isPlaying()) {
                    musicService.next();
                    state = 1;
                } else if (musicService.isPause()) {
                    musicService.next();
                    state = 1;
                    main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                }
                Log.d(TAG, "next");

                break;
            case R.id.main_image:
                Log.d(TAG, "点击了图片");
                Intent intent = new Intent(this, PlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("state", state);
                bundle.putInt("po", p);
                intent.putExtras(bundle);
                Log.i(TAG, "----" + p + state);
                startActivity(intent);
            default:
                break;
        }
    }

    /**
     * 按两次返回键退出程序
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            ExitApplication.getInstance().exit(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindMusicService();
        Log.i(TAG, "MainActivity--destroy");
    }

    /**
     * 侧滑
     *
     * @param
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.robot) {
            //对话机器人
            Intent intent = new Intent(this, RobotActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            //修改个人信息
            Intent intent = new Intent(this, Main2Activity.class);
            startActivityForResult(intent, SUBACTIVITY);
        } else if (id == R.id.nav_manage) {
            //换头像
            popwindow.showAtLocation(MainActivity.this.findViewById(R.id.activity_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            Log.i(TAG, "切换头像");
        } else if (id == R.id.nav_share) {
            Theme.theme(this);
            Toast.makeText(this, "正在开发，等待下一版本···", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "正在开发，等待下一版本···", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 子activity带回的信息更新
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri filtUri;
        File outputFile = new File("/mnt/sdcard/tupian_out.jpg");//裁切后输出的图片
        switch (requestCode) {
            case SUBACTIVITY:
                Log.i(TAG, "SUBACTIVITY");
                if (resultCode == Activity.RESULT_OK) {
                    Uri uriData = data.getData();
                    tv_name = (TextView) findViewById(R.id.tv_name);
                    tv_name.setText(uriData.toString());
                } else if (resultCode == Activity.RESULT_CANCELED) {

                }
                break;
            case SystemProgramUtils.REQUEST_CODE_PAIZHAO:
                //拍照完成，进行图片裁切
                File file = new File("/mnt/sdcard/tupian.jpg");
                filtUri = FileProviderUtils.uriFromFile(MainActivity.this, file);
                SystemProgramUtils.Caiqie(MainActivity.this, filtUri, outputFile);
                break;
            case SystemProgramUtils.REQUEST_CODE_ZHAOPIAN:
                //相册选择图片完毕，进行图片裁切
                if (data == null ||  data.getData()==null) {
                    return;
                }
                filtUri = data.getData();
                SystemProgramUtils.Caiqie(MainActivity.this, filtUri, outputFile);
                break;
            case SystemProgramUtils.REQUEST_CODE_CAIQIE:
                //图片裁切完成，显示裁切后的图片
                try {
                    Uri uri = Uri.fromFile(outputFile);
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    iv_touxiang = (CircleImageView) findViewById(R.id.iv_touxiang);
                    iv_touxiang.setImageBitmap(bitmap);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 切换头像事件
     * @param v
     */
    @Override
    public void setOnItemClick(View v) {
        switch (v.getId()) {
            case R.id.bt_xiangji:
                popwindow.dismiss();
                SystemProgramUtils.paizhao(this,new File("/mnt/sdcard/tupian.jpg"));
                break;
            case R.id.bt_xiangce:
                SystemProgramUtils.zhaopian(this);
                Log.i(TAG, "相册");
                break;
            case R.id.bt_quxiao:
                popwindow.dismiss();
                Log.i(TAG, "取消选择");
                break;
            default:
                break;
        }
    }

    //广播接收器
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            final int position = bundle.getInt("count");
            final int state_s = bundle.getInt("state");
            state = state_s;
            p = position;
            Log.i(TAG, "MainActivity得到的值" + position);
            Log.i(TAG, "MainActivity得道的状态" + state_s);
            if (position != -1) {
                final Music music =  MusicUtil.getmusics(getApplicationContext()).get(position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        main_musicName.setText(music.getSong());
                        main_author.setText(music.getSonger());
                        main_image.setImageBitmap(MusicUtil.getArtwork(context, music.getId(), music.getAlbum_id(), true, false));
                        if (state == 1) {
                            main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                        } else {
                            main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_stop));
                        }

                    }
                });
            }

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("services.MusicService");
        registerReceiver(receiver, filter);
        if (p != -1) {
            p = musicService.getCurrentProgress();
            Log.i(TAG, "MainActivity--runUiThread" + musicService.getCurrentProgress());
            state = musicService.getState();
            Log.i(TAG, "MainActivity--runUiThread" + musicService.getState());

            final Music music = MusicUtil.getmusics(getApplicationContext()).get(p);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "MainActivity--runUiThread---" + p);
                    main_musicName.setText(music.getSong());
                    main_author.setText(music.getSonger());
                    main_image.setImageBitmap(MusicUtil.getArtwork(getApplicationContext(), music.getId(), music.getAlbum_id(), true, false));
                    if (state == 1) {
                        main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                    } else {
                        main_pause_play.setImageDrawable(getResources().getDrawable(R.mipmap.ic_stop));
                    }

                }
            });
        }
        Log.i(TAG, "MainActivity--restart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        unbindMusicService();
        Log.i(TAG, "MainActivity--pause");
    }
}
