package fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ycy.musicplayer.MainActivity;
import com.example.ycy.musicplayer.R;

import java.util.List;

import adapter.MusicAdapter;
import entity.Music;
import utils.MusicUtil;
import utils.MyApplication;

/**
 * Created by Administrator on 2019/1/14.
 */

public class LocalFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "LocalFragment";
    ListView local_fragment_list;
    TextView tv_empty;
    SwipeRefreshLayout swipeRefreshLayout;
    public List<Music> musics;
    public MusicAdapter musicAdapter;
    MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        local_fragment_list = view.findViewById(R.id.local_fragment_list);
        tv_empty = view.findViewById(R.id.tv_empty);
        local_fragment_list.setEmptyView(tv_empty);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                musics.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
                loadData();
            }
        });
        local_fragment_list.setOnItemClickListener(this);
        loadData();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //加载本地音乐列表
    public void loadData() {
        musics = MusicUtil.getmusics(mainActivity);
        MyApplication.setMusics(musics);
        musicAdapter = new MusicAdapter(mainActivity, musics);
        local_fragment_list.setAdapter(musicAdapter);
        Log.i(TAG, "歌曲数量" + musics.size());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (musics != null){
            MyApplication.setMusics(musics);
            MyApplication.setIsWeb(false);
            MyApplication.setIsLoc(true);
            MyApplication.setState(true);
            mainActivity.musicService.play(position);
        }else {
            Toast.makeText(getActivity(),"暂无本地歌曲",Toast.LENGTH_SHORT).show();
        }
//        state = 1;
        Log.i(TAG, "LocalFragment传出position----" + position);
    }
//
//    //发送自定义视图通知
//    public void sendCustomViewNotification(View view) {
//        //显示bigView的notification用到的视图
//        RemoteViews bigView = new RemoteViews(getPackageName(), R.layout.notification);
//
//        Notification notification = new NotificationCompat.Builder(getContext())
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker("开始播放啦~~")
//                .setOngoing(true)
//                .setCustomBigContentView(bigView)//设置显示bigView的notification视图
//                .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
//                .build();
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager.notify(22, notification);
//    }


}
