package manage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
        setFocusable(true);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
    }
    public boolean isFocused(){
        return true;
    }
}