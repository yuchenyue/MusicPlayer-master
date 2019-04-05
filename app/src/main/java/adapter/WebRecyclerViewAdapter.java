package adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ycy.musicplayer.R;
import java.util.ArrayList;
import java.util.List;

import entity.NetMusic;
import services.MusicService;

public class WebRecyclerViewAdapter extends RecyclerView.Adapter<WebRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "WebRecyclerViewAdapter";
    public Context context;
    private List<NetMusic.DataBean> netMusicList = new ArrayList<>();
    private int nposition;
    MusicService musicService;

    public WebRecyclerViewAdapter(Context context, List<NetMusic.DataBean> netMusicList) {
        this.context = context;
        this.netMusicList = netMusicList;
    }

    @Override
    public WebRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.music_item_one, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nposition = holder.getAdapterPosition();
                startMusic();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(WebRecyclerViewAdapter.ViewHolder holder, int position) {
        NetMusic.DataBean music = netMusicList.get(position);
        holder.music_Name.setText(music.getName());
        holder.author.setText(music.getSinger());
        Glide.with(context)
                .load(music.getPic())
                .into(holder.image_net);
    }

    @Override
    public int getItemCount() {
        return netMusicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView music_Name, author;
        ImageView image_net;

        public ViewHolder(View view) {
            super(view);
            music_Name = view.findViewById(R.id.musicName);
            author = view.findViewById(R.id.author);
            image_net = view.findViewById(R.id.image_net);
        }
    }

    private void startMusic() {
        Dialog();
        Toast.makeText(context, netMusicList.get(nposition).getName(), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "点击了-----" + netMusicList.get(nposition).getName());
        Log.i(TAG, "点击了-----" + netMusicList.get(nposition).getUrl());
        Log.i(TAG, "点击了-----" + netMusicList.get(nposition).getLrc());
    }

    public void Dialog(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        builder.setTitle("是否下载歌曲？" ) ;
        builder.setMessage(netMusicList.get(nposition).getSinger() +"__"+ netMusicList.get(nposition).getName() ) ;
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void download(){
        Toast.makeText(context, netMusicList.get(nposition).getLrc(), Toast.LENGTH_SHORT).show();

    }

}