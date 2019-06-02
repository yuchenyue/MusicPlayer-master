package fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ycy.musicplayer.MainActivity;
import com.example.ycy.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

import adapter.WebRecyclerViewAdapter;
import entity.NetMusic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviceApi.Api;
import utils.HttpUtil;

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
    MainActivity mainActivity;
    TextView tv_empty_net;
    Button item_search;

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
        View view = inflater.inflate(R.layout.fragment_network, container, false);
        web_musicList = view.findViewById(R.id.web_musicList);
        layoutManager = new LinearLayoutManager(getActivity());
        web_musicList.setLayoutManager(layoutManager);
        search_text = view.findViewById(R.id.search_text);
        btn_search = view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        tv_empty_net = view.findViewById(R.id.tv_empty_net);
        item_search = view.findViewById(R.id.item_search);
        return view;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                search_s = search_text.getText().toString().trim();
                if (TextUtils.isEmpty(search_s)) {
                    Toast.makeText(getActivity(), "可能没有这首歌", Toast.LENGTH_SHORT).show();
                    netMusicList.clear();
                    tv_empty_net.setVisibility(View.VISIBLE);
                } else {
                    if (netMusicList == null) {
                        tv_empty_net.setVisibility(View.GONE);
                        getNetMusicList(search_s);
                    } else {
                        tv_empty_net.setVisibility(View.GONE);
                        getNetMusicList(search_s);
                    }
                }
                break;
            default:
                break;
        }

    }


    private void getNetMusicList(String s) {
        Api mApi = HttpUtil.getWebMusic();
        Call<NetMusic> musicCall = mApi.getMusic(s, "song",1);
        musicCall.enqueue(new Callback<NetMusic>() {
            @Override
            public void onResponse(Call<NetMusic> call, final Response<NetMusic> response) {
                if (response.code() == 200) {
                    netMusicList = response.body().getData();
                    adapter = new WebRecyclerViewAdapter(getActivity(), netMusicList);
                    web_musicList.setAdapter(adapter);
                    adapter.setOnItemClickListener(MyItemClickListener);
                }else {
                    Toast.makeText(getActivity(),"服务器离线",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NetMusic> call, Throwable t) {
                Toast.makeText(getActivity(),"服务器离线",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private WebRecyclerViewAdapter.OnItemClickListener MyItemClickListener = new WebRecyclerViewAdapter.OnItemClickListener(){

        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()){
                case R.id.item_search:
                    searchdialog(position);
                    break;
                default:
                    Toast.makeText(getActivity(),"item"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void searchdialog(int position){
            AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
            builder.setTitle("是否下载歌曲？" ) ;
            builder.setMessage(netMusicList.get(position).getSinger() +"__"+ netMusicList.get(position).getName() ) ;
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //做下载
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
        }
    };
}
