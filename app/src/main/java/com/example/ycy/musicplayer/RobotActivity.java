package com.example.ycy.musicplayer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.ChatListAdapter;
import entity.ChatMessage;
import utils.MyApplication;
import utils.RobotUtils;

public class RobotActivity extends AppCompatActivity implements View.OnClickListener {

    private List<ChatMessage> mlist;
    private ListView lv_chat_list;
    private EditText et_send;
    private Button btn_send;
    private ChatListAdapter chatAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ChatMessage formMessage = (ChatMessage) msg.obj;
            mlist.add(formMessage);
            chatAdapter.notifyDataSetChanged();
            lv_chat_list.setSelection(mlist.size() - 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_robot);
        MyApplication.getInstance().addActivity(this);
        Toolbar mToolbar=(Toolbar)findViewById(R.id.toolbar_robot);
        setSupportActionBar(mToolbar);
        //设置是否有返回箭头
        getSupportActionBar().setTitle("小助手");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initDate();

    }

    private void initDate() {
        mlist = new ArrayList<ChatMessage>();
        mlist.add(new ChatMessage("您好!什么我都可以回答你哦！我可是无所不知的！", ChatMessage.Type.INCOUNT, new Date()));
        chatAdapter = new ChatListAdapter(this, mlist);
        lv_chat_list.setAdapter(chatAdapter);
    }

    /**
     * 初始化实例
     */
    private void initView() {
        lv_chat_list = (ListView) findViewById(R.id.lv_chat_list);
        et_send = (EditText) findViewById(R.id.et_send);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 按钮监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                send();
                break;
            default:
                break;
        }
    }

    /**
     * 发送信息按钮事件
     */
    private void send(){
        // 1.判断是否输入内容
        final String send_message = et_send.getText().toString();
        if (TextUtils.isEmpty(send_message)) {
            Toast.makeText(RobotActivity.this, "对不起，不能为空！",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // 2.自己输入的内容也是一条记录，记录刷新
        ChatMessage toMessage = new ChatMessage();
        toMessage.setMessage(send_message);
        toMessage.setData(new Date());
        toMessage.setType(ChatMessage.Type.OUTCOUNT);
        mlist.add(toMessage);
        chatAdapter.notifyDataSetChanged();
        et_send.setText("");
        // 3.发送你的消息，去服务器端，返回数据
        Thread thread = new Thread(new Runnable() {
            public void run() {
                ChatMessage fromMessage = RobotUtils.sendMessage(send_message);
                Message m = Message.obtain();
                m.what = 0x1;
                m.obj = fromMessage;
                mHandler.sendMessage(m);
            }
        });
        thread.start();
    }
}
