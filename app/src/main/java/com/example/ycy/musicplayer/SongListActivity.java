package com.example.ycy.musicplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import adapter.ListRecyclerViewAdapter;
import entity.ListMusic;
import retrofit2.Call;
import retrofit2.Response;
import serviceApi.Api;
import services.MusicService;
import utils.FastScrollManager;
import utils.HttpUtil;
import utils.MyApplication;

public class SongListActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SongListActivity";
    private static final int SEARCH_MUSICLIST = 0x1;
    ImageView list_img;//显示专辑图
    TextView list_tv, tv_empty_list;//显示专辑简介
    Button list_button;//播放列表按钮
    RecyclerView netsong_musicList;
    SwipeRefreshLayout song_list_refreshLayout;
    private List<ListMusic.DataBean.Song> listMusicList = new ArrayList<>();
    public FastScrollManager layoutManager;
    ListRecyclerViewAdapter lisadapter;
    String id, pic, description;
    private int position;
    MusicService musicService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        MyApplication.getInstance().addActivity(this);
        id = getIntent().getStringExtra("id");//传进来的专辑的ID
        pic = getIntent().getStringExtra("pic");//传进来的专辑的图片
        description = getIntent().getStringExtra("description");//传进来的专辑的简介
        intView();
        getNetMusicList();
    }

    private void intView() {
        list_button = (Button) findViewById(R.id.list_button);
        list_button.setOnClickListener(this);
        list_img = (ImageView) findViewById(R.id.list_img);
        Glide.with(getApplicationContext())
                .load(pic)
                .into(list_img);
        list_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list_tv = (TextView) findViewById(R.id.list_tv);
        list_tv.setText(description);
        list_tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_empty_list = (TextView) findViewById(R.id.tv_empty_list);
        netsong_musicList = (RecyclerView) findViewById(R.id.netsong_musicList);
        layoutManager = new FastScrollManager(MyApplication.getContext());
        song_list_refreshLayout = (SwipeRefreshLayout) findViewById(R.id.song_list_refreshLayout);
        song_list_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        song_list_refreshLayout.setRefreshing(false);
                    }
                },1500);
                getNetMusicList();

            }
        });
    }

    /**
     * 获取歌曲列表
     */
    private void getNetMusicList() {
        Api mApi = HttpUtil.getWebMusic();
        Call<ListMusic> musicCall = mApi.getListMusic("579621905", id);
        musicCall.enqueue(new retrofit2.Callback<ListMusic>() {
            @Override
            public void onResponse(Call<ListMusic> call, Response<ListMusic> response) {
                listMusicList = response.body().getData().getSongs();
                netsong_musicList.setLayoutManager(layoutManager);
                lisadapter = new ListRecyclerViewAdapter(SongListActivity.this, listMusicList);
                netsong_musicList.setAdapter(lisadapter);
                lisadapter.setOnItemClickListener(MyItemClickListener);
                tv_empty_list.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<ListMusic> call, Throwable t) {
                tv_empty_list.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 播放列表按钮
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list_button:
                AlertDialog.Builder normalDialog = new AlertDialog.Builder(SongListActivity.this);
                normalDialog.setTitle("是否播放当前列表");
                normalDialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SongListActivity.this,"是",Toast.LENGTH_SHORT).show();
                        startMusic();//我想播放这个歌单的时候在吧这个列表传到MyApplicaton中
                    }
                });
                normalDialog.setNeutralButton("否",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SongListActivity.this,"否",Toast.LENGTH_SHORT).show();
                    }
                });
                normalDialog.show();
                break;
        }

    }

    public void startMusic() {
        if (MyApplication.listMusicList == null){
            MyApplication.setListMusicList(listMusicList);
        }else {
            MyApplication.listMusicList.clear();
            MyApplication.setListMusicList(listMusicList);
        }
//        MyApplication.setListMusicList(listMusicList);
//        Toast.makeText(getApplication(),listMusicList.size(),Toast.LENGTH_SHORT).show();
        MyApplication.setIsWeb(true);
        musicService.playweb();//这里一直是为空的
    }

    /**
     * 详情按钮
     */
    private ListRecyclerViewAdapter.OnItemClickListener MyItemClickListener = new ListRecyclerViewAdapter.OnItemClickListener(){

        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()){
                case R.id.item_list:
                    showMultiBtnDialog(position);
                    break;
                default:
                    //在这里播放就可以
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(listMusicList.get(position).getUrl());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(SongListActivity.this,listMusicList.get(position).getName(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void showMultiBtnDialog(final int position){
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(SongListActivity.this);
            normalDialog.setTitle(listMusicList.get(position).getName());
            normalDialog.setMessage(listMusicList.get(position).getSinger());
            normalDialog.setPositiveButton("下载",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(SongListActivity.this,"下载",Toast.LENGTH_SHORT).show();
                }
            });
            normalDialog.setNeutralButton("收藏",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(SongListActivity.this,"收藏",Toast.LENGTH_SHORT).show();
                }
            });
            normalDialog.show();
        }
    };

}
