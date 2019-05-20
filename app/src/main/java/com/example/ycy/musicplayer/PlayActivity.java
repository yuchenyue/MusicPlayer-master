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

import com.bumptech.glide.Glide;

import java.util.List;

import entity.ListMusic;
import entity.Music;
import utils.MusicUtil;
import utils.MyApplication;

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
    public PlayReceiver preceiver = null;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        MyApplication.getInstance().addActivity(this);
        bindMusicService();
        //接收传值
        positions = getIntent().getIntExtra("po", 0);

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
            positions = position;
            if (MyApplication.isState() == true) {
                Glide.with(getApplication())
                        .load(R.mipmap.ic_play)
                        .into(listen_pause);
                if (position != -1) {
                    if (MyApplication.getIsLoc() == true) {
                        final Music music = musics.get(position);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listen_title_tv.setText(music.getSong());
                                Listen_artist_tv.setText(music.getSonger());
                                listen_length.setText(MusicUtil.formatTime(music.getDuration()));
                                play_background.setImageBitmap(MusicUtil.getArtwork(context, music.getId(), music.getAlbum_id(), true, false));
                            }
                        });
                    } else if (MyApplication.getIsWeb() == true) {
                        final ListMusic.DataBean.Song listMusic = MyApplication.getListMusicList().get(position);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listen_title_tv.setText(listMusic.getName());
                                Listen_artist_tv.setText(listMusic.getSinger());
                                Glide.with(getApplication())
                                        .load(listMusic.getPic())
                                        .into(play_background);
                            }
                        });
                    }
                }
            } else {
                Glide.with(getApplication())
                        .load(R.mipmap.ic_stop)
                        .into(listen_pause);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        unregisterReceiver(preceiver);
        unbindMusicService();
    }


    public void onDraw_t() {
        if (MyApplication.isState() == true) {
            onDraw();
            onDraw_play();
        } else if (MyApplication.isState() == false) {
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

        //唱片中间专辑图
        play_background = (ImageView) findViewById(R.id.play_background);

        imageView_back = (ImageView) findViewById(R.id.imageView_back);
        imageView_back.setOnClickListener(this);

        listen_zhizhen = (ImageView) findViewById(R.id.listen_zhizhen);
        listen_changpian = (ImageView) findViewById(R.id.listen_changpian);
        //歌名
        listen_title_tv = (TextView) findViewById(R.id.listen_title_tv);
//        listen_title_tv.setText(music.getSong());
        //歌手
        Listen_artist_tv = (TextView) findViewById(R.id.listen_artist_tv);
//        Listen_artist_tv.setText(music.getSonger());
        listen_current = (TextView) findViewById(R.id.listen_current);//目前播放时间
        //歌曲时长
        listen_length = (TextView) findViewById(R.id.listen_length);
//        listen_length.setText(MusicUtil.formatTime(music.getDuration()));
        //进度条
        listen_jindutiao = (SeekBar) findViewById(R.id.listen_jindutiao);
//        listen_jindutiao.setMax(music.getDuration());


        listen_pause = (ImageView) findViewById(R.id.listen_pause);
        if (MyApplication.isState() == true) {
            listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
        } else {
            listen_pause.setImageDrawable(getResources().getDrawable(R.mipmap.ic_stop));
        }
        listen_up = (ImageView) findViewById(R.id.listen_up);
        listen_next = (ImageView) findViewById(R.id.listen_next);

        if (MyApplication.getIsLoc() == true) {
            Music music = musics.get(positions);
            Bitmap play_backgroundBitmap = MusicUtil.getArtwork(this, music.getId(), music.getAlbum_id(), true, false);
            play_background.setImageBitmap(play_backgroundBitmap);
            listen_title_tv.setText(music.getSong());
            Listen_artist_tv.setText(music.getSonger());
            listen_length.setText(MusicUtil.formatTime(music.getDuration()));
            listen_jindutiao.setMax(music.getDuration());
        } else if (MyApplication.getIsWeb() == true) {
            ListMusic.DataBean.Song listMusic = MyApplication.getListMusicList().get(positions);
            listen_title_tv.setText(listMusic.getName());
            Listen_artist_tv.setText(listMusic.getSinger());
            listen_length.setText(SongListActivity.formatTime(listMusic.getTime()));
            listen_jindutiao.setMax(listMusic.getTime());
            Glide.with(getApplicationContext()).load(listMusic.getPic()).into(play_background);
        }
        listen_jindutiao.setOnSeekBarChangeListener(this);
        mHandler.post(mRunnable);

        listen_pause.setOnClickListener(this);
        listen_up.setOnClickListener(this);
        listen_next.setOnClickListener(this);
    }

    /**
     * 进度条
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (MyApplication.getIsLoc()){
                musicService.seekToPosition(seekBar.getProgress());
            }else {
                musicService.seekToPosition(seekBar.getProgress()*1000);
            }

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
            if (MyApplication.getIsLoc() == true) {
                listen_jindutiao.setProgress(musicService.getCurrentPosition());
                listen_current.setText(MusicUtil.formatTime(musicService.getCurrentPosition()));
            } else if (MyApplication.getIsWeb() == true) {
                listen_jindutiao.setProgress(musicService.getCurrentPosition() / 1000);
                listen_current.setText(SongListActivity.formatTime(musicService.getCurrentPosition() / 1000));
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };


    /**
     * 实现上一首、暂停、下一首点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.listen_up:
                change();
                if (musicService.isPlaying()) {
                    musicService.up();
                    MyApplication.setState(true);
                } else if (musicService.isPause()) {
                    musicService.up();
                    MyApplication.setState(true);
                    onDraw_play();
                    Glide.with(getApplication())
                            .load(R.mipmap.ic_play)
                            .into(listen_pause);
                }
                break;
            case R.id.listen_pause:
                if (musicService.isPlaying()) {
                    animator.cancel();
                    animator_zz.reverse();
                    animator_pb.cancel();
                    Glide.with(getApplication())
                            .load(R.mipmap.ic_stop)
                            .into(listen_pause);
                    musicService.pause();
                    MyApplication.setState(false);
                } else {
                    if (musicService.isPause()) {
                        onDraw_play();
                        Glide.with(getApplication())
                                .load(R.mipmap.ic_play)
                                .into(listen_pause);
                        musicService.start();
                        MyApplication.setState(true);
                    }
                }
                break;
            case R.id.listen_next:
                change();
                if (musicService.isPlaying()) {
                    musicService.next();
                    MyApplication.setState(true);
                } else if (musicService.isPause()) {
                    musicService.next();
                    MyApplication.setState(true);
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
        if (MyApplication.getIsLoc() == true) {
            Music music = musics.get(musicService.getCurrentProgress());
            Bitmap play_backgroundBitmap = MusicUtil.getArtwork(this, music.getId(), music.getAlbum_id(), true, true);
            play_background.setImageBitmap(play_backgroundBitmap);
            listen_title_tv.setText(music.getSong());
            Listen_artist_tv.setText(music.getSonger());
            listen_jindutiao.setProgress(0);
            listen_jindutiao.setMax(music.getDuration());
        } else if (MyApplication.getIsWeb() == true) {
            ListMusic.DataBean.Song listMusic = MyApplication.getListMusicList().get(positions);
            listen_title_tv.setText(listMusic.getName());
            Listen_artist_tv.setText(listMusic.getSinger());
            listen_jindutiao.setProgress(0);
            listen_jindutiao.setMax(listMusic.getTime());
            Glide.with(getApplicationContext()).load(listMusic.getPic()).into(play_background);
        }

    }

}
