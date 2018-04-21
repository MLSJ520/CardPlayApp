package com.example.lenovo.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by lenovo on 2018/4/16.
 */

public class Card {  //一个卡片有花色，值，描述，三种区分
    private int type; // 0 背景，1,2,3，4花色
    private int value; // 其本身的值
    private String desc; //描述需要结合随机出的数值以及卡的功能一起使用
    private Context context;
    private static final int types[] = {R.drawable.hong,R.drawable.hei,R.drawable.fang,R.drawable.mei};
    private static final String values[] = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    private Bitmap cardType;
    private Bitmap cardValue;

    public Card(Context context,int type, int value){
        this.type = types[type];
        this.value = value;
        desc = values[value];
    }
    public Card(Context context,int type){
        this.type = R.drawable.card_bg;  //背景
    }
    public void drawSelf(){
        cardType = BitmapFactory.decodeResource(context.getResources(),type);
        Canvas canvas = new Canvas(cardType);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText(values[value],14,38,paint);
        canvas.save();
        canvas.translate(200,279);
        canvas.rotate((float) Math.PI);
        canvas.drawText(values[value],0,0,paint);
        canvas.restore();
        canvas.drawText(desc,100,140,paint);
    }
}
/*
在一张bitmap上绘制bitmap
Bitmap b=BitmapFactory.decodeResource(CON.getResources(),R.drawable.deltio);
Bitmap bmOverlay = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());
canvas = new Canvas(bmOverlay);
Paint paint = new Paint();
paint.setColor(Color.RED);
canvas.drawBitmap(b, new Matrix(), null);
canvas.drawCircle(750, 14, 11, paint);

 */
