package fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.ycy.musicplayer.MainActivity;
import com.example.ycy.musicplayer.MoreSongSheetActivity;
import com.example.ycy.musicplayer.R;
import com.example.ycy.musicplayer.SongListActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.NewRecyclerViewAdapter;
import adapter.ScrollBean;
import adapter.ScrollLeftAdapter;
import adapter.ScrollRightAdapter;
import entity.LetMusic;
import retrofit2.Call;
import retrofit2.Response;
import serviceApi.Api;
import utils.FastScrollManager;
import utils.HttpUtil;
import utils.MyApplication;

/**
 * Created by Administrator on 2019/1/14.
 */

public class otherFragment extends Fragment {

    private static final String TAG = "otherFragment";
    MainActivity mainActivity;

    private RecyclerView recLeft;
    private RecyclerView recRight;
    private TextView rightTitle,right_text;
    private List<String> left;
    private List<ScrollBean> right;
    private ScrollLeftAdapter leftAdapter;
    private ScrollRightAdapter rightAdapter;
    //右侧title在数据中所对应的position集合
    private List<Integer> tPosition = new ArrayList<>();
    private Context mContext;
    //title的高度
    private int tHeight;
    //记录右侧当前可见的第一个item的position
    private int first = 0;
    private GridLayoutManager rightManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = this.getContext();
        mainActivity = (MainActivity) this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        Log.d(TAG,"otherFragment:onCreateView");
        mContext = this.getContext();
        recLeft = view.findViewById(R.id.rec_left);
        recRight = view.findViewById(R.id.rec_right);
        rightTitle = view.findViewById(R.id.right_title);

        initData();

        initLeft();
        initRight();

