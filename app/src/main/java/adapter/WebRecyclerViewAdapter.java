package adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ycy.musicplayer.R;
import java.util.ArrayList;
import java.util.List;

import entity.NetMusic;

public class WebRecyclerViewAdapter extends RecyclerView.Adapter<WebRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "WebRecyclerViewAdapter";
    public Context context;
    private List<NetMusic.DataBean> netMusicList = new ArrayList<>();
    private int nposition;

    public WebRecyclerViewAdapter(Context context, List<NetMusic.DataBean> netMusicList) {
        this.context = context;
        this.netMusicList = netMusicList;
    }

    @Override
    public WebRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.music_item_net, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WebRecyclerViewAdapter.ViewHolder holder, int position) {
        NetMusic.DataBean music = netMusicList.get(position);
        holder.music_Name.setText(music.getName());
        holder.author.setText(music.getSinger());
        holder.item_pause.setTag(position);
        Glide.with(context)
                .load(music.getPic())
                .into(holder.image_net);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return netMusicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView music_Name, author;
        ImageView image_net;
        Button item_pause;
        public ViewHolder(View view) {
            super(view);
            music_Name = view.findViewById(R.id.musicName);
            author = view.findViewById(R.id.author);
            image_net = view.findViewById(R.id.image_net);
            item_pause = view.findViewById(R.id.item_pause);
            view.setOnClickListener(this);
            item_pause.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(v,getAdapterPosition());
                item_pause.setVisibility(View.VISIBLE);
            }
        }
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, int position);
        void onItemLongClick(View v);
    }

    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
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
    public void download() {
        Toast.makeText(context, netMusicList.get(nposition).getLrc(), Toast.LENGTH_SHORT).show();
    }

}