package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ycy.musicplayer.R;

import java.util.List;

import entity.Music;
import entity.NetMusic;

/**
 * Created by Administrator on 2019/2/28.
 */

public class NetMusicListAdapter extends BaseAdapter {

    private Context context;
    NetMusic netMusics;
    private List<NetMusic> netlist;

    public NetMusicListAdapter(Context context, List<NetMusic> netlist) {
        this.context = context;
        this.netlist = netlist;
    }

    @Override
    public int getCount() {
        return netlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
      
       return null;
    }

    public class Musicholder{
        public TextView musicName,author;
        public ImageView image;
    }
}