        return view;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"otherFragment:onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG,"otherFragment:onDestroyView");
        super.onDestroyView();
    }

    private void initRight() {

        rightManager = new GridLayoutManager(mContext, 3);

        if (rightAdapter == null) {
            rightAdapter = new ScrollRightAdapter(R.layout.scroll_right, R.layout.layout_right_title, null);
            recRight.setLayoutManager(rightManager);
            recRight.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.set(dpToPx(mContext, getDimens(mContext, R.dimen.dp3))
                            , 0
                            , dpToPx(mContext, getDimens(mContext, R.dimen.dp3))
                            , dpToPx(mContext, getDimens(mContext, R.dimen.dp3)));
                }
            });
            recRight.addOnItemTouchListener(new OnItemChildClickListener() {
                @Override
                public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                }

                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    super.onItemChildClick(adapter,view,position);
                    right_text = view.findViewById(R.id.right_text);
                    String s = (String) right_text.getText();
                    switch (view.getId()){
                        case R.id.right_text:
                            Intent intent = new Intent(getContext(), MoreSongSheetActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("s",s);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            Log.d(TAG,"11"+ s);
                            break;
                    }
                }
            });
            recRight.setAdapter(rightAdapter);
        } else {
            rightAdapter.notifyDataSetChanged();
        }

        rightAdapter.setNewData(right);

        //设置右侧初始title
        if (right.get(first).isHeader) {
            rightTitle.setText(right.get(first).header);
        }

        recRight.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取右侧title的高度
                tHeight = rightTitle.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //判断如果是header
                if (right.get(first).isHeader) {
                    //获取此组名item的view
                    View view = rightManager.findViewByPosition(first);
                    if (view != null) {
                        //如果此组名item顶部和父容器顶部距离大于等于title的高度,则设置偏移量
                        if (view.getTop() >= tHeight) {
                            rightTitle.setY(view.getTop() - tHeight);
                        } else {
                            //否则不设置
                            rightTitle.setY(0);
                        }
                    }
                }

                //因为每次滑动之后,右侧列表中可见的第一个item的position肯定会改变,并且右侧列表中可见的第一个item的position变换了之后,
                //才有可能改变右侧title的值,所以这个方法内的逻辑在右侧可见的第一个item的position改变之后一定会执行
                int firstPosition = rightManager.findFirstVisibleItemPosition();
                if (first != firstPosition && firstPosition >= 0) {
                    //给first赋值
                    first = firstPosition;
                    //不设置Y轴的偏移量
                    rightTitle.setY(0);

                    //判断如果右侧可见的第一个item是否是header,设置相应的值
                    if (right.get(first).isHeader) {
                        rightTitle.setText(right.get(first).header);
                    } else {
                        rightTitle.setText(right.get(first).t.getType());
                    }
                }
                //遍历左边列表,列表对应的内容等于右边的title,则设置左侧对应item高亮
                for (int i = 0; i < left.size(); i++) {
                    if (left.get(i).equals(rightTitle.getText().toString())) {
                        leftAdapter.selectItem(i);
                    }
                }
                //如果右边最后一个完全显示的item的position,等于bean中最后一条数据的position(也就是右侧列表拉到底了),
                //则设置左侧列表最后一条item高亮
                if (rightManager.findLastCompletelyVisibleItemPosition() == right.size() - 1) {
                    leftAdapter.selectItem(left.size() - 1);
                }
            }
        });
    }

    private void initLeft() {
        if (leftAdapter == null) {
            leftAdapter = new ScrollLeftAdapter(R.layout.scroll_left, null);
            recLeft.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recLeft.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
            recLeft.setAdapter(leftAdapter);
        } else {
            leftAdapter.notifyDataSetChanged();
        }

        leftAdapter.setNewData(left);

        leftAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    //点击左侧列表的相应item,右侧列表相应的title置顶显示
                    //(最后一组内容若不能填充右侧整个可见页面,则显示到右侧列表的最底端)
                    case R.id.item:
                        leftAdapter.selectItem(position);
                        rightManager.scrollToPositionWithOffset(tPosition.get(position), 0);
                        break;
                }
            }
        });
    }



    //获取数据(若请求服务端数据,请求到的列表需有序排列)
    private void initData() {
        left = new ArrayList<>();
        left.add("语种");
        left.add("风格");
        left.add("场景");
        left.add("情感");
        left.add("主题");

        right = new ArrayList<>();

        right.add(new ScrollBean(true, left.get(0)));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("华语", left.get(0))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("欧美", left.get(0))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("日语", left.get(0))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("韩语", left.get(0))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("粤语", left.get(0))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("小语种", left.get(0))));

        right.add(new ScrollBean(true, left.get(1)));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("流行", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("摇滚", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("民谣", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("电子", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("舞曲", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("说唱", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("轻音乐", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("爵士", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("轻音乐", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("乡村", left.get(1))));

        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("古典", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("民族", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("英伦", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("金属", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("朋克", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("蓝调", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("雷鬼", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("世界音乐", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("拉丁", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("New Age", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("古风", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("后摇", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("Bossa Nova", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("另类/独立", left.get(1))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("R&B/Soul", left.get(1))));

        right.add(new ScrollBean(true, left.get(2)));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("清晨", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("夜晚", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("学习", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("工作", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("午休", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("下午茶", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("地铁", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("驾车", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("运动", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("旅行", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("散步", left.get(2))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("酒吧", left.get(2))));

        right.add(new ScrollBean(true, left.get(3)));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("怀旧", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("清新", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("浪漫", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("性感", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("伤感", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("治愈", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("放松", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("孤独", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("感动", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("兴奋", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("快乐", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("安静", left.get(3))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("思念", left.get(3))));

        right.add(new ScrollBean(true, left.get(4)));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("影视原声", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("ACG", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("儿童", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("校园", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("游戏", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("70后", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("80后", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("90后", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("网络歌曲", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("KTV", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("经典", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("翻唱", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("吉他", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("钢琴", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("器乐", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("榜单", left.get(4))));
        right.add(new ScrollBean(new ScrollBean.ScrollItemBean("00后", left.get(4))));

        for (int i = 0; i < right.size(); i++) {
            if (right.get(i).isHeader) {
                //遍历右侧列表,判断如果是header,则将此header在右侧列表中所在的position添加到集合中
                tPosition.add(i);
            }
        }
    }

    /**
     * 获得资源 dimens (dp)
     *
     * @param context
     * @param id      资源id
     * @return
     */
    public float getDimens(Context context, int id) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float px = context.getResources().getDimension(id);
        return px / dm.density;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5f);
    }
}
