package services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.ycy.musicplayer.MainActivity;

import java.io.IOException;
import java.util.List;

import utils.MusicUtil;
import entity.Music;

/**
 * Created by Administrator on 2019/1/16.
 */

public class MusicService extends Service {

    public static final String TAG = "PlayService";
    public MediaPlayer mediaPlayer;
    List<Music> musics;
    public int currentProgress;//歌曲位置
    private static int state = 2;
    public boolean isPause = false;
    public boolean isPause(){
        return isPause;
    }
    public boolean isPlaying(){
        if (mediaPlayer != null){
            return mediaPlayer.isPlaying();
        }
        return false;
    }
    //歌曲位置
    public int getCurrentProgress(){
        return currentProgress;
    }

    public void seekToPosition(int msec){
        mediaPlayer.seekTo(msec);
    }
    //播放进度
    public int getCurrentPosition(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public MusicService(){
    }

    public int getState(){
        return state;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        musics = MusicUtil.getmusics(this);
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
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
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
    public class MyBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }



    //播放
    public void play(int position){
        if (position >= 0 && position < musics.size()){
            Music music = musics.get(position);
            try{
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


    //暂停
    public void pause(){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
            state = 2;
        }
        chuandi();
    }
    //下一首
    public void next(){
        if (currentProgress >= musics.size()-1){
            currentProgress=0;
        }else{
            currentProgress++;
        }
        play(currentProgress);
        chuandi();
    }
    //上一首
    public void up(){
        if (currentProgress-1 < 0){
            currentProgress = musics.size()-1;
        }else{
            currentProgress--;
        }
        play(currentProgress);
    }
    public void start(){
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            state = 1;
        }
    }

    public void chuandi(){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("count",currentProgress);
        if (mediaPlayer.isPlaying()){
            state = 1;
            bundle.putInt("state",state);
        }else {
            state = 2;
            bundle.putInt("state",state);
        }
        intent.putExtras(bundle);
        intent.setAction("services.MusicService");
        Log.i(TAG,"Service传出的currentPosition" + currentProgress);
        Log.i(TAG,"Service传出的播放状态" + state);
        sendBroadcast(intent);
    }

}
