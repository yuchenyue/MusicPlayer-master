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

import entity.ListMusic;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "ListRecyclerViewAdapter";
    public Context context;
    private List<ListMusic.DataBean.Song> listMusicList = new ArrayList<>();

    public ListRecyclerViewAdapter(Context context, List<ListMusic.DataBean.Song> listMusicList) {
        this.context = context;
        this.listMusicList = listMusicList;
    }

    @Override
    public ListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListRecyclerViewAdapter.ViewHolder holder, int position) {
        ListMusic.DataBean.Song music = listMusicList.get(position);
        holder.list_name.setText(music.getName());
        holder.list_author.setText(music.getSinger());
        Glide.with(context)
                .load(music.getPic())
                .into(holder.image_list);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listMusicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button item_list;
        TextView list_name, list_author;
        ImageView image_list;
        public ViewHolder(View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.list_name);
            list_author = itemView.findViewById(R.id.list_author);
            image_list = itemView.findViewById(R.id.image_list);
            item_list = itemView.findViewById(R.id.item_list);
            //将创建的View注册点击事件
            itemView.setOnClickListener(this);
            item_list.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }
    //item里面有多个控件可以点击（item+item内部控件）
    public enum ViewName{
        ITEM,PRACTISE;
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