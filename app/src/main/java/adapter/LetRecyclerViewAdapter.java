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

import entity.LetMusic;
import entity.NetMusic;

public class LetRecyclerViewAdapter extends RecyclerView.Adapter<LetRecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "WebRecyclerViewAdapter";
    public Context context;
    private List<LetMusic.DataBean> netMusicList = new ArrayList<>();

    public LetRecyclerViewAdapter(Context context, List<LetMusic.DataBean> netMusicList) {
        this.context = context;
        this.netMusicList = netMusicList;
    }

    @Override
    public LetRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_teo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LetRecyclerViewAdapter.ViewHolder holder, int position) {
        LetMusic.DataBean music = netMusicList.get(position);
        holder.music_Name.setText(music.getTitle());
        holder.author.setText(music.getCreator());
        Glide.with(context)
                .load(music.getCoverImgUrl())
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

}