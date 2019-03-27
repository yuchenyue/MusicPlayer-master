package com.example.ycy.musicplayer;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import entity.Music;
import utils.MusicUtil;

public class PlayActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final String TAG = "PlayActivity";
    ImageView imageView_back, play_background;//返回,中心图片
    ImageView listen_zhizhen, listen_changpian;//唱针，唱片
    ImageView listen_pause, listen_up, listen_next;//暂停，上一首，下一首
    TextView listen_title_tv, Listen_artist_tv;//歌名，歌手
    TextView listen_current, listen_length;//时间，总时长
    SeekBar listen_jindutiao;//进度条
    int positions = 0;
    ObjectAnimator animator, animator_zz, animator_pb;
    List<Music> musics;
    private static int state = 2;
    public PlayReceiver preceiver = null;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Log.d(TAG, "PlayActivity绑定服务");
        bindMusicService();
        //接收传值
        state = getIntent().getIntExtra("state", 2);
        Log.i(TAG, "PlayActivity接收到MainActivity传来的播放状态---" + state);
//        positions = getIntent().getIntExtra("po", 0);
        Log.i(TAG, "PlayActivity接收到MainActivity传来的position---" + positions);

        //动态注册广播
        preceiver = new PlayReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("services.MusicService");
        registerReceiver(preceiver, filter);

        musics = MusicUtil.getmusics(this);
        //绑定ID
        initView();
        onDraw_t();
    }

    /**
     * 广播接收器 更新UI
     */
    private class PlayReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            final int position = bundle.getInt("count");
            final int state_s = bundle.getInt("state");
            state = state_s;
            positions = position;
            Log.i(TAG, "PlayActivity得到的值" + position);
            Log.i(TAG, "PlayActivity得道的状态" + state_s);
            if (position != -1) {
                final Music music = musics.get(position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listen_title_tv.setText(music.getSong());
                        Listen_artist_tv.setText(music.getSonger());
                        listen_length.setText(MusicUtil.formatTime(music.getDuration()));
                        play_background.setImageBitmap(MusicUtil.getArtwork(context, music.getId(), music.getAlbum_id(), true, false));
                        if (state == 1) {
                            listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                        } else {
                            listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_stop));
                        }
                    }
                });
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "解绑服务 onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "解绑服务 onDestroy");
        mHandler.removeCallbacks(mRunnable);
        unregisterReceiver(preceiver);
        unbindMusicService();
    }


    public void onDraw_t(){
        if (state == 1) {
            Log.i(TAG,"动画"+ state);
            onDraw();
            onDraw_play();
        }else if (state == 2){
            onDraw();
        }
    }

    /**
     * 黑胶唱片
     */
    public void onDraw() {
        //唱盘
        animator = ObjectAnimator.ofFloat(listen_changpian, "rotation", 0, 360);
        animator.setDuration(15000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.INFINITE);
        //唱针
        animator_zz = ObjectAnimator.ofFloat(listen_zhizhen, "rotation", 0, 20);
        listen_zhizhen.setPivotX(0);
        listen_zhizhen.setPivotY(0);
        animator_zz.setDuration(500);
        animator_zz.setInterpolator(new LinearInterpolator());
        //中心图
        animator_pb = ObjectAnimator.ofFloat(play_background, "rotation", 0, 360);
        animator_pb.setDuration(15000);
        animator_pb.setInterpolator(new LinearInterpolator());
        animator_pb.setRepeatCount(ValueAnimator.INFINITE);
        animator_pb.setRepeatMode(ValueAnimator.INFINITE);

    }

    /**
     * 动态黑胶唱片
     */
    public void onDraw_play() {
        animator.start();
        animator_pb.start();
        animator_zz.start();
    }

    /**
     * 实例化组件
     */
    private void initView() {
        Music music = musics.get(positions);
        //唱片中间专辑图
        play_background = (ImageView) findViewById(R.id.play_background);
        Bitmap play_backgroundBitmap = MusicUtil.getArtwork(this, music.getId(), music.getAlbum_id(), true, false);
        play_background.setImageBitmap(play_backgroundBitmap);

        imageView_back = (ImageView) findViewById(R.id.imageView_back);
        imageView_back.setOnClickListener(this);

        listen_zhizhen = (ImageView) findViewById(R.id.listen_zhizhen);
        listen_changpian = (ImageView) findViewById(R.id.listen_changpian);
        //歌名
        listen_title_tv = (TextView) findViewById(R.id.listen_title_tv);
        listen_title_tv.setText(music.getSong());
        //歌手
        Listen_artist_tv = (TextView) findViewById(R.id.listen_artist_tv);
        Listen_artist_tv.setText(music.getSonger());
        listen_current = (TextView) findViewById(R.id.listen_current);//目前播放时间
        //歌曲时长
        listen_length = (TextView) findViewById(R.id.listen_length);
        listen_length.setText(MusicUtil.formatTime(music.getDuration()));
        //进度条
        listen_jindutiao = (SeekBar) findViewById(R.id.listen_jindutiao);
        listen_jindutiao.setMax(music.getDuration());
        listen_jindutiao.setOnSeekBarChangeListener(this);
        mHandler.post(mRunnable);

        listen_pause = (ImageView) findViewById(R.id.listen_pause);
        if (state == 1) {
            listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
        } else {
            listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_stop));
        }
        listen_up = (ImageView) findViewById(R.id.listen_up);
        listen_next = (ImageView) findViewById(R.id.listen_next);
        listen_pause.setOnClickListener(this);
        listen_up.setOnClickListener(this);
        listen_next.setOnClickListener(this);
    }

    /**
     * 进度条
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            musicService.seekToPosition(seekBar.getProgress());
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            listen_jindutiao.setProgress(musicService.getCurrentPosition());
            listen_current.setText(MusicUtil.formatTime(musicService.getCurrentPosition()));
            mHandler.postDelayed(mRunnable, 1000);
            Log.i(TAG, "当前时间---" + MusicUtil.formatTime(musicService.getCurrentPosition()));
        }
    };


    /**
     * 实现上一首、暂停、下一首点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.listen_up:
                Log.d(TAG, "点击了上一首");
                change();
                if (musicService.isPlaying()) {
                    musicService.up();
                    state = 1;
                } else if (musicService.isPause()) {
                    musicService.up();
                    state = 1;
                    onDraw_play();
                    listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                }
                break;
            case R.id.listen_pause:
                if (musicService.isPlaying()) {
                    animator.cancel();
                    animator_zz.reverse();
                    animator_pb.cancel();
                    listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_stop));
                    musicService.pause();
                    state = 2;
                } else {
                    if (musicService.isPause()) {
                        onDraw_play();
                        listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                        musicService.start();
                        state = 1;
                    } else {
                        musicService.play(positions);
                        state = 1;
                        onDraw_play();
                        listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                    }
                }
                break;
            case R.id.listen_next:
                Log.d(TAG, "点击了下一曲");
                change();
                if (musicService.isPlaying()) {
                    musicService.next();
                    state = 1;
                } else if (musicService.isPause()) {
                    musicService.next();
                    state = 1;
                    onDraw_play();
                    listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 中心旋转图随着歌曲切换而切换
     */
    public void change() {
        Music music = musics.get(musicService.getCurrentProgress());
        Log.i(TAG, "中心图的id---" + musicService.getCurrentProgress() + "歌曲ID" + music.getSong());
        Bitmap play_backgroundBitmap = MusicUtil.getArtwork(this, music.getId(), music.getAlbum_id(), true, true);
        play_background.setImageBitmap(play_backgroundBitmap);
        listen_title_tv.setText(music.getSong());
        Listen_artist_tv.setText(music.getSonger());
        listen_jindutiao.setProgress(0);
        listen_jindutiao.setMax(music.getDuration());
    }

}
