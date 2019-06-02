package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ycy.musicplayer.MainActivity;
import com.example.ycy.musicplayer.R;
import com.example.ycy.musicplayer.SongListActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.HotRecyclerViewAdapter;
import entity.LetMusic;
import retrofit2.Call;
import retrofit2.Response;
import serviceApi.Api;
import utils.FastScrollManager;
import utils.HttpUtil;

public class hotFragment extends Fragment {
    private static final String TAG = "hotFragment";
    public List<LetMusic.DataBean> letMusicList = new ArrayList<LetMusic.DataBean>();
    private HotRecyclerViewAdapter hadapter;
    RecyclerView songsheet_fragment_list;
    SwipeRefreshLayout let_list_refreshLayout;
    TextView tv_empty;
    public FastScrollManager layoutManager;
    MainActivity mainActivity;
    com.getbase.floatingactionbutton.FloatingActionButton to_top;

    public hotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        tv_empty = view.findViewById(R.id.tv_empty_hot);
        songsheet_fragment_list = view.findViewById(R.id.songsheet_fragment_list_hot);
        layoutManager = new FastScrollManager(getActivity(), 3);
        songsheet_fragment_list.setAdapter(hadapter);
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
        let_list_refreshLayout = view.findViewById(R.id.hot_list_refreshLayout);
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
                getNetMusicList();
            }
        });
        to_top = view.findViewById(R.id.to_top_hot);
        //点击返回顶部
        to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songsheet_fragment_list.smoothScrollToPosition(0);
            }
        });
        getNetMusicList();
        return view;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "hotFragment:onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "hotFragment:onDestroyView");
        super.onDestroyView();
    }

    private void getNetMusicList() {
        Api mApi = HttpUtil.getWebMusic();
        Call<LetMusic> musicCall = mApi.getLMusic(null, 15, "hot", null);
        Log.i(TAG, "显示了--" + mApi);
        musicCall.enqueue(new retrofit2.Callback<LetMusic>() {
            @Override
            public void onResponse(Call<LetMusic> call, Response<LetMusic> response) {
                if (response.code() == 200) {
                    letMusicList = response.body().getData();
                    if (letMusicList == null) {
                        tv_empty.setVisibility(View.VISIBLE);
                    } else {
                        songsheet_fragment_list.setLayoutManager(layoutManager);
                        hadapter = new HotRecyclerViewAdapter(getActivity(), letMusicList);
                        songsheet_fragment_list.setAdapter(hadapter);
                        hadapter.setOnItemClickListener(MyItemClickListener);
                        let_list_refreshLayout.setRefreshing(false);
                        hadapter.notifyDataSetChanged();
                        Log.i(TAG, "热门歌曲显示了--" + letMusicList.size() + response.code() + "首歌曲");
                    }
                } else {
                    let_list_refreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "服务器离线", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LetMusic> call, Throwable t) {
            }

        });
    }


    private HotRecyclerViewAdapter.OnItemClickListener MyItemClickListener = new HotRecyclerViewAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()) {
                case R.id.item_let:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle(letMusicList.get(position).getName());
                    dialog.setMessage(letMusicList.get(position).getDescription());
                    dialog.setNegativeButton("好的", null);
                    dialog.show();
                    break;
                default:
                    Intent intent = new Intent(getActivity(), SongListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", letMusicList.get(position).getId());
                    bundle.putString("pic", letMusicList.get(position).getCoverImgUrl());
                    bundle.putString("description", letMusicList.get(position).getDescription());
                    bundle.putString("name",letMusicList.get(position).getName());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "item" + (position + 1), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}