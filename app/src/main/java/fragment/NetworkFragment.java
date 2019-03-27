package fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ycy.musicplayer.MainActivity;
import com.example.ycy.musicplayer.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import adapter.WebRecyclerViewAdapter;
import entity.NetMusic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviceApi.Api;
import utils.HttpUtil;
import utils.MyApplication;

/**
 * Created by Administrator on 2019/1/14.
 */

public class NetworkFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NetworkFragment";
    private RecyclerView web_musicList;
    public LinearLayoutManager layoutManager;
    public WebRecyclerViewAdapter adapter;
    public List<NetMusic.DataBean> netMusicList = new ArrayList<NetMusic.DataBean>();
    private EditText search_text;
    private Button btn_search;
    private String search_s;
    Context context;
    MainActivity mainActivity;

    public NetworkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.network_fragment, container, false);

        web_musicList = view.findViewById(R.id.web_musicList);
        layoutManager = new LinearLayoutManager(MyApplication.getContext());
        search_text = view.findViewById(R.id.search_text);
        btn_search = view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                search_s = search_text.getText().toString().trim();
                if (search_s.equals("")){
                    Toast.makeText(context, "可能没有这首歌", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "可能没有这首歌" + search_s);
                }else{
                    if (netMusicList == null) {
                        getNetMusicList(search_s);
                    }else{
                        getNetMusicList(search_s);
                    }
                }

                Log.d(TAG, "NetworkFragment--22--" + search_s);
                break;
            default:
                break;
        }

    }

    private void getNetMusicList(String s) {
        Api mApi = HttpUtil.getWebMusic();
        Call<NetMusic> musicCall = mApi.getMusic("579621905", s, "song", 20, 0);
        musicCall.enqueue(new Callback<NetMusic>() {
            @Override
            public void onResponse(Call<NetMusic> call, final Response<NetMusic> response) {
                if (response.code() != 400) {
                    Log.d(TAG, "NetworkFragment--87--");
                    netMusicList = response.body().getData();
                    if (netMusicList != null) {
                        //写个适配器
                        web_musicList.setLayoutManager(layoutManager);
                        adapter = new WebRecyclerViewAdapter(getContext(), netMusicList);
                        web_musicList.setAdapter(adapter);
                    } else {
                        nList();
                        Toast.makeText(context, "可能没有这首歌", Toast.LENGTH_SHORT).show();
                    }

                    Log.i(TAG, "歌名--" + netMusicList.get(0).getName() + netMusicList.get(0).getSinger());
                    Log.i(TAG, "显示了--" + netMusicList.size() + "首歌曲");


                }

            }

            @Override
            public void onFailure(Call<NetMusic> call, Throwable t) {
            }
        });
    }

    public List nList() {
        List nList = Collections.EMPTY_LIST;
        return nList;
    }

    public void setNetMusicList(String s) {
        getNetMusicList(s);
    }


}
