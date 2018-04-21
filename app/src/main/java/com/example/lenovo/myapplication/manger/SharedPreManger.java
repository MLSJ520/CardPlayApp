package com.example.lenovo.myapplication.manger;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lenovo on 2017/6/27.
 */

public class SharedPreManger {
    SharedPreferences sf;
    SharedPreferences.Editor editor;
    /*
    SharedPreferences sf = getSharedPreferences("User", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sf.edit();//获取编辑器
                            editor.putInt("UserId",userBean.getUserId());
                            editor.commit();//提交修改
     */
    public static String USER_PHONE = "phoneN";
    public static String IS_LOGIN = "isLogin";
    public static String USER_PWD = "pwd";

    public SharedPreManger(String can,Context context){
        sf = context.getSharedPreferences(can, Context.MODE_PRIVATE);
        editor = sf.edit();
    }
    public void setUserPhone(String userPhone){
        editor.putString(USER_PHONE,userPhone);
        editor.commit();
    }
    public void setUserPwd(String pwd){
        editor.putString(USER_PWD,pwd);
        editor.commit();
    }
    public String getUserPhone() {return sf.getString(USER_PHONE,null);}
    public String getUserPwd() {return sf.getString(USER_PWD,null);}

    public void setIsLogin(boolean isLogin){
        editor.putBoolean(IS_LOGIN,isLogin);
        editor.commit();
    }
    public boolean getIsLogin(){
        return sf.getBoolean(IS_LOGIN,false);
    }


}
