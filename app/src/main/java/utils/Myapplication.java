package utils;

import android.app.Application;
import android.content.Context;

import java.util.List;

import entity.NetMusic;

public class MyApplication extends Application {

    public static Boolean getIsWeb() {
        return isWeb;
    }
    public static Boolean getisLoc() {
        return isLoc;
    }

    public static void setIsWeb(Boolean isWeb) {
        MyApplication.isWeb = isWeb;
    }

    public static void setIsLoc(boolean isLoc) {
        MyApplication.isLoc = isLoc;
    }

    private static Boolean isWeb = false;
    private static Boolean isLoc = false;
    private static Context context;

    private static int position;

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

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}