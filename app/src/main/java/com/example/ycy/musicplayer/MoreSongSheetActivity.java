package com.example.ycy.musicplayer;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import adapter.MoreRecyclerViewAdapter;
import entity.LetMusic;
import retrofit2.Call;
import retrofit2.Response;
import serviceApi.Api;
import utils.FastScrollManager;
import utils.HttpUtil;

public class MoreSongSheetActivity extends AppCompatActivity {

    private static final String TAG = "MoreSongSheetActivity";
    public List<LetMusic.DataBean> letMusicList = new ArrayList<LetMusic.DataBean>();
    private MoreRecyclerViewAdapter madapter;
    RecyclerView songsheet_fragment_list;
    SwipeRefreshLayout let_list_refreshLayout;
    TextView tv_empty;
    public FastScrollManager layoutManager;
    com.getbase.floatingactionbutton.FloatingActionButton to_top;
    String style;
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_song_sheet);
        layoutManager = new FastScrollManager(this,3);
        Intent intent = getIntent();
        style = intent.getStringExtra("s");
        if (style == null){
            tv_empty.setVisibility(View.VISIBLE);
            Log.i(TAG,"选择有误，请重新选择种类");
        }
        Toolbar mToolbar=(Toolbar)findViewById(R.id.toolbar_more);
        setSupportActionBar(mToolbar);
        //设置是否有返回箭头
        getSupportActionBar().setTitle(style);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();

    }

    private void initView() {
        songsheet_fragment_list = (RecyclerView) findViewById(R.id.songsheet_fragment_list_more);
        songsheet_fragment_list.setLayoutManager(layoutManager);
        madapter = new MoreRecyclerViewAdapter(MoreSongSheetActivity.this, letMusicList);
        songsheet_fragment_list.setAdapter(madapter);
        madapter.setOnItemClickListener(MyItemClickListener);
        songsheet_fragment_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager = (FastScrollManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (firstVisibleItemPosition == 0) {
                        to_top.setVisibility(View.INVISIBLE);
                    } else {
                        to_top.setVisibility(View.VISIBLE);
                    }
                    if (lastVisibleItemPosition == (layoutManager.getItemCount() - 1) && isSlidingToLast == true) {
                        page = page + 20;
                        getNetMusicList(style,page);
                        Toast.makeText(getApplicationContext(), "上拉加载", Toast.LENGTH_SHORT).show();
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    to_top.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
            }
        });
        //下拉刷新
        let_list_refreshLayout = (SwipeRefreshLayout) findViewById(R.id.more_list_refreshLayout);
        let_list_refreshLayout.setRefreshing(true);
        let_list_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        let_list_refreshLayout.setRefreshing(false);
                    }
                }, 2000);
                getNetMusicList(style,page);
            }
        });
        to_top = (FloatingActionButton) findViewById(R.id.to_top_more);
//        点击返回顶部
        to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songsheet_fragment_list.smoothScrollToPosition(0);
            }
        });

        getNetMusicList(style,0);
    }


    private void getNetMusicList(final String style,int page) {
        Api mApi = HttpUtil.getWebMusic();
        Call<LetMusic> musicCall = mApi.getLMusic(null,20,null,style,page);
        musicCall.enqueue(new retrofit2.Callback<LetMusic>() {
            @Override
            public void onResponse(Call<LetMusic> call, Response<LetMusic> response) {
                if (response.code() == 200) {
                    letMusicList.addAll(response.body().getData());
                    let_list_refreshLayout.setRefreshing(false);
                    madapter.notifyDataSetChanged();
                    if (letMusicList == null ) {
                        tv_empty.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(MoreSongSheetActivity.this,"服务器离线",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LetMusic> call, Throwable t) {
                tv_empty.setVisibility(View.VISIBLE);
            }

        });
    }

    private MoreRecyclerViewAdapter.OnItemClickListener MyItemClickListener = new MoreRecyclerViewAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()) {
                case R.id.item_let:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MoreSongSheetActivity.this);
                    dialog.setTitle(letMusicList.get(position).getName());
                    dialog.setMessage(letMusicList.get(position).getDescription());
                    dialog.setNegativeButton("好的", null);
                    dialog.show();
                    break;
                default:
                    Intent intent = new Intent(MoreSongSheetActivity.this, SongListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", letMusicList.get(position).getId());
                    bundle.putString("pic", letMusicList.get(position).getCoverImgUrl());
                    bundle.putString("description", letMusicList.get(position).getDescription());
                    bundle.putString("name",letMusicList.get(position).getName());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
        }
    };
}
