package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ycy.musicplayer.R;

import java.util.List;

import utils.MusicUtil;
import entity.Music;

/**
 * Created by Administrator on 2019/1/16.
 */

public class MusicAdapter extends BaseAdapter {


    private Context context;
    private List<Music> musics;

    public MusicAdapter(Context context, List<Music> musics) {
        this.context = context;
        this.musics = musics;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Musicholder musicholder = new Musicholder();
        if (view == null) {
            view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.music_item_local, null);
            musicholder.albumImage = view.findViewById(R.id.albumImage);
            musicholder.t_song = view.findViewById(R.id.t_song);
            musicholder.t_songer = view.findViewById(R.id.t_songer);
            musicholder.t_duration = view.findViewById(R.id.t_duration);
            view.setTag(musicholder);
        } else {
            musicholder = (Musicholder) view.getTag();
        }
        Music music = musics.get(position);
        musicholder.t_song.setText(music.getSong());
        musicholder.t_songer.setText(music.getSonger());
        String time = MusicUtil.formatTime(music.getDuration());
        musicholder.t_duration.setText(time);
        //列表显示图片
        Bitmap albumBitmapItem = MusicUtil.getArtwork(context, music.getId(), music.getAlbum_id(), true, true);
        musicholder.albumImage.setImageBitmap(albumBitmapItem);
        return view;
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int position) {
        return musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Musicholder {
        public TextView t_song, t_songer, t_duration;
        public ImageView albumImage;
    }
}
