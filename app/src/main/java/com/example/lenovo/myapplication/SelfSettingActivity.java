package com.example.lenovo.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.myapplication.adapter.MyListAdapter;
import com.example.lenovo.myapplication.bean.Item;
import com.example.lenovo.myapplication.bean.UserBean;
import com.example.lenovo.myapplication.manger.ActivityCollector;
import com.example.lenovo.myapplication.manger.SharedPreManger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelfSettingActivity extends BaseActivity implements View.OnClickListener {

    private List<Item> myList = new ArrayList<>();
    private static int[] settingList = {R.layout.activity_card, R.layout.activity_card};
    private ImageView close;
    private TextView textName;
    private static UserBean userBean = Data.getUserInfo();
    private String yzmStr,newPhone;
    private EditText editPhone,editYzm;
    private Button btnNYzm;
    private int TIME = 90;
    private RelativeLayout layoutParent;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_setting);
        layoutParent = (RelativeLayout)findViewById(R.id.layout_parent);
        initList();
        MyListAdapter listAdapter = new MyListAdapter(SelfSettingActivity.this,R.layout.list_item,myList);
        final ListView listView = (ListView) findViewById(R.id.list_setting);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("List",String.valueOf(i));
                layoutParent.removeAllViews();
                switch(i){
                    case 0:
                        initUserInfo(layoutParent);
                        break;
                    case 1:
                        initBattleInfo(layoutParent);
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        });
        /*listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mHandler.postAtFrontOfQueue(new Runnable() {
                        public void run() {
                            listView.setSelection(0);
                        }
                    });
                }
            }
        });*/
        listView.performItemClick(listView.getChildAt(0),0,listView.getItemIdAtPosition(0));

        close = (ImageView)findViewById(R.id.btn_close);
        close.setOnClickListener(this);
        ImageView btnEdit = (ImageView)findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);
        textName = (TextView)findViewById(R.id.text_name);
        textName.setText(userBean.getUserName());
        Log.d("SelfSetting",userBean.toString());
        TextView textLv = (TextView)findViewById(R.id.text_Lv);
        TextView textExp = (TextView)findViewById(R.id.text_exp);
        int lv = userBean.getUserLv();
        textLv.setText("Lv."+lv);
        textExp.setText(userBean.getUserLv()+"/"+Data.getExp(lv));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_logout:
                //登录状态修改为异常，跳转到登录界面
                SharedPreManger sharedPreManger = new SharedPreManger("UserInfo",this);
                sharedPreManger.setIsLogin(false);
                Intent intent = new Intent(this,LoginActivity.class);
                ActivityCollector.finishAll();
                startActivity(intent);
                break;
            case R.id.btn_change_phone:
                // TODO 手机号显示为185****3227字样，更换手机号码弹框
                changePhone();
                //原手机号，先手机号，验证码
                break;
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_edit:
                editName();
                break;
            case R.id.btn_yzm:
                // 获取验证码

                break;
            default:
                break;
        }
    }

    private void editName(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LinearLayout view = new LinearLayout(this);
        final EditText editText = new EditText(this);
        editText.setText(userBean.getUserName());
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setMaxLines(10);
        editText.setLayoutParams(vlp);
        view.setPadding(20,0,20,0);
        view.addView(editText);
        dialog.setTitle("修改用户名").setIcon(R.drawable.edit2).setView(view);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String str = editText.getText().toString();
                if (str.length()<1|str.length()>15) {
                    Toast.makeText(SelfSettingActivity.this,"名字长度无效，修改失败",Toast.LENGTH_SHORT).show();
                } else {

                    int id = userBean.getUserId();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(Data.MODIFY_NAME+id+Data.MODIFY_NAME2+str).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SelfSettingActivity.this,"修改用户名请求失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                             }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textName.setText(str);
                                    userBean.setUserName(str);
                                    Data.setUserInfo(userBean);
                                    Toast.makeText(SelfSettingActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
                //TODO 主界面名称修改
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    private void initUserInfo(RelativeLayout layoutParent){
        layoutParent.addView(LayoutInflater.from(SelfSettingActivity.this).inflate(R.layout.self_info,null));
        TextView textId = (TextView)findViewById(R.id.text_user_id);
        TextView textPhone = (TextView)findViewById(R.id.text_user_phone);
        Button changPhone = (Button)findViewById(R.id.btn_change_phone);
        Button btnLogout = (Button)findViewById(R.id.btn_logout);
        changPhone.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        textId.setText("ID:"+userBean.getUserId());
        String phone = userBean.getUserPhone();
       // Log.d("phone",phone.replaceAll("(?<=\\d{3})\\d(?=\\d{4})","*"));
        textPhone.setText("手机:"+phone.replaceAll("(?<=\\d{2})\\d(?=\\d{2})","*"));
    }

    private void initBattleInfo(RelativeLayout layoutParent){
        layoutParent.addView(LayoutInflater.from(SelfSettingActivity.this).inflate(R.layout.battle_info,null));
        TextView battleAll = (TextView)findViewById(R.id.text_battle_all);
        TextView battleWin = (TextView)findViewById(R.id.text_battle_win);
        TextView battleRate = (TextView)findViewById(R.id.text_battle_rate);
        battleAll.setText("对战场次:"+userBean.getFightNum());
        battleWin.setText("胜利场次:"+userBean.getWinNum());
        int rate = 0;
        if (userBean.getFightNum() > 0){
            rate = userBean.getWinNum()*100/userBean.getFightNum();
        }
        battleRate.setText("胜率:"+rate+"%");
        Switch recordCheck = (Switch)findViewById(R.id.record_check);
        recordCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Data.setShowRecord(b);
            }
        });
    }
    private void changePhone(){
        //新建一个dialog
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("换绑手机号码").setCancelable(false);

        View view = LayoutInflater.from(SelfSettingActivity.this).inflate(R.layout.change_phone,null);
        dialog.setView(view);

        editPhone = (EditText)view.findViewById(R.id.edit_nphone);
        editYzm = (EditText)view.findViewById(R.id.edit_nyzm);
        btnNYzm = (Button)view.findViewById(R.id.btn_nyzm);
        btnNYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPhone = editPhone.getText().toString();
                if (newPhone.length()!=11){
                    Toast.makeText(SelfSettingActivity.this,"号码格式错误",Toast.LENGTH_SHORT).show();
                } else {
                    checkPhone(newPhone);
                }
            }
        });
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                yzmStr = editYzm.getText().toString();
                Log.d("YZM",yzmStr);
                checkYzm(newPhone,yzmStr);

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
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
                                    Toast.makeText(getApplicationContext(),"手机号已被绑定",Toast.LENGTH_SHORT).show();
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
    public void checkYzm(final String phoneN, String yzm){
        BmobSMS.verifySmsCode(this,phoneN, yzm, new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {
                if(ex==null){//短信验证码已验证成功
                    Log.i("bmob", "验证通过");
                    final UserBean userBean = Data.getUserInfo();
                    int id = userBean.getUserId();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(Data.MODIFY_PHONE+id+Data.MODIFY_PHONE2+newPhone).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SelfSettingActivity.this,"修改手机号请求失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    userBean.setUserPhone(newPhone);
                                    Data.setUserInfo(userBean);
                                    Toast.makeText(SelfSettingActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                    layoutParent.removeAllViews();
                                    initUserInfo(layoutParent);
                                    SharedPreManger sf = new SharedPreManger("UserInfo",getApplicationContext());
                                    sf.setUserPhone(newPhone);
                                }
                            });
                        }
                    });
                    //TODO 此处更新用户信息，data，以及原界面
                    //TODO 更新数据库

                }else{
                    Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(),"验证码不正确",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    class CutDownTimer implements Runnable {
        @Override
        public void run() {
            while(TIME>0){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        btnNYzm.setEnabled(false);
                        btnNYzm.setText(TIME+"秒");
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
                    btnNYzm.setEnabled(true);
                    btnNYzm.setText("获取验证码");
                }
            });
            TIME = 90;
        }
    }
    private void initList(){
        Item item = new Item(R.drawable.head,"详情");
        myList.add(item);
        item = new Item(R.drawable.battle,"对战");
        myList.add(item);
        item = new Item(R.drawable.setting,"设置");
        myList.add(item);
    }

}
