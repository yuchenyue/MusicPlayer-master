package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import entity.Music;

/**
 * Created by Administrator on 2019/1/16.
 * 主界面（MainActivity）中Fragment适配器
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private String []titles;
    List<Fragment> fragmentList;

    public FragmentAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
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
    public void addTitlesAndFragments(String []titles, List<Fragment> fragmentList) {
        this.titles = titles;
        this.fragmentList = fragmentList;
    }

}
