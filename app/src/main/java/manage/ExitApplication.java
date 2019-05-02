package manage;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import services.MusicService;

/**
 * 解决程序退出不完全问题
 */
public class ExitApplication extends Application {
    MusicService musicService;
    private List<Activity> list = new ArrayList<Activity>();
    private static ExitApplication exit;
    private ExitApplication(){

    }
    public static ExitApplication getInstance(){
        if (null == exit){
            exit = new ExitApplication();
        }
        return exit;
    }
    public void addActivity(Activity activity){
        list.add(activity);
    }
    public void exit(Context context){
        musicService.stop();
        Intent intent = new Intent(this,MusicService.class);
        stopService(intent);
        for (Activity activity : list){
            activity.finish();
        }
        System.exit(0);
    }
}