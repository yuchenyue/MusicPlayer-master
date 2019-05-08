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
        layoutManager = new LinearLayoutManager(MyApplication.getContext());
        search_text = view.findViewById(R.id.search_text);
        btn_search = view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        tv_empty_net = view.findViewById(R.id.tv_empty_net);
        item_search = view.findViewById(R.id.item_search);
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                search_s = search_text.getText().toString().trim();
                if (TextUtils.isEmpty(search_s)) {
//                    Toast.makeText(context, "可能没有这首歌", Toast.LENGTH_SHORT).show();
                    netMusicList.clear();
                    tv_empty_net.setVisibility(View.VISIBLE);
                    Log.d(TAG, "可能没有这首歌" + search_s);
                } else {
                    if (netMusicList == null) {
                        tv_empty_net.setVisibility(View.GONE);
                        getNetMusicList(search_s);
                    } else {
                        tv_empty_net.setVisibility(View.GONE);
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
                if (response.code() == 200) {
                    Log.d(TAG, "NetworkFragment--87--");
                    netMusicList = response.body().getData();
                    //写个适配器
                    web_musicList.setLayoutManager(layoutManager);
                    adapter = new WebRecyclerViewAdapter(getContext(), netMusicList);
                    web_musicList.setAdapter(adapter);

                    adapter.setOnItemClickListener(MyItemClickListener);
                    Log.i(TAG, "歌名--" + netMusicList.get(0).getName() + netMusicList.get(0).getSinger());
                    Log.i(TAG, "显示了--" + netMusicList.size() + "首歌曲");
                }else {
                    Toast.makeText(getContext(),"服务器离线",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NetMusic> call, Throwable t) {
            }
        });
    }

    public void setNetMusicList(String s) {
        getNetMusicList(s);
    }

    private WebRecyclerViewAdapter.OnItemClickListener MyItemClickListener = new WebRecyclerViewAdapter.OnItemClickListener(){

        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()){
                case R.id.item_search:
                    searchdialog(position);
                    break;
                default:
                    Toast.makeText(getContext(),"item"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void searchdialog(int position){
            AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
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
