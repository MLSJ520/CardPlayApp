package com.example.lenovo.myapplication;
import android.app.Application;

import com.example.lenovo.myapplication.bean.UserBean;

import okhttp3.WebSocket;

/**
 * Created by lenovo on 2018/3/13.
 */

public class Data extends Application {
    private static String BMOB_ID = "44ac50d55e71179cc956fb5f66489dd7";
    private static String ip = "192.168.1.104:8080";
    private static final String SERVER_URL = "http://"+ip+"/CardPlayServer";
    public static String GET_USER_BY_PHONE = SERVER_URL+"/userController/getUserByPhone.action?phone=";
    public static String GET_USER_BY_PWD = SERVER_URL+"/userController/getUserByPwd.action?phone=";
    public static String GET_USER_BY_PWD1 = "&pwd=";
    public static String REGIST = SERVER_URL+"/userController/regist.action?userBean=";
    public static String MODIFY_NAME = SERVER_URL+"/userController/modifyName.action?userID=";
    public static String MODIFY_NAME2 = "&name=";
    public static String MODIFY_PHONE = SERVER_URL+"/userController/modifyPhone.action?userID=";
    public static String MODIFY_PHONE2 = "&name=";
    // 用户经验改变时，请求，若不足以升级lv传0
    public static String UPDATE_EXP= SERVER_URL+"/userController/updateUserExp.action?userID=";
    public static String UPDATE_EXP2 = "&exp=";
    public static String UPDATE_EXP3 = ".";


    public static WebSocket webSocket;
    public static String SOCKET_SERVER = "ws://"+ip+"/CardPlayServer/Server/websocket.ws";

    public static int[] EXP = new int[101];

    public static boolean SHOW_RECORD = true;

    public static boolean isShowRecord() {
        return SHOW_RECORD;
    }

    public static void setShowRecord(boolean showRecord) {
        SHOW_RECORD = showRecord;
    }

    private static UserBean USER_INFO;  //z在主界面之前加载赋值完毕
    public static String getBmobId() {
        return BMOB_ID;
    }
    public static void setUserInfo(UserBean userBean) {
        USER_INFO = userBean;
    }
    public static UserBean getUserInfo() {return USER_INFO;}
    public static int getExp(int lv){
        if (lv<100)
            return EXP[lv+1];
        else
            return 9999999;
    }
    public static void setExp(int[] Exp){
        for(int i = 0;i<Exp.length;i++){
            EXP[i] = Exp[i];
        }
    }
}
