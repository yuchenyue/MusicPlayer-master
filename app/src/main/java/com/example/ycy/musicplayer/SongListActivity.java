package com.example.ycy.musicplayer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.util.ArrayList;
import java.util.List;

import adapter.ListRecyclerViewAdapter;
import entity.ListMusic;
import manage.FastBlurUtil;
import retrofit2.Call;
import retrofit2.Response;
import serviceApi.Api;
import utils.HttpUtil;
import utils.MyApplication;
import utils.WeiboDialogUtils;

public class SongListActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SongListActivity";
    CollapsingToolbarLayout song_list_toolbarLayout;
    TextView tv_empty_list,list_text;//显示专辑简介
    Button list_button;//播放列表按钮
    RecyclerView netsong_musicList;
    SwipeRefreshLayout song_list_refreshLayout;
    private List<ListMusic.DataBean> listMusicList = new ArrayList<>();
    public LinearLayoutManager layoutManager;
    ListRecyclerViewAdapter lisadapter;
    String id, pic, description,name;
    private int position;
    private Dialog mWeiboDialog;
    private Handler xHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        MyApplication.getInstance().addActivity(this);
        bindMusicService();
        id = getIntent().getStringExtra("id");//传进来的专辑的ID
        pic = getIntent().getStringExtra("pic");//传进来的专辑的图片
        description = getIntent().getStringExtra("description");//传进来的专辑的简介
        name = getIntent().getStringExtra("name");//传进来的歌单名
        Toolbar song_list_toolbar = (Toolbar) findViewById(R.id.song_list_toolbar);
        setSupportActionBar(song_list_toolbar);
        song_list_toolbar.setTitle("歌单");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        song_list_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        song_list_toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.song_list_toolbarLayout);
        song_list_toolbarLayout.setTitle(name);
        intView();
        getNetMusicList();
    }

    private void intView() {
        list_button = (Button) findViewById(R.id.list_button);
        list_button.setOnClickListener(this);
        song_list_toolbarLayout.setOnClickListener(this);
        Glide.with(getApplicationContext())
                .load(pic)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap bitmap = FastBlurUtil.doBlur(resource,25,false);
                        Drawable drawable = new BitmapDrawable(bitmap);
                        song_list_toolbarLayout.setBackground(drawable);
                    }
                });
        list_text = (TextView) findViewById(R.id.list_text);
        if (description != null){
            list_text.setText(description);
        }else {
            list_text.setText("暂无介绍");
        }
        tv_empty_list = (TextView) findViewById(R.id.tv_empty_list);
        netsong_musicList = (RecyclerView) findViewById(R.id.netsong_musicList);
        layoutManager = new LinearLayoutManager(MyApplication.getContext());
        netsong_musicList.setLayoutManager(layoutManager);
        lisadapter = new ListRecyclerViewAdapter(SongListActivity.this, listMusicList);
        netsong_musicList.setAdapter(lisadapter);
        lisadapter.setOnItemClickListener(MyItemClickListener);
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
        Call<ListMusic> musicCall = mApi.getListMusic(id,1);
        musicCall.enqueue(new retrofit2.Callback<ListMusic>() {
            @Override
            public void onResponse(Call<ListMusic> call, Response<ListMusic> response) {
                listMusicList.addAll(response.body().getData());
                lisadapter.notifyDataSetChanged();
                if (listMusicList.size()!=0){
                    tv_empty_list.setVisibility(View.GONE);
                }
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
                        startMusic();
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
            case R.id.song_list_toolbarLayout:
                if (description != null){
                    mWeiboDialog = WeiboDialogUtils.createMesgDialog(this,description+"");
                }else {
                    mWeiboDialog = WeiboDialogUtils.createMesgDialog(this,"暂无介绍");
                }

                break;
            default:
                break;
        }
    }

    public void startMusic() {
        MyApplication.setIsLoc(false);
        MyApplication.setIsWeb(true);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this,"歌单添加中...");
        xHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MyApplication.listMusicList == null){
                    MyApplication.setListMusicList(listMusicList);
                }else {
                    MyApplication.listMusicList.clear();
                    MyApplication.setListMusicList(listMusicList);
                }
                musicService.playweb(0);
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(SongListActivity.this,"由于各种原因，该歌单有"+MyApplication.getListMusicList().size()+"首歌曲可播放！",Toast.LENGTH_LONG).show();
            }
        },3000);
    }

    /**
     * 转换歌曲时间的格式
     *
     * @param time
     * @return
     */
    public static String formatTime(int time) {
        if (time % 60 < 10) {
            String tt = time / 60 + ":0" + time % 60;
            return tt;
        } else {
            String tt = time / 60 + ":" + time % 60;
            return tt;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindMusicService();
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
                    showlrc(position);
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
//            normalDialog.setNeutralButton("收藏",new DialogInterface.OnClickListener(){
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(SongListActivity.this,"收藏",Toast.LENGTH_SHORT).show();
//                }
//            });
            normalDialog.show();
        }

        public void showlrc(final int position){
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(SongListActivity.this);
            normalDialog.setTitle(listMusicList.get(position).getName());
            normalDialog.setMessage(listMusicList.get(position).getLrc());
            Log.d(TAG,listMusicList.get(position).getLrc()+"");
            normalDialog.setPositiveButton("下载",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(SongListActivity.this,"下载",Toast.LENGTH_SHORT).show();
                }
            });
            normalDialog.show();
        }
    };

}
