package com.example.ycy.musicplayer;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import adapter.HotRecyclerViewAdapter;
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
    TextView tv_empty,text_style;
    Button style_back;
    public FastScrollManager layoutManager;
    com.getbase.floatingactionbutton.FloatingActionButton to_top;
    String style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_song_sheet);
        layoutManager = new FastScrollManager(this, LinearLayoutManager.VERTICAL, false);
        Intent intent = getIntent();
        style = intent.getStringExtra("s");
        if (style == null){
            Log.i(TAG,"选择有误，请重新选择种类");
        }

        initView();

    }

    private void initView() {
        text_style = (TextView) findViewById(R.id.text_style);
        text_style.setText(style);
        style_back = (Button) findViewById(R.id.style_back);
        style_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        songsheet_fragment_list = (RecyclerView) findViewById(R.id.songsheet_fragment_list_more);
        songsheet_fragment_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLasst = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager = (FastScrollManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (firstVisibleItemPosition == 0) {
                        to_top.setVisibility(View.INVISIBLE);
                    } else {
                        to_top.setVisibility(View.VISIBLE);
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    to_top.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    isSlidingToLasst = true;
                } else {
                    isSlidingToLasst = false;
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
                getNetMusicList(style);
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

        getNetMusicList(style);
    }


    private void getNetMusicList(final String style) {
        Api mApi = HttpUtil.getWebMusic();
        Call<LetMusic> musicCall = mApi.getLMusic("579621905", style, 30, 0,null);
        musicCall.enqueue(new retrofit2.Callback<LetMusic>() {
            @Override
            public void onResponse(Call<LetMusic> call, Response<LetMusic> response) {
                if (response.code() == 200) {
                    letMusicList = response.body().getData();
                    if (letMusicList == null ) {
                        tv_empty.setVisibility(View.VISIBLE);
                    } else {
                        songsheet_fragment_list.setLayoutManager(layoutManager);
                        madapter = new MoreRecyclerViewAdapter(MoreSongSheetActivity.this, letMusicList);
                        songsheet_fragment_list.setAdapter(madapter);
                        madapter.setOnItemClickListener(MyItemClickListener);
                        let_list_refreshLayout.setRefreshing(false);
                        madapter.notifyDataSetChanged();
                        Log.i(TAG, style + "歌曲显示了--" + letMusicList.size() + "首歌曲" + response.code());
                    }
                }else {
//                    let_list_refreshLayout.setRefreshing(false);
                    Toast.makeText(MoreSongSheetActivity.this,"服务器离线",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LetMusic> call, Throwable t) {
//                tv_empty.setVisibility(View.VISIBLE);
            }

        });
    }

    private MoreRecyclerViewAdapter.OnItemClickListener MyItemClickListener = new MoreRecyclerViewAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()) {
                case R.id.item_let:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MoreSongSheetActivity.this);
                    dialog.setTitle(letMusicList.get(position).getTitle());
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
                    intent.putExtras(bundle);
                    startActivity(intent);
//                    Toast.makeText(MoreSongSheetActivity.this, "item" + (position + 1), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
