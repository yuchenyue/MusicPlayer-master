package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ycy.musicplayer.MainActivity;
import com.example.ycy.musicplayer.R;
import com.example.ycy.musicplayer.SongSheetActivity;

/**
 * Created by Administrator on 2019/1/14.
 */

public class LetworkFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LetworkFragment";
    ImageView iv_cover;
    TextView tv_music_1,tv_music_2,tv_music_3;
    View v_divider;
    FrameLayout jp_gd;
    MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.let_fragment, container, false);
        iv_cover = view.findViewById(R.id.iv_cover);
        tv_music_1 = view.findViewById(R.id.tv_music_1);
        tv_music_2 = view.findViewById(R.id.tv_music_2);
        tv_music_3 = view.findViewById(R.id.tv_music_3);
        v_divider = view.findViewById(R.id.v_divider);
        jp_gd = view.findViewById(R.id.jp_gd);
        jp_gd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jp_gd:
                Toast.makeText(getContext(),"精品歌单",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), SongSheetActivity.class));
                break;
        }
    }
}
