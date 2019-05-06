package utils;

import android.app.Application;
import android.content.Context;

import java.util.List;

import entity.LetMusic;
import entity.ListMusic;
import entity.Music;
import entity.NetMusic;

public class MyApplication extends Application {

    public static Boolean getIsWeb() {
        return isWeb;
    }
    public static Boolean getisLoc() {
        return isLoc;
    }
    private static Boolean isWeb = false;
    private static Boolean isLoc = false;
    private static Context context;
    private static int position;

    public static void setIsWeb(Boolean isWeb) {
        MyApplication.isWeb = isWeb;
    }
    public static void setIsLoc(boolean isLoc) {
        MyApplication.isLoc = isLoc;
    }
    public static int getPosition() {
        return position;
    }
    public static void setPosition(int position) {
        MyApplication.position = position;
    }
    public static List<NetMusic.DataBean> getMusicList() {
        return musicList;
    }
    public static void setMusicList(List<NetMusic.DataBean> musicList) {
        MyApplication.musicList = musicList;
    }
    private static List<NetMusic.DataBean> musicList;
    private static List<Music> musics;
    public static List<Music> getMusics() {
        return musics;
    }
    public static void setMusics(List<Music> musics) {
        MyApplication.musics = musics;
    }

    public static List<ListMusic.DataBean.Song> listMusicList;

    public static List<ListMusic.DataBean.Song> getListMusicList() {
        return listMusicList;
    }

    public static void setListMusicList(List<ListMusic.DataBean.Song> listMusicList) {
        MyApplication.listMusicList = listMusicList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}