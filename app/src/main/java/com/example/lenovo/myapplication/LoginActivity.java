package com.example.lenovo.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.myapplication.bean.UserBean;
import com.example.lenovo.myapplication.manger.ActivityCollector;
import com.example.lenovo.myapplication.manger.SharedPreManger;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    SharedPreManger sharedPreManger;
    private ProgressBar loginBar;
    private boolean isLogin = false;
    private Button btnLogin,btnRegister;
    private TextView loadingText;
    Thread thread;
    int MAX = 100;
    int cur = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreManger = new SharedPreManger("UserInfo",this);
        isLogin = sharedPreManger.getIsLogin();
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        loginBar = (ProgressBar)findViewById(R.id.login_progressBar);
        loadingText = (TextView)findViewById(R.id.text_loading);

        loginBar.setMax(MAX);
        Log.d("sharePre",String.valueOf(isLogin));
        if (isLogin) {
            btnLogin.setVisibility(View.INVISIBLE);
            loginBar.setVisibility(View.INVISIBLE);
            loadingText.setVisibility(View.INVISIBLE);
            btnRegister.setText("进入游戏");
            //btnRegister.setVisibility(View.);
        } else {
            loginBar.setVisibility(View.INVISIBLE);
            loadingText.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
        }
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        setExpArray();
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        switch (view.getId()){
            case R.id.btn_register:
                if (isLogin) {
                    btnRegister.setVisibility(View.INVISIBLE);
                    loginBar.setVisibility(View.VISIBLE);
                    loadingText.setVisibility(View.VISIBLE);
                    progressGo();
                    getUserInfo();
                } else {
                    intent.putExtra("actionType",1);
                    intent.putExtra("actionName","注册");
                    startActivity(intent);
                }
                break;
            case R.id.btn_login:
                intent.putExtra("actionType",0);
                intent.putExtra("actionName","登录");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    // 传给登录界面两个参数，，名称和Type：0 登录，1 注册。
    public void getUserInfo(){
        String phoneN = sharedPreManger.getUserPhone();
        String pwd = sharedPreManger.getUserPwd();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Data.GET_USER_BY_PWD+phoneN+Data.GET_USER_BY_PWD1+pwd).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("web--getUserByPwd","请求失败");
                cur = MAX;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(LoginActivity.this,"请求失败，正在重试",Toast.LENGTH_SHORT).show();

                        loginBar.setProgress(cur);
                        goNewRequest();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();

                Log.d("web--Json",json);
                UserBean userBean = gson.fromJson(json,UserBean.class);
                Log.d("web--Json",userBean.getUserPhone());
                Data.setUserInfo(userBean);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginBar.setProgress(MAX);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });
            }
        });
    }
    public void progressGo(){
        cur = 0;
        loginBar.setProgress(cur);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(cur<MAX){
                    loginBar.incrementProgressBy(1);
                    cur++;
                    try {
                        Thread.sleep(300); //强制线程休眠0.1秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("确定要退出吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCollector.finishAll();
                System.exit(0);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }
    public void goNewRequest() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("请求失败").setMessage("是否重新获取？");
        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressGo();
                getUserInfo();
            }
        });
        dialog.setNegativeButton("否", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCollector.finishAll();
            }
        });
        dialog.show();
    }
    public void setExpArray(){
        int[] Exp = new int[101];
        int nextExp = 0;
        for(int i = 1;i<101;i++) {
            nextExp += i*(i+5)*10;
            if (i == 100)
                Log.d("EXP"," "+nextExp);
            Exp[i]=nextExp;
        }
        Data.setExp(Exp);
    }
}
