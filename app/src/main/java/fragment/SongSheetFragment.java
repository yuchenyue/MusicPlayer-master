package fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ycy.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

import adapter.SongSheetFragmentAdapter;

public class SongSheetFragment extends Fragment {
    private static final String TAG = "SongSheetFragment";
    private String[] titles = {"热门", "最新","选择分类"};
    private List<Fragment> fragments_song;
    private SongSheetFragmentAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public SongSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songsheet, container, false);
        //实例化
        viewPager = view.findViewById(R.id.viewpager2);
        tabLayout = view.findViewById(R.id.tablayout2);
        addTabToTabLayout();
        //页面，数据源，里面是创建的三个页面（Fragment）
        fragments_song = new ArrayList<>();
        fragments_song.add(new hotFragment());
        fragments_song.add(new newFragment());
        fragments_song.add(new otherFragment());
        //ViewPager的适配器，获得Fragment管理器
        adapter = new SongSheetFragmentAdapter(getChildFragmentManager());
        adapter.addTitlesAndFragments(titles,fragments_song);
        viewPager.setAdapter(adapter);
        //将TabLayout和ViewPager绑定在一起，一个动另一个也会跟着动
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
    /**
     * Description：给TabLayout添加tab
     */
    private void addTabToTabLayout() {
        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}