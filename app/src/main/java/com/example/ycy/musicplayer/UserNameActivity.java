package com.example.ycy.musicplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import utils.MyApplication;

public class UserNameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UserNameActivity";

    ImageView use_giveup, use_save;
    TextView tv_use_name;
    EditText et_use_name;
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        MyApplication.getInstance().addActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("编辑");

        initView();

    }

    private void initView() {
        login_button = (Button) findViewById(R.id.login_button);
        if (MyApplication.getLogin() == true) {
            login_button.setText("退出登录");
        }else {
            login_button.setText("登录");
        }
        login_button.setOnClickListener(this);
        et_use_name = (EditText) findViewById(R.id.et_use_name);
        tv_use_name = (TextView) findViewById(R.id.tv_use_name);
        use_giveup = (ImageView) findViewById(R.id.use_giveup);
        use_giveup.setOnClickListener(this);
        use_save = (ImageView) findViewById(R.id.use_save);
        use_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.use_giveup:
                setResult(RESULT_CANCELED, null);
                finish();
                break;
            case R.id.use_save:
                String usename = et_use_name.getText().toString();
                if (usename.equals("")) {
                    Toast.makeText(getApplicationContext(), "昵称不能为空，还是取个名字吧~", Toast.LENGTH_LONG).show();
                } else {
                    Uri data = Uri.parse(usename);
                    Intent intent = new Intent(null, data);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                Log.i(TAG, "用户信息" + usename);
                break;
            case R.id.login_button:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            default:
                break;
        }

    }
}
