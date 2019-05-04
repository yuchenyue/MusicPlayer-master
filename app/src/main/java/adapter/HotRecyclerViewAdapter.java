package adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ycy.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

import entity.LetMusic;

public class HotRecyclerViewAdapter extends RecyclerView.Adapter<HotRecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "WebRecyclerViewAdapter";
    public Context context;
    private List<LetMusic.DataBean> hotMusicList = new ArrayList<>();
    private boolean fadeTips = false;//是否隐藏了底部

    public boolean isFadeTips() {
        return fadeTips;
    }

    public HotRecyclerViewAdapter(Context context, List<LetMusic.DataBean> hotMusicList) {
        this.context = context;
        this.hotMusicList = hotMusicList;
    }

    @Override
    public HotRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_hot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotRecyclerViewAdapter.ViewHolder holder, int position) {
        LetMusic.DataBean music = hotMusicList.get(position);
        holder.hot_name.setText(music.getTitle());
        holder.hot_author.setText(music.getCreator());
        holder.hot_songnum.setText("歌曲数：" + music.getSongNum());
        holder.hot_playnum.setText("播放量：" + music.getPlayCount());
        Glide.with(context)
                .load(music.getCoverImgUrl())
                .into(holder.image_hot);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return hotMusicList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView hot_name, hot_author, hot_songnum, hot_playnum;
        ImageView image_hot;
        Button item_hot;

        public ViewHolder(View view) {
            super(view);
            hot_name = view.findViewById(R.id.hot_name);
            hot_author = view.findViewById(R.id.hot_author);
            hot_songnum = view.findViewById(R.id.hot_songnum);
            hot_playnum = view.findViewById(R.id.hot_playnum);
            image_hot = view.findViewById(R.id.image_hot);
            item_hot = view.findViewById(R.id.item_hot);
            view.setOnClickListener(this);
            item_hot.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }


    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


}