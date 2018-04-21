package com.example.lenovo.myapplication;


import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class  CardActivity extends BaseActivity implements View.OnClickListener {
    String txtphoneNo;
    private List<HashMap<String,Object>> gridList = new ArrayList<HashMap<String, Object>>();
    GridView gridCard;
    SimpleAdapter gridAdapter;
    private int ACTIVE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Button btnActive = (Button)findViewById(R.id.btn_active);
        Button btnInactive = (Button)findViewById(R.id.btn_inactive);
        ImageView btnClose = (ImageView)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
        btnActive.setOnClickListener(this);
        btnInactive.setOnClickListener(this);
        gridCard = (GridView)findViewById(R.id.grid_card);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_active:
                // TODO 在加载游戏时便请求用户卡信息，存入data，
                // 决定全部存在一个数组里，遍历2遍 54*2，而且卡位置不用考虑颠倒  ok
                // 或者使用两个数组，只需遍历一般 54 ，但是新的卡片加入时会颠倒。。
                // TODO 在升级时修改data中卡信息，并且提交卡请求
                break;
            case R.id.btn_inactive:
                break;
            case R.id.btn_close:
                finish();
            default:
                break;
        }
    }

    private void initData(){
        switch (ACTIVE){
            case 0:
                break;
            case 1:
                break;
            default:
                break;
        }

    }

    protected void sendSMSMessage() {
        Log.i("Send SMS", "");
        String phoneNo = txtphoneNo;
        String message = "hello";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
