package com.example.ycy.musicplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="Main2Activity";

    ImageView use_giveup,use_save;
    TextView tv_use_name;
    EditText et_use_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("编辑");

        initView();

    }

    private void initView() {

        et_use_name = (EditText) findViewById(R.id.et_use_name);
        tv_use_name = (TextView) findViewById(R.id.tv_use_name);
        use_giveup = (ImageView) findViewById(R.id.use_giveup);
        use_giveup.setOnClickListener(this);
        use_save = (ImageView) findViewById(R.id.use_save);
        use_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.use_giveup:
                setResult(RESULT_CANCELED, null);
                finish();
                break;
            case R.id.use_save:
                String usename = et_use_name.getText().toString().trim();
                Uri data = Uri.parse(usename);
                Intent result = new Intent(null,data);
                setResult(RESULT_OK,result);
                Log.i(TAG,"用户信息" + usename);
                finish();
                break;
            default:
                break;
        }

    }
}
