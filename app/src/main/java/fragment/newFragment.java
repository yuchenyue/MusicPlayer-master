package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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

import adapter.NewRecyclerViewAdapter;
import entity.LetMusic;
import retrofit2.Call;
import retrofit2.Response;
import serviceApi.Api;
import utils.FastScrollManager;
import utils.HttpUtil;
import utils.MyApplication;

/**
 * Created by Administrator on 2019/1/14.
 */

public class newFragment extends Fragment {

    private static final String TAG = "newFragment";
    public List<LetMusic.DataBean> letMusicList = new ArrayList<LetMusic.DataBean>();
    private NewRecyclerViewAdapter ladapter;
    RecyclerView songsheet_fragment_list;
    SwipeRefreshLayout let_list_refreshLayout;
    public FastScrollManager layoutManager;
    MainActivity mainActivity;
    com.getbase.floatingactionbutton.FloatingActionButton to_top;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        layoutManager = new FastScrollManager(MyApplication.getContext(), LinearLayoutManager.VERTICAL, false);

        songsheet_fragment_list = view.findViewById(R.id.songsheet_fragment_list);
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
        let_list_refreshLayout = view.findViewById(R.id.let_list_refreshLayout);
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
        to_top = view.findViewById(R.id.to_top);
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getNetMusicList() {
        Api mApi = HttpUtil.getWebMusic();
        Call<LetMusic> musicCall = mApi.getLMusic("579621905",null, 30, 0, "new");
        musicCall.enqueue(new retrofit2.Callback<LetMusic>() {
            @Override
            public void onResponse(Call<LetMusic> call, Response<LetMusic> response) {
                letMusicList = response.body().getData();
                songsheet_fragment_list.setLayoutManager(layoutManager);
                ladapter = new NewRecyclerViewAdapter(getContext(), letMusicList);
                songsheet_fragment_list.setAdapter(ladapter);
                ladapter.setOnItemClickListener(MyItemClickListener);
                let_list_refreshLayout.setRefreshing(false);
                ladapter.notifyDataSetChanged();
                Log.i(TAG, "最新歌曲显示了--" + letMusicList.size() + "首歌曲");
            }

            @Override
            public void onFailure(Call<LetMusic> call, Throwable t) {
            }
        });
    }


    private NewRecyclerViewAdapter.OnItemClickListener MyItemClickListener = new NewRecyclerViewAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()) {
                case R.id.item_let:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle(letMusicList.get(position).getTitle());
                    dialog.setMessage(letMusicList.get(position).getDescription());
                    dialog.setNegativeButton("好的", null);
                    dialog.show();
                    break;
                default:
                    Intent intent = new Intent(getContext(), SongListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", letMusicList.get(position).getId());
                    bundle.putString("pic", letMusicList.get(position).getCoverImgUrl());
                    bundle.putString("description", letMusicList.get(position).getDescription());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(getContext(), "item" + (position + 1), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
