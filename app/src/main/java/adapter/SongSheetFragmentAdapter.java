package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class SongSheetFragmentAdapter extends FragmentPagerAdapter {
    private String []titles;
    List<Fragment> fragments_song;

    public SongSheetFragmentAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments_song.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    //自定义一个添加title和fragment的方法，供Activity使用
    public void addTitlesAndFragments(String []titles, List<Fragment> fragments_song) {
        this.titles = titles;
        this.fragments_song = fragments_song;
    }

}