package adapter;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ycy.musicplayer.R;

import java.util.List;

/**
 * Created by Raul_lsj on 2018/3/28.
 */

public class ScrollRightAdapter extends BaseSectionQuickAdapter<ScrollBean, BaseViewHolder> {
    private static final String TAG="ScrollRightAdapter";

    public ScrollRightAdapter(int layoutResId, int sectionHeadResId, List<ScrollBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, ScrollBean item) {
        helper.setText(R.id.right_title, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScrollBean item) {
        final ScrollBean.ScrollItemBean t = item.t;
        helper.setText(R.id.right_text, t.getText());
        helper.setOnClickListener(R.id.right_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,t.getText());
            }
        });
    }
}
