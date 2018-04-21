package com.example.lenovo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lenovo.myapplication.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends BaseActivity {

    public int cards[];     //共有108张
    private List<Card> myCardList;   //底牌，烦躁，因为有随机，还要删除，list明显更胜一筹，，哎
    private List<Card> handCardList = new ArrayList<>();  //手牌，每次获取手牌后都要进行重新排序，重绘，保留这个cardlist，减少内部重绘
    private int leftCardNum = 54;             //剩余牌数
    private static final int MAX_HAND_NUM = 10;  //暂定为10
    private int myHp;   //hp
    private int myDe;   //防御
    private int otherHp;   //hp
    private int otherDe;   //防御
    // 对我方的影响
    private List<Card> myTermCard;// 我出的牌
    private List<Card> otherTermCard;// 敌方出的牌,从请求数据中获得，本次请求还应该包括整个出牌的时间，避免延迟

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    // 在此获取到打乱两副牌，获取到己方底牌    1-54，重复两遍
    // 玩家开始时有两次机会选择底牌，第一次不选，第二次就直接发给玩家，底牌变为手牌后记得删除
    // 抽牌时，出现弹框，弹出10-手牌张底牌，玩家选中后给 ，有技能火时，每次抽牌消耗一点技能火

}
