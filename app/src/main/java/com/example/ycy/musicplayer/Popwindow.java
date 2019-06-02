package com.example.ycy.musicplayer;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;


public class Popwindow extends PopupWindow implements View.OnClickListener{

    private Context context;
    private View popView;
    private OnItemClickListener mListener;
    public Button btnCamera,btnAlbum,btnCancel;


    public Popwindow(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        popView = inflater.inflate(R.layout.bottom_choose, null);

        btnCamera = popView.findViewById(R.id.bt_xiangji);
        btnAlbum =  popView.findViewById(R.id.bt_xiangce);
        btnCancel = popView.findViewById(R.id.bt_quxiao);

        btnCamera.setOnClickListener(this);
        btnAlbum.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        setContentView(popView);
        this.setAnimationStyle(R.style.popwindow_anim_style);
        this.setTouchable(true);
        this.setFocusable(true);
        this.setOutsideTouchable(true);// 设置允许在外点击消失
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.update();
        ColorDrawable dw = new ColorDrawable(0X50000000);
        this.setBackgroundDrawable(dw);

        popView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = popView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(MainActivity listener) {
        this.mListener = listener;
    }

    public void onClick(View v) {
        if (mListener != null) {
            mListener.setOnItemClick(v);
        }
    }



}