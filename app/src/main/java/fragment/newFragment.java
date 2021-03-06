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
import android.widget.TextView;
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
    TextView tv_empty;
    public FastScrollManager layoutManager;
    MainActivity mainActivity;
    com.getbase.floatingactionbutton.FloatingActionButton to_top;
    int page = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        tv_empty = view.findViewById(R.id.tv_empty_new);
        songsheet_fragment_list = view.findViewById(R.id.songsheet_fragment_list);
        layoutManager = new FastScrollManager(getActivity(),3);
        songsheet_fragment_list.setLayoutManager(layoutManager);

        ladapter = new NewRecyclerViewAdapter(getActivity(), letMusicList);
        songsheet_fragment_list.setAdapter(ladapter);
        ladapter.setOnItemClickListener(MyItemClickListener);

        let_list_refreshLayout = view.findViewById(R.id.let_list_refreshLayout);
        let_list_refreshLayout.setRefreshing(true);

        to_top = view.findViewById(R.id.to_top);

        initView();
        getNetMusicList(page);
        return view;
    }

    public void  initView(){
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
                        getNetMusicList(page);
                        Toast.makeText(getContext(), "上拉加载", Toast.LENGTH_SHORT).show();
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
        let_list_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        let_list_refreshLayout.setRefreshing(false);
                    }
                }, 2000);
                getNetMusicList(0);
            }
        });
        //点击返回顶部
        to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songsheet_fragment_list.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getNetMusicList(int page) {
        Api mApi = HttpUtil.getWebMusic();
        Call<LetMusic> musicCall = mApi.getLMusic(null,20,"new",null,page);
        musicCall.enqueue(new retrofit2.Callback<LetMusic>() {
            @Override
            public void onResponse(Call<LetMusic> call, Response<LetMusic> response) {
                if (response.code() == 200) {
                    letMusicList.addAll(response.body().getData());
                    let_list_refreshLayout.setRefreshing(false);
                    ladapter.notifyDataSetChanged();
                    if (letMusicList == null){
                        tv_empty.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(getActivity(),"服务器离线",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LetMusic> call, Throwable t) {
                tv_empty.setVisibility(View.VISIBLE);
            }
        });
    }


    private NewRecyclerViewAdapter.OnItemClickListener MyItemClickListener = new NewRecyclerViewAdapter.OnItemClickListener() {

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
                    break;
            }
        }
    };
}
