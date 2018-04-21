package com.example.lenovo.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.myapplication.bean.UserBean;
import com.example.lenovo.myapplication.manger.ActivityCollector;
import com.example.lenovo.myapplication.manger.MyWebSocketListener;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.WebSocket;
import okio.BufferedSink;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    UserBean userBean;
    ProgressBar lvBar;
    TextView textName,textLv,textExp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton btnCard = (ImageButton) findViewById(R.id.btn_card);
        btnCard.setOnClickListener(this);
        RelativeLayout selfInfoPart = (RelativeLayout)findViewById(R.id.self_info_part);
        selfInfoPart.setOnClickListener(this);
        textName = (TextView)findViewById(R.id.text_name);
        textLv = (TextView)findViewById(R.id.text_Lv);
        textExp = (TextView)findViewById(R.id.text_exp);
        lvBar = (ProgressBar)findViewById(R.id.main_lv_bar);
        userBean = Data.getUserInfo();
        textName.setText(userBean.getUserName());   //忘记是id还是name了
        Log.d("Main",userBean.toString());
        int lv = userBean.getUserLv();
        textLv.setText("Lv."+lv);
        textExp.setText(userBean.getUserLv()+"/"+Data.getExp(lv));
        lvBar.setProgress(userBean.getUserExp());
        lvBar.setMax(1000);   //数据库设置经验等级
        // 头像的问题，设置0-9 共十个头像，取手机号码尾数设置头像

        Button btnStart = (Button)findViewById(R.id.btn_start_game);
        btnStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.btn_friend:
                break;
            case R.id.btn_card:
                Data.webSocket.close(1000,"0");
                intent = new Intent(this, CardActivity.class);
                //startActivity(intent);
                break;
            case R.id.self_info_part:
                intent = new Intent(this,SelfSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_start_game:
                openWebsocket();
                break;
            default:
                ;
        }
    }

    public void openWebsocket(){
        UserBean userBean = Data.getUserInfo();
        Gson gson = new Gson();
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Data.SOCKET_SERVER).build();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", String.valueOf(userBean.getUserId()))
                .add("type","1")
                .add("userBean",gson.toJson(userBean))
                .build();
        Data.webSocket = httpClient.newWebSocket(request,new MyWebSocketListener());
        //httpClient.dispatcher().executorService().shutdown();
        Log.d("WebSocket","------------");

    }

    @Override
    protected void onResume() {
        super.onResume();
        userBean = Data.getUserInfo();
        textName.setText(userBean.getUserName());
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
}
