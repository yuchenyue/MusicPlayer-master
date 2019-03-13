package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ycy.musicplayer.R;

import java.text.SimpleDateFormat;
import java.util.List;

import entity.ChatMessage;

public class ChatListAdapter extends BaseAdapter {

    private List<ChatMessage> mList;
    private LayoutInflater inflater;

    //定义常量,区分收发信息
    public static final int chat_left = 1;//收
    public static final int chat_right = 2;//发

    //构造器
    public ChatListAdapter(Context context,List<ChatMessage> mList) {
        inflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.isEmpty()? 0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = mList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            // 通过ItemType加载不同的布局
            if (getItemViewType(position) == 0) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_item, parent,false);
                viewHolder = new ViewHolder();
                viewHolder.chat_time = (TextView) convertView.findViewById(R.id.chat_left_time);
                viewHolder.chat_message = (TextView) convertView.findViewById(R.id.chat_left_message);
            } else {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_item, parent,false);
                viewHolder = new ViewHolder();
                viewHolder.chat_time = (TextView) convertView.findViewById(R.id.chat_right_time);
                viewHolder.chat_message = (TextView) convertView.findViewById(R.id.chat_right_message);
            }
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设置数据
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ViewHolder vh = (ViewHolder) convertView.getTag();
        vh.chat_time.setText(df.format(chatMessage.getData()));
        vh.chat_message.setText(chatMessage.getMessage());
        return convertView;
    }

    //获取当前Item的类型
    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mList.get(position);
        if (chatMessage.getType() == ChatMessage.Type.INCOUNT){
            return 0;
        }
        return 1;
    }

    private class ViewHolder {
        private TextView chat_time, chat_message;
    }

    //返回所有Layout数据
    @Override
    public int getViewTypeCount() {
        return 2;
    }

}