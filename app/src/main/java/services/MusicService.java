package services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

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
    private int currentPosition = 0;//记录播放歌曲位置
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
    public MusicService(){

    }
    public int getCurrentPosition(){
            return currentPosition;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        musics = MusicUtil.getmusics(this);
    }

    //获取当前进度值
    public int getCurrentProgress() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }
    //getDduration获取文件的持续时间
    public int getDuration(){
        return mediaPlayer.getDuration();
    }
    public void seekto(int msec){
        mediaPlayer.seekTo(msec);
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
                currentPosition = position;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //暂停
    public void pause(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPause = true;
        }
    }
    //下一首
    public void next(){
        if (currentPosition >= musics.size()-1){
            currentPosition=0;
        }else{
            currentPosition++;
        }
        play(currentPosition);
    }
    //上一首
    public void up(){
        if (currentPosition-1 < 0){
            currentPosition = musics.size()-1;
        }else{
            currentPosition--;
        }
        play(currentPosition);
    }
    public void start(){
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

}
