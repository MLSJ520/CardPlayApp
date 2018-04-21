package com.example.lenovo.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by lenovo on 2018/3/29.
 */

public class MyDialog extends Dialog  {
    private Context context;
    private String title;
    public MyDialog(Context context,String title) {
        super(context);
        this.context = context;
        this.title = title;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        ImageView btnClose = (ImageView) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }
}
