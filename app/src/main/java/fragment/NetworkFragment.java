package fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ycy.musicplayer.MainActivity;
import com.example.ycy.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

import adapter.NetMusicListAdapter;
import entity.NetMusic;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviceApi.Api;
import utils.HttpUtil;

/**
 * Created by Administrator on 2019/1/14.
 */

public class NetworkFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String TAG = "NetworkFragment";
    ListView network_fragment_list_view;
    private LayoutInflater inflater;
    public List<NetMusic.DataBean> webMusicList = new ArrayList<>();
    public NetMusicListAdapter netMusicListAdapter;
    List<NetMusic> netlist;
    MainActivity mainActivity;

    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    public static NetworkFragment newInstance() {
        NetworkFragment net = new NetworkFragment();
        return net;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.network_fragment, container, false);
        network_fragment_list_view = view.findViewById(R.id.network_fragment_list_view);
        network_fragment_list_view.setOnItemClickListener(this);
        network_fragment_list_view.setAdapter(netMusicListAdapter);
        loadData();
        getMusicList("江南");
        return view;
    }

    public void loadData(){

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void getMusicList(String s) {
        Api mApi = HttpUtil.getWebMusic();
        Call<NetMusic> musicCall = mApi.getMusic("579621905",s,"song",20,0);
        musicCall.enqueue(new Callback<NetMusic>() {
            @Override
            public void onResponse(Call<NetMusic> call, final Response<NetMusic> response) {
                if (response.code() != 400) {
                    webMusicList = response.body().getData();

                }
            }
            @Override
            public void onFailure(Call<NetMusic> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {

    }
}
