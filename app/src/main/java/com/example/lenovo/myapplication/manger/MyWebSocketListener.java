package com.example.lenovo.myapplication.manger;

import android.util.Log;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by lenovo on 2018/4/21.
 */

public class MyWebSocketListener extends WebSocketListener {
    public MyWebSocketListener() {
        super();
    }

    //onOpen当WebSocket和远程建立连接时回调
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        Log.d("webSocket",response.message());
        webSocket.send("hello");
        Log.d("webSocket","建立websocket连接成功");   //发送自己的信息,userBean
    }

    //两个onMessage就是接收到消息时回调，只是消息内容的类型不同
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        Log.d("webSocket",text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        // 1.获取对方信息
        // 2.获取对方牌面
        // 3.获取自己的所有牌  共有3种消息

        // 1.请求连接时发送的自己的信息  共有2种信息
        // 2.出牌时发送的信息
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }
    //onClosed就是当连接已经释放的时候被回调
    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
    }

    /*
    onFailure当然是失败时被回调（包括连接失败，发送失败等）。
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
    }
}
