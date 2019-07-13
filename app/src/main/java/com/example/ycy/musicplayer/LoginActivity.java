package com.example.ycy.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import manage.DBHelper;
import permission.PermissionUtils;
import permission.request.IRequestPermissions;
import permission.request.RequestPermissions;
import permission.requestresult.IRequestPermissionsResult;
import permission.requestresult.RequestPermissionsResultSetApp;
import retrofit2.http.GET;
import utils.MyApplication;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_account, edit_password;
    private TextView nologin;
    private Button btn_login, btn_register;
    private CheckBox checkBox;
    private ImageButton openpwd;
    private boolean flag = false;
    private String account, password;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
    IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyApplication.getInstance().addActivity(this);
        sharedPreferences = getSharedPreferences("remember", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dbHelper = new DBHelper(this, "Data.db", null, 1);

        initView();

        if (!requestPermissions()) {
            return;
        }
        isRemember();
    }

    private void initView() {
        edit_account = (EditText) findViewById(R.id.edit_account);
        edit_account.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_account.clearFocus();
                }
                return false;
            }
        });
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_password.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_password.getWindowToken(), 0);
                }
                return false;
            }
        });
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        openpwd = (ImageButton) findViewById(R.id.btn_openpwd);
        nologin = (TextView) findViewById(R.id.nologin);
        nologin.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        openpwd.setOnClickListener(this);
    }

    /**
     * 判断是否是否保存过密码
     */
    public void isRemember() {
        SharedPreferences prefer = getSharedPreferences("remember", MODE_PRIVATE);
        boolean isRemember = prefer.getBoolean("remember", false);
        if (isRemember) {
            edit_account.setText(prefer.getString("name", ""));
            edit_password.setText(prefer.getString("pass", ""));
            checkBox.setChecked(true);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (!requestPermissions()) {
                    return;
                }
                if (edit_account.getText().toString().trim().equals("") | edit_password.getText().
                        toString().trim().equals("")) {
                    Toast.makeText(this, "请输入账号或者注册账号！", Toast.LENGTH_SHORT).show();
                } else {
                    readUserInfo();

                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_openpwd:
                if (flag == true) {//不可见
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    openpwd.setBackgroundResource(R.drawable.visible);
                } else {
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    openpwd.setBackgroundResource(R.drawable.invisible);
                }
                break;
            case R.id.nologin:
                MyApplication.setLogin(false);
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
    }


    /**
     * 读取UserData.db中的用户信息
     */
    protected void readUserInfo() {
        if (login(edit_account.getText().toString(), edit_password.getText().toString())) {
            Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("Username", edit_account.getText().toString());
            MyApplication.setLogin(true);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "账户或密码错误，请重新输入！！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 验证登录信息
     */
    public boolean login(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "Select * from usertable where username=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        boolean rember = false;
        while (cursor.moveToFirst()) {
            cursor.close();
            rember = true;
            if (rember) {
                SharedPreferences.Editor editor = getSharedPreferences("remember", MODE_PRIVATE).edit();
                if (checkBox.isChecked()) {
                    editor.putBoolean("remember", true);
                    editor.putString("name", edit_account.getText().toString().trim());
                    editor.putString("pass", edit_password.getText().toString().trim());
                    editor.commit();
                } else {
                    editor.clear();
                }
                editor.commit();
            }
            return true;
        }

        return false;
    }

    /**
     * 申请权限
     */
    private boolean requestPermissions() {
        String[] permissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET};
        return requestPermissions.requestPermissions(this, permissions, PermissionUtils.ResultCode1);
    }

    //用户授权操作结果（可能授权了，也可能未授权）
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //用户给APP授权的结果
        //判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
        if (requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults)) {
            //请求的权限全部授权成功，此处可以做自己想做的事了
            //输出授权结果
            Toast.makeText(LoginActivity.this, "授权成功，请重新点击刚才的操作！", Toast.LENGTH_LONG).show();
        } else {
            //输出授权结果
            Toast.makeText(LoginActivity.this, "请给APP授权，否则功能无法正常使用！", Toast.LENGTH_LONG).show();
        }
    }
}
