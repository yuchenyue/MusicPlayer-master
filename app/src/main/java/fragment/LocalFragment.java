package fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ycy.musicplayer.MainActivity;
import com.example.ycy.musicplayer.R;

import java.util.HashMap;
import java.util.List;

import adapter.MusicAdapter;
import entity.Music;
import utils.MusicUtil;

/**
 * Created by Administrator on 2019/1/14.
 */

public class LocalFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "LocalFragment";
    ListView local_fragment_list;
    public List<Music> musics;
    public MusicAdapter musicAdapter;
    public HashMap<String, String> map;
    MainActivity mainActivity;
//    CallBackValue callBackValue;
    public static int state = 2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.local_fragment, container, false);
        local_fragment_list = view.findViewById(R.id.local_fragment_list);
        local_fragment_list.setOnItemClickListener(this);
        loadData();
        mainActivity.bindMusicService();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mainActivity.unbindMusicService();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivity.unbindMusicService();
    }

    //加载本地音乐列表
    public void loadData() {
        musics = MusicUtil.getmusics(mainActivity);
        musicAdapter = new MusicAdapter(mainActivity, musics);
        local_fragment_list.setAdapter(musicAdapter);
        Log.i(TAG,"歌曲数量" + musics.size());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mainActivity.musicService.play(position);//点击这个item
        state = 1;
        Log.i(TAG, "LocalFragment传出position----" + position);
    }


}
