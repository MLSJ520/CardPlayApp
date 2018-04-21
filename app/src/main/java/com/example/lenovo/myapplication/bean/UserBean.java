package com.example.lenovo.myapplication.bean;

public class UserBean {
    private int userId;
    private String userPhone;
    private String userName;
    private String userPwd;
    private String userPic;
    private int userExp;
    private int userLv;
    private boolean userState;
    private String userWeixin;
    private int fightNum;
    private int winNum;

    public int getWinNum() {
        return winNum;
    }

    public void setWinNum(int winNum) {
        this.winNum = winNum;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPic() {
        return userPic;
    }
    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
    public int getUserExp() {
        return userExp;
    }
    public void setUserExp(int userExp) {
        this.userExp = userExp;
    }
    public int getUserLv() {
        return userLv;
    }
    public void setUserLv(int userLv) {
        this.userLv = userLv;
    }
    public String getUserWeixin() {
        return userWeixin;
    }
    public void setUserWeixin(String userWeixin) {
        this.userWeixin = userWeixin;
    }
    public int getFightNum() {
        return fightNum;
    }
    public void setFightNum(int fightNum) {
        this.fightNum = fightNum;
    }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public String getUserPwd() {
        return userPwd;
    }
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
    public boolean isUserState() {
        return userState;
    }
    public void setUserState(boolean userState) {
        this.userState = userState;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", userPhone='" + userPhone + '\'' +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userPic='" + userPic + '\'' +
                ", userExp=" + userExp +
                ", userLv=" + userLv +
                ", userState=" + userState +
                ", userWeixin='" + userWeixin + '\'' +
                ", fightNum=" + fightNum +
                ", winNum=" + winNum +
                '}';
    }
}
