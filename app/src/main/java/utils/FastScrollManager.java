package utils;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class FastScrollManager extends LinearLayoutManager {
    public FastScrollManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public FastScrollManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FastScrollManager(Context context) {
        super(context);
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position){
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()){
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return FastScrollManager.this.computeScrollVectorForPosition(targetPosition);
            }

            //控制速度。
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return super.calculateSpeedPerPixel(displayMetrics);
            }
            @Override
            protected int calculateTimeForScrolling(int dx) {
                if (dx > 3000) {
                    dx = 3000;
                }

                int time = super.calculateTimeForScrolling(dx);
                return time;
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}