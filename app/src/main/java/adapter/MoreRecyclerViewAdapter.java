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

public class MoreRecyclerViewAdapter extends RecyclerView.Adapter<MoreRecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "WebRecyclerViewAdapter";
    public Context context;
    private List<LetMusic.DataBean> letMusicList = new ArrayList<>();

    public MoreRecyclerViewAdapter(Context context, List<LetMusic.DataBean> letMusicList) {
        this.context = context;
        this.letMusicList = letMusicList;
    }

    @Override
    public MoreRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_let, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoreRecyclerViewAdapter.ViewHolder holder, int position) {
        LetMusic.DataBean music = letMusicList.get(position);
        holder.let_name.setText(music.getName());
        holder.let_songnum.setText("歌曲数：" + music.getTrackCount());
        holder.let_playnum.setText("播放量：" + music.getPlayCount());
        Glide.with(context)
                .load(music.getCoverImgUrl())
                .error(R.drawable.default_cover)
                .into(holder.image_let);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return letMusicList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView let_name,let_songnum, let_playnum;
        ImageView image_let;
        Button item_let;

        public ViewHolder(View view) {
            super(view);
            let_name = view.findViewById(R.id.let_name);
            let_songnum = view.findViewById(R.id.let_songnum);
            let_playnum = view.findViewById(R.id.let_playnum);
            image_let = view.findViewById(R.id.image_let);
            item_let = view.findViewById(R.id.item_let);
            view.setOnClickListener(this);
            item_let.setOnClickListener(this);
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