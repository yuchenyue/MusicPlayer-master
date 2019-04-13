package com.example.ycy.musicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.ListRecyclerViewAdapter;
import entity.ListMusic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviceApi.Api;
import utils.HttpUtil;
import utils.MyApplication;

public class SongListActivity extends AppCompatActivity {
    private static final String TAG = "SongListActivity";
    ImageView list_img;
    TextView list_tv,tv_empty_list;
    RecyclerView netsong_musicList;
    private List<ListMusic.DataBean.Song> listMusicList = new ArrayList<>();
    public LinearLayoutManager layoutManager;
    ListRecyclerViewAdapter lisadapter;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        id = getIntent().getStringExtra("id");
        Toast.makeText(this,"歌单id"+id,Toast.LENGTH_SHORT).show();
        intView();
        getNetMusicList();
    }

    private void intView() {
        list_img = (ImageView) findViewById(R.id.list_img);
        list_tv = (TextView) findViewById(R.id.list_tv);
        tv_empty_list = (TextView) findViewById(R.id.tv_empty_list);
        netsong_musicList = (RecyclerView) findViewById(R.id.netsong_musicList);
        layoutManager = new LinearLayoutManager(MyApplication.getContext());
    }
    private void getNetMusicList() {
        Api mApi = HttpUtil.getWebMusic();
        Call<ListMusic> musicCall = mApi.getListMusic("579621905",id,10,0);
        musicCall.enqueue(new Callback<ListMusic>() {
            @Override
            public void onResponse(Call<ListMusic> call, Response<ListMusic> response) {
                Log.d(TAG, "NetworkFragment--87--");
                listMusicList = response.body().getData().getSongs();
                //写个适配器
                netsong_musicList.setLayoutManager(layoutManager);
                lisadapter = new ListRecyclerViewAdapter(SongListActivity.this, listMusicList);
                netsong_musicList.setAdapter(lisadapter);
            }
            @Override
            public void onFailure(Call<ListMusic> call, Throwable t) {
                tv_empty_list.setVisibility(View.VISIBLE);
            }
        });
    }
}
