package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import entity.Music;

/**
 * Created by Administrator on 2019/1/16.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;
    List<Music> musics;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<Music> musics) {
        super(fm);
        this.fragmentList = fragmentList;
        this.musics = musics;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragmentList.get(position);
            case 1:
                return fragmentList.get(position);
        }
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


}
