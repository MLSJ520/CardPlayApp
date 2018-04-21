package com.example.lenovo.myapplication;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/4/16.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private Context context;
    private List<Card> cardList = new ArrayList<>();
    public GameView(Context context){
        super(context);
        this.context = context;
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
 
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }


}
