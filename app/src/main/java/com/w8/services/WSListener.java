package com.w8.services;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w8.R;
import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.event.Connect_succ;

import org.greenrobot.eventbus.EventBus;

import java.io.EOFException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * 接受服务器发送过来的信息，并广播给相应的activity。
 */
public class WSListener extends WebSocketListener {
    public static String TAG = WSListener.class.getSimpleName();
    public static boolean needChat = false;//标记是否成功：在长连接创建成功后获取一次getChat。

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        MyApp.webSocket = webSocket;
        MyApp.canNewWs = true;

        //服务采用，握手前安全验证，所以，握手成功，就代表可以通讯啦。

        EventBus.getDefault().post(new Connect_succ());

        //第一次连接成功，应该检查是否有chat信息。发送一次http请求。2次http将ws的连接前后包裹。

        //尝试连接的地方通知连接中。
        //这里通知，已经连接成功。

        //关于失败:
        //设置等，失败后，记录到app中，不通知用户，下次连接时发送到服务器即可。
        //好友请求，和，聊天发送消息。先使用websocket发送，如果，返回false，再使用http发送。http有超时时间，如果失败及通知失败即可。

        AppUtil.stopAlarmNight();
        AppUtil.stopAlarmDay();

        needChat = true;
        MyApp.mC.getChat(true);
        // 总是不一定成功，导致因为有长连接啦就不能在更新遗漏信息。
        // 所有非用户触发不能及时返回给用户的，都应该分级并存储失败结果定时检查。
        // （暂时解决方法）

        Log.e(TAG, "长连接建立成功");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.e(TAG, text);
        // 接受推送信息。当前仅仅接受chat，和心跳验证.
        String[] ss = text.split("\n");
        for (int i = 0; i < ss.length; i++) {
            String msg = ss[i];
            if (MyApp.mC.getString(R.string.send_heart).equals(msg)) {
                webSocket.send(MyApp.mC.getString(R.string.res_heart));
            } else if (MyApp.mC.getString(R.string.res_heart).equals(msg)) {
                //客户端不应该收到回复心跳操作。
                webSocket.close(5500, MyApp.mC.getString(R.string.res_heart));
            } else {
                MyApp.mC.succGetChat(ss[i]);
                //业务处理，理论上，应该是判断chat内容，然后获取具体的信息。
            }
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        clo(webSocket);
    }

    //所有失败信息，发送失败，连接失败。onFailure会关闭onClosed所以不需要在这里面调用onClosed再次关闭。
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        if (t instanceof UnknownHostException) {
            //地址没有找到。不包含被拒绝。
            clo(webSocket);
        } else if (t instanceof EOFException) {
            //流，没有正常关闭。
            //服务器关闭啦服务？
            clo(webSocket);
        } else if (t instanceof SocketTimeoutException) {
            clo(webSocket);
        } else {
            //连接被拒绝，返回IO错误，也跑到这里来啦。
            //不管是什么错误，都关闭。
            clo(webSocket);
        }
    }

    private void clo(WebSocket webSocket) {
        MyApp.canNewWs = true;
        if (webSocket == MyApp.webSocket) {
            MyApp.webSocket = null;

        }

        //启动，其他http轮询。（危险举动，容易造成连接请求死循环不？）应该在用户切换activity等操作或者启用定时任务，不可以在这里调用
//        AppUtil.startAlarmGuard();
        Log.e(TAG, "长连接，，，关闭。");
    }
}
