package manage;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决程序退出不完全问题
 */
public class ExitApplication extends Application {
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
        for (Activity activity : list){
            activity.finish();
        }
        System.exit(0);
    }
}