package services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import entity.NetMusic;
import utils.MusicUtil;
import entity.Music;

/**
 * Created by Administrator on 2019/1/16.
 */

public class MusicService extends Service {

    public static final String TAG = "PlayService";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    public MediaPlayer mediaPlayer;
    List<Music> musics;
//    List<NetMusic.DataBean> netMsuicList;
    public int currentProgress;//歌曲位置
    private static int state = 2;
    public boolean isWeb;
    public boolean isPause = false;


    /**
     * 暂停播放
     *
     * @return
     */
    public boolean isPause() {
        return isPause;
    }

    /**
     * 正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 歌曲位置
     *
     * @return
     */
    public int getCurrentProgress() {
        return currentProgress;
    }

    /**
     * 进度条
     *
     * @param msec
     */
    public void seekToPosition(int msec) {
        mediaPlayer.seekTo(msec);
    }

    /**
     * 播放进度
     *
     * @return
     */
    public int getCurrentPosition() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return mediaPlayer.getCurrentPosition();
        }
        return mediaPlayer.getCurrentPosition();
    }

    public MusicService() {
    }

    /**
     * 将播放状态暴露出去
     *
     * @return
     */
    public int getState() {
        return state;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        isWeb = false;
        musics = MusicUtil.getmusics(this);
        //自动播放下一首
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private MyBinder myBinder = new MyBinder();

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    /**
     * 播放
     *
     * @param position
     */
    public void play(int position) {
        if (position >= 0 && position < musics.size()) {
            Music music = musics.get(position);
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this, Uri.parse(music.getUrl()));
                mediaPlayer.prepare();
                mediaPlayer.start();
                currentProgress = position;
                state = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        chuandi();
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
            state = 2;
        }
        chuandi();
    }

    /**
     * 下一首
     */
    public void next() {
        if (currentProgress >= musics.size() - 1) {
            currentProgress = 0;
        } else {
            currentProgress++;
        }
        play(currentProgress);
        chuandi();
    }

    /**
     * 上一首
     */
    public void up() {
        if (currentProgress - 1 < 0) {
            currentProgress = musics.size() - 1;
        } else {
            currentProgress--;
        }
        play(currentProgress);
    }

    /**
     * 继续
     */
    public void start() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            state = 1;
        }
    }

    /**
     * 当播放改变是发送广播
     */
    public void chuandi() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("count", currentProgress);
        if (mediaPlayer.isPlaying()) {
            state = 1;
            bundle.putInt("state", state);
        } else {
            state = 2;
            bundle.putInt("state", state);
        }
        intent.putExtras(bundle);
        intent.setAction("services.MusicService");
        Log.i(TAG, "Service传出的currentPosition" + currentProgress);
        Log.i(TAG, "Service传出的播放状态" + state);
        sendBroadcast(intent);
    }

    /**
     * 监听网络变化的广播
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d(TAG, "网络状态已经改变");
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    Log.d(TAG, "当前网络名称：" + name);
                    //doSomething()
                } else {
                    Log.d(TAG, "没有可用网络");
                    Toast.makeText(context, "当前无可用网络，请检查网络状态！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


}
