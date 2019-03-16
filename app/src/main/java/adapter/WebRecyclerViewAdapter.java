package adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ycy.musicplayer.MainActivity;
import com.example.ycy.musicplayer.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import entity.NetMusic;
import services.MusicService;
import utils.MyApplication;

public class WebRecyclerViewAdapter extends RecyclerView.Adapter<WebRecyclerViewAdapter.ViewHolder> {

    private static final String TAG ="NetworkFragment";
    public Context context;
    private List<NetMusic.DataBean> netMusicList = new ArrayList<>();
    private int position;

    public WebRecyclerViewAdapter(Context context, List<NetMusic.DataBean> netMusicList) {
        this.context = context;
        this.netMusicList = netMusicList;
    }

    @Override
    public WebRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.music_item_one,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = holder.getAdapterPosition();
                startMusic();
            }
        });
        return holder;
    }

    private void startMusic() {
        Log.i(TAG,"点击了--"+position);
        Intent intent = new Intent(context, MusicService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putBoolean("isPlay",true);
        bundle.putSerializable("netMusicList",(Serializable) netMusicList);
        intent.putExtras(bundle);
        context.startService(intent);
        MyApplication.setIsWeb(true);
        Toast.makeText(context,netMusicList.get(position).getName(),Toast.LENGTH_SHORT).show();
        Log.i(TAG,"点击了--"+ netMusicList.get(position).getName());
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView music_Name,author;
        ImageView image_net;
        public ViewHolder(View view) {
            super(view);
            music_Name = view.findViewById(R.id.musicName);
            author = view.findViewById(R.id.author);
            image_net = view.findViewById(R.id.image_net);
        }
    }
}