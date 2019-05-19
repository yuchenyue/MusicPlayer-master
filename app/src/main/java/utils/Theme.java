package utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.ycy.musicplayer.MainActivity;

public class Theme {
    public static void theme(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("退出APP？");
        builder.setNeutralButton("残忍退出",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.getInstance().exit(context);
            }
        });
        builder.setPositiveButton("我点错了",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
}