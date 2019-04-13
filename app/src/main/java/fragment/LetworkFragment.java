package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ycy.musicplayer.MainActivity;
import com.example.ycy.musicplayer.R;
import com.example.ycy.musicplayer.SongListActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.LetRecyclerViewAdapter;
import adapter.WebRecyclerViewAdapter;
import entity.LetMusic;
import entity.NetMusic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviceApi.Api;
import utils.HttpUtil;
import utils.MyApplication;

/**
 * Created by Administrator on 2019/1/14.
 */

public class LetworkFragment extends Fragment{

    private static final String TAG = "LetworkFragment";
    private static final int START_MUSIC = 0x1;
    public List<LetMusic.DataBean> letMusicList = new ArrayList<LetMusic.DataBean>();
    private LetRecyclerViewAdapter ladapter;
    RecyclerView songsheet_fragment_list;
    public LinearLayoutManager layoutManager;
    MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.let_fragment, container, false);
        songsheet_fragment_list = view.findViewById(R.id.songsheet_fragment_list);
        layoutManager = new LinearLayoutManager(MyApplication.getContext());
        getNetMusicList();
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



    private void getNetMusicList() {
        Api mApi = HttpUtil.getWebMusic();
        Call<LetMusic> musicCall = mApi.getLMusic("579621905",10,0);
        musicCall.enqueue(new retrofit2.Callback<LetMusic>() {
            @Override
            public void onResponse(Call<LetMusic> call, Response<LetMusic> response) {
                    Log.d(TAG, "LetworkFragment--9089--");
                    letMusicList = response.body().getData();
//                    //写个适配器
                    songsheet_fragment_list.setLayoutManager(layoutManager);
                    ladapter = new LetRecyclerViewAdapter(getContext(), letMusicList);
                    songsheet_fragment_list.setAdapter(ladapter);
                    ladapter.setOnItemClickListener(MyItemClickListener);
                    Log.i(TAG, "显示了--" + letMusicList.size() + "首歌曲");
                }
            @Override
            public void onFailure(Call<LetMusic> call, Throwable t) {
            }
        });
    }

    private LetRecyclerViewAdapter.OnItemClickListener MyItemClickListener = new LetRecyclerViewAdapter.OnItemClickListener(){

        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()){
                case R.id.item_let:
                    Toast.makeText(getContext(),"按钮"+letMusicList.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Intent intent = new Intent(getContext(),SongListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",letMusicList.get(position).getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(getContext(),"item"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {
            Toast.makeText(getContext(),"长按没用哦~",Toast.LENGTH_SHORT).show();
        }
    };
}
