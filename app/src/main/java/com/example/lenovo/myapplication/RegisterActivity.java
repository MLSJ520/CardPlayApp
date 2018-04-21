package com.example.lenovo.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.myapplication.bean.UserBean;
import com.example.lenovo.myapplication.manger.ActivityCollector;
import com.example.lenovo.myapplication.manger.SharedPreManger;
import com.google.gson.Gson;

import java.io.IOException;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private int type = 1;
    private int TIME = 90;
    private Handler mHandler = new Handler();
    private Button btnYzm;
    EditText editPhone ;
    EditText editPwd ;
    EditText editYzm;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        BmobSMS.initialize(this, Data.getBmobId());

        Intent intent = getIntent();
        type = intent.getIntExtra("actionType",1);
        String name = intent.getStringExtra("actionName");

        LinearLayout yzmPart = (LinearLayout) findViewById(R.id.yzm_part);
        if (type == 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) yzmPart.getLayoutParams();
            params.height = 0;
            //yzmPart.setLayoutParams((new LinearLayout.LayoutParams(0, 0)));
            yzmPart.setLayoutParams(params);
        }
        TextView title =(TextView) findViewById(R.id.label_title);
        title.setText(name);
        ImageButton btnBack = (ImageButton)findViewById(R.id.btn_rback);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        editPwd = (EditText) findViewById(R.id.edit_pwd);
        editYzm = (EditText) findViewById(R.id.edit_yzm);

        btnYzm = (Button) findViewById(R.id.btn_yzm);
        Button btnGo = (Button) findViewById(R.id.btn_go);

        btnBack.setOnClickListener(this);
        btnYzm.setOnClickListener(this);
        btnGo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String phoneN,yzm,pwd;
        switch (view.getId()){
            case R.id.btn_rback:
                finish();
                break;
            case R.id.btn_yzm:
                //注册时，先提交后台，查看该手机号是否注册，若注册返回数据提示该手机号已注册，否，再提交验证码请求
                phoneN = editPhone.getText().toString();
                checkPhone(phoneN);
                break;
            case R.id.btn_go:
                initProgressDialog();
                phoneN = editPhone.getText().toString();
                pwd = editPwd.getText().toString();
                if(type == 1){
                    yzm = editYzm.getText().toString();
                    checkYzm(phoneN,yzm,pwd);
                } else {
                    login(phoneN,pwd);
                }

                //页面显示加载状态，提交请求，判断当前操作是否有效，有效则跳转，无效提示。
                //验证码和后台完全正确才算ok

                break;
            default:
                break;
        }
    }

    public void checkPhone(final String phoneN){
        Log.d("web--phoneN",phoneN);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Data.GET_USER_BY_PHONE+phoneN).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("web","getUserByPhone请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("web","getUserByPhone请求成功");
                        String result = response.body().string();
                        Log.d("web",result);
                        if (result.equals("false")){
                            //做请求验证码操作
                            getYzm(phoneN);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"手机号已注册",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        }).start();
        //
    }
    public void getYzm(String phoneN){
        BmobSMS.requestSMSCode(this, phoneN, "模板名称",new RequestSMSCodeListener() {
            @Override
            public void done(Integer smsId,BmobException ex) {
                if(ex==null){//验证码发送成功
                    Log.i("bmob", "短信id："+smsId);//用于查询本次短信发送详情
                    Toast.makeText(getApplicationContext(),"验证码发送成功",Toast.LENGTH_SHORT).show();
                    new Thread(new CutDownTimer()).start();//开始倒计时
                    //刷新按钮冷却时间
                }
            }
        });
    }

    public void checkYzm(final String phoneN, String yzm, final String pwd){
        BmobSMS.verifySmsCode(this,phoneN, yzm, new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {
                if(ex==null){//短信验证码已验证成功
                    Log.i("bmob", "验证通过");
                    //在此进行注册事宜
                    regist(phoneN,pwd);

                }else{
                    Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(),"验证码不正确",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void login(String phoneN,String pwd) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Data.GET_USER_BY_PWD+phoneN+Data.GET_USER_BY_PWD1+pwd).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("web--getUserByPwd","请求失败");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"请求失败，请重试",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                String json = response.body().string();
                Log.d("web--login",json);
                Gson gson = new Gson();
                UserBean userBean = gson.fromJson(json,UserBean.class);
                SharedPreManger sharedPreManger = new SharedPreManger("UserInfo",RegisterActivity.this);
                sharedPreManger.setUserPhone(userBean.getUserPhone());
                sharedPreManger.setUserPwd(userBean.getUserPwd());
                sharedPreManger.setIsLogin(true);
                Log.d("web--Json",userBean.getUserPhone());
                Data.setUserInfo(userBean);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeDialog();
                    }
                });
            }
        });
    }
    public void regist(String phoneN,String pwd){
        final UserBean userBean = new UserBean();
        userBean.setUserPhone(phoneN);
        userBean.setUserPwd(pwd);
        Gson gson = new Gson();
        final String json = gson.toJson(userBean);
        //Log.d("web--注册",j)
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式
                RequestBody requestBody = RequestBody.create(JSON, json);
                Request request = new Request.Builder().url(Data.REGIST).post(requestBody).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("web--regist","请求失败");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this,"请求失败，请重试",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Data.setUserInfo(userBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeDialog();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public void initProgressDialog(){
        progressDialog = new ProgressDialog(RegisterActivity.this);
        //progressDialog.setTitle("正在加载");
        progressDialog.setMessage("wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void closeDialog(){
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        ActivityCollector.finishAll();
        startActivity(intent);
    }


    class CutDownTimer implements Runnable {
        @Override
        public void run() {
            while(TIME>0){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        btnYzm.setEnabled(false);
                        btnYzm.setText(TIME+"秒");
                    }
                });
                try {
                    Thread.sleep(1000); //强制线程休眠1秒，就是设置倒计时的间隔时间为1秒。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TIME--;
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    btnYzm.setEnabled(true);
                    btnYzm.setText("获取验证码");
                }
            });
            TIME = 90;
        }
    }
}
