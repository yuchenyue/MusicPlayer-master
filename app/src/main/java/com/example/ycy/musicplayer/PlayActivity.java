package com.example.ycy.musicplayer;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import entity.Music;
import utils.MusicUtil;

public class PlayActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "PlayActivity";
    ImageView imageView_back, play_background;//返回,中心图片
    ImageView listen_zhizhen, listen_changpian;//唱针，唱片
    ImageView listen_pause, listen_up, listen_next;//暂停，上一首，下一首
    TextView listen_title_tv, Listen_artist_tv;//歌名，歌手
    TextView listen_current, listen_length;//时间，总时长
    SeekBar listen_jindutiao;//进度条
    int positions;
    ObjectAnimator animator, animator_zz, animator_pb;
    List<Music> musics;
    private static boolean state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Log.d(TAG, "PlayActivity绑定服务");
        bindMusicService();
        //接收传值
        state = getIntent().getBooleanExtra("state", false);
        Log.i(TAG, "PlayActivity接收到MainActivity传来的播放状态---" + state);

        positions = getIntent().getIntExtra("po", 0);
        Log.i(TAG, "PlayActivity接收到MainActivity传来的position---" + positions);

        musics = MusicUtil.getmusics(this);
        //绑定ID
        initView();
        onDraw();
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "解绑服务 onPause");
        unbindMusicService();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "解绑服务 onDestroy");
    }
    //黑胶唱片
    public void onDraw() {
        //唱盘
        animator = ObjectAnimator.ofFloat(listen_changpian, "rotation", 0, 360);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.INFINITE);
//        animator.start();
        //唱针
        animator_zz = ObjectAnimator.ofFloat(listen_zhizhen, "rotation", -20, 0);
        listen_zhizhen.setPivotX(0);
        listen_zhizhen.setPivotY(0);
        animator_zz.setDuration(800);
        animator_zz.setInterpolator(new LinearInterpolator());
        //中心图
        animator_pb = ObjectAnimator.ofFloat(play_background, "rotation", 0, 360);
        animator_pb.setDuration(10000);
        animator_pb.setInterpolator(new LinearInterpolator());
        animator_pb.setRepeatCount(ValueAnimator.INFINITE);
        animator_pb.setRepeatMode(ValueAnimator.INFINITE);
//        animator_pb.start();
        Log.i(TAG,"ZZ1---" + state);
        if (state != false){
            Log.i(TAG,"ZZ2---" + state);
//            animator_zz.start();
            animator_pb.start();
        }
    }
    //动态黑胶唱片
    public void onDraw_play() {
        animator.start();
        animator_pb.start();
        animator_zz.start();
    }
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
        listen_jindutiao.setProgress(0);
        listen_jindutiao.setMax(music.getDuration());

        listen_pause = (ImageView) findViewById(R.id.listen_pause);
        listen_up = (ImageView) findViewById(R.id.listen_up);
        listen_next = (ImageView) findViewById(R.id.listen_next);
        listen_pause.setOnClickListener(this);
        listen_up.setOnClickListener(this);
        listen_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.listen_up:
                Log.d(TAG, "点击了上一首");
                change_up();
                if (musicService.isPlaying()) {
                    musicService.up();
                    state = true;
                } else if (musicService.isPause()) {
                    musicService.up();
                    state = true;
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
                    state = false;
                } else {
                    if (musicService.isPause()) {
                        onDraw_play();
                        listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                        musicService.start();
                        state = true;
                    } else {
                        musicService.play(positions);
                        state = true;
                        onDraw_play();
                        listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                    }
                }
                break;
            case R.id.listen_next:
                Log.d(TAG, "点击了下一曲");
                change_next();
                if (musicService.isPlaying()) {
                    musicService.next();
                    state = true;
                } else if (musicService.isPause()) {
                    musicService.next();
                    state = true;
                    onDraw_play();
                    listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                }
                break;
            default:
                break;
        }
    }

    //切换中心图
    public void change_next() {
        int ps;
        if (state = true) {
            ps = musicService.getCurrentPosition();
            if (ps + 1 <= musics.size() - 1) {
                ps = ps + 1;
            } else {
                ps = 0;
            }
            Music music = musics.get(ps);
            Log.i(TAG, "中心图的id---" + ps + "歌名" + music.getSong());
            Bitmap play_backgroundBitmap = MusicUtil.getArtwork(this, music.getId(), music.getAlbum_id(), true, true);
            play_background.setImageBitmap(play_backgroundBitmap);
            listen_title_tv.setText(music.getSong());
            Listen_artist_tv.setText(music.getSonger());
            listen_length.setText(MusicUtil.formatTime(music.getDuration()));
        } else if (state = false) {
            ps = positions;
            if (ps + 1 <= musics.size() - 1) {
                ps = ps + 1;
            } else {
                ps = 0;
            }
            Music music = musics.get(ps);
            Log.i(TAG, "中心图的id---" + ps + "歌名" + music.getSong());
            Bitmap play_backgroundBitmap = MusicUtil.getArtwork(this, music.getId(), music.getAlbum_id(), true, true);
            play_background.setImageBitmap(play_backgroundBitmap);
            listen_title_tv.setText(music.getSong());
            Listen_artist_tv.setText(music.getSonger());
            listen_length.setText(MusicUtil.formatTime(music.getDuration()));
        }
    }

    public void change_up() {//ps 0-11
        int ps = musicService.getCurrentPosition();
        if (ps - 1 >= 0) {
            ps = ps - 1;
        } else {
            ps = musics.size() - 1;
        }
        Music music = musics.get(ps);
        Log.i(TAG, "中心图的id---" + ps + "歌曲ID" + music.getSong());
        Bitmap play_backgroundBitmap = MusicUtil.getArtwork(this, music.getId(), music.getAlbum_id(), true, true);
        play_background.setImageBitmap(play_backgroundBitmap);
        listen_title_tv.setText(music.getSong());
        Listen_artist_tv.setText(music.getSonger());
        listen_length.setText(MusicUtil.formatTime(music.getDuration()));
    }

}
