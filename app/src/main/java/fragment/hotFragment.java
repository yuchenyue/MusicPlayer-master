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

import adapter.HotRecyclerViewAdapter;
import adapter.LetRecyclerViewAdapter;
import entity.LetMusic;
import retrofit2.Call;
import retrofit2.Response;
import serviceApi.Api;
import utils.FastScrollManager;
import utils.HttpUtil;
import utils.MyApplication;

public class hotFragment extends Fragment{
    private static final String TAG = "hotFragment";
    public List<LetMusic.DataBean> hotMusicList = new ArrayList<LetMusic.DataBean>();
    private HotRecyclerViewAdapter hadapter;
    RecyclerView songsheet_fragment_list_hot;
    SwipeRefreshLayout hot_list_refreshLayout;
    public LinearLayoutManager layoutManager;
    MainActivity mainActivity;
    com.getbase.floatingactionbutton.FloatingActionButton to_top_hot;

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
        View view = inflater.inflate(R.layout.hotfragment, container, false);
        layoutManager = new FastScrollManager(MyApplication.getContext(),LinearLayoutManager.VERTICAL,false);

        songsheet_fragment_list_hot = view.findViewById(R.id.songsheet_fragment_list_hot);
        songsheet_fragment_list_hot.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLasst = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (firstVisibleItemPosition == 0) {
                        to_top_hot.setVisibility(View.INVISIBLE);
                    } else {
                        to_top_hot.setVisibility(View.VISIBLE);
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    to_top_hot.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){
                    isSlidingToLasst = true;
                }else {
                    isSlidingToLasst = false;
                }
            }
        });
        //下拉刷新
        hot_list_refreshLayout = view.findViewById(R.id.hot_list_refreshLayout);
        hot_list_refreshLayout.setRefreshing(true);
        hot_list_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNetMusicList();
                        hot_list_refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        to_top_hot = view.findViewById(R.id.to_top_hot);
        //点击返回顶部
        to_top_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songsheet_fragment_list_hot.smoothScrollToPosition(0);
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
        Call<LetMusic> musicCall = mApi.getLMusic("579621905", 30, 0,"hot");
        musicCall.enqueue(new retrofit2.Callback<LetMusic>() {
            @Override
            public void onResponse(Call<LetMusic> call, Response<LetMusic> response) {
                Log.d(TAG, "LetworkFragment--9089--");
                hotMusicList = response.body().getData();
                songsheet_fragment_list_hot.setLayoutManager(layoutManager);
                hadapter = new HotRecyclerViewAdapter(getContext(), hotMusicList);
                songsheet_fragment_list_hot.setAdapter(hadapter);
                hadapter.setOnItemClickListener(MyItemClickListener);
                hot_list_refreshLayout.setRefreshing(false);
                hadapter.notifyDataSetChanged();
                Log.i(TAG, "显示了--" + hotMusicList.size() + "首歌曲");
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
                case R.id.item_hot:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle(hotMusicList.get(position).getTitle());
                    dialog.setMessage(hotMusicList.get(position).getDescription());
                    dialog.setNegativeButton("好的", null);
                    dialog.show();
                    break;
                default:
                    Intent intent = new Intent(getContext(), SongListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", hotMusicList.get(position).getId());
                    bundle.putString("pic", hotMusicList.get(position).getCoverImgUrl());
                    bundle.putString("description", hotMusicList.get(position).getDescription());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(getContext(), "item" + (position + 1), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}