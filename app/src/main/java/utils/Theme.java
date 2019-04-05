package utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Theme {
    public static void theme(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请选择主题");
        builder.setSingleChoiceItems(new String[]{"默认","黑暗系"}, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("取消",null);
        builder.show();
    }
}