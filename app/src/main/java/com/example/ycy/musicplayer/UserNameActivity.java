package com.example.ycy.musicplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import utils.MyApplication;

public class UserNameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UserNameActivity";

    TextView tv_use_name;
    EditText et_use_name;
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        MyApplication.getInstance().addActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_username);
        setSupportActionBar(toolbar);
        toolbar.setTitle("个人中心");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();

    }

    private void initView() {
        login_button = (Button) findViewById(R.id.login_button);
        if (MyApplication.getLogin() == true) {
            login_button.setText("退出登录");
        } else {
            login_button.setText("登录");
        }
        login_button.setOnClickListener(this);
        et_use_name = (EditText) findViewById(R.id.et_use_name);
        tv_use_name = (TextView) findViewById(R.id.tv_use_name);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
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
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                break;
        }

    }
}
