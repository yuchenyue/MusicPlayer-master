package com.example.ycy.musicplayer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapter.LetRecyclerViewAdapter;
import entity.LetMusic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviceApi.Api;
import utils.HttpUtil;
import utils.MyApplication;

public class SongSheetActivity extends AppCompatActivity {
    private static final String TAG = "SongSheetActivity";
    public List<LetMusic.DataBean> letMusicList = new ArrayList<LetMusic.DataBean>();
    Context context;
    public LetRecyclerViewAdapter ladapter;
    private RecyclerView let_fragment_list;
    public LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_sheet);
        let_fragment_list = (RecyclerView) findViewById(R.id.let_fragment_list);
        layoutManager = new LinearLayoutManager(MyApplication.getContext());
        getNetMusicList();
    }

    private void getNetMusicList() {
        Api mApi = HttpUtil.getWebMusic();
        Call<LetMusic> musicCall = mApi.getLMusic("579621905",20,0);
        musicCall.enqueue(new Callback<LetMusic>() {
            @Override
            public void onResponse(Call<LetMusic> call, Response<LetMusic> response) {
//                if (response.code() != 400) {
                Log.d(TAG, "NetworkFragment--87--");
                letMusicList = response.body().getData();
                //写个适配器
                let_fragment_list.setLayoutManager(layoutManager);
                ladapter = new LetRecyclerViewAdapter(context, letMusicList);
                let_fragment_list.setAdapter(ladapter);
//                    Log.i(TAG, "歌名--" + netMusicList.get(0).getName() + netMusicList.get(0).getCreator());
                Log.i(TAG, "显示了--" +letMusicList.size() + "首歌曲");
            }
//            }

            @Override
            public void onFailure(Call<LetMusic> call, Throwable t) {

            }

        });
    }
}
