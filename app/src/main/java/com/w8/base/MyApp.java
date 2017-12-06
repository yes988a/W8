package com.w8.base;

import android.app.Application;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.w8.ActiveActivity;
import com.w8.R;
import com.w8.base.data.Active;
import com.w8.base.data.ActiveDao;
import com.w8.base.data.ChatTims;
import com.w8.base.data.DaoMaster;
import com.w8.base.data.DaoSession;
import com.w8.base.data.Friend;
import com.w8.base.data.FriendDao;
import com.w8.base.data.Frireq;
import com.w8.base.entity.ChatEntity;
import com.w8.base.event.Refresh_active;
import com.w8.base.pcurl.ChatUtil;
import com.w8.base.pcurl.FriendUtil;
import com.w8.base.pcurl.LoginUtil;
import com.w8.base.pcurl.MineUtil;
import com.w8.services.WSListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class MyApp extends Application {

    public static String TAG = MyApp.class.getSimpleName();

    /**
     * 数据库名称
     */
    public static String DBName = "w.db";
    public static MyApp mC;

    //_get结尾，必须通过它的get方法获取。（因为需要初始化。）
    private static DaoMaster daoMaster_get;      //整个数据库管理。
    private static DaoSession daoSession_get;    // dao具体管理session
    private static RequestQueue volleyQeue_get;  // http连接管理

    private static OkHttpClient mOkHttpClient_get;  // 客户端。用于创建连接和关闭连接。
    public static WebSocket webSocket;         //正在使用的长连接。长连接。如果为null标识没有连接存在

    public static boolean canNewWs = true;//是否允许创建ws。（以下情况不允许：正在创建。已经退出。）。。。防止false不能变成true《一、登陆时转true，二、更新安全严重时校验并适当处理。》

    @Override
    public void onCreate() {
        // 程序创建的时候执行，测地后台关闭应用程序后，再打开时执行。
        super.onCreate();
        if (mC == null) {
            mC = this;
        }

        //AppUtil.startAlarmGuard();放在这里不行，应该放到每次创建activity时检测吗？
    }

    public DaoMaster getDM() {
        if (daoMaster_get == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(this, DBName, null);
            daoMaster_get = new DaoMaster(helper.getEncryptedWritableDb("passworddddd"));
        }
        return daoMaster_get;
    }

    public DaoSession getDS() {
        if (daoSession_get == null) {
            daoSession_get = getDM().newSession();
        }
        return daoSession_get;
    }

    public RequestQueue getVQ() {
        if (volleyQeue_get == null) {
            volleyQeue_get = Volley.newRequestQueue(this);
        }
        return volleyQeue_get;
    }


    private OkHttpClient getOkHttpClient() {
        //长连接：只要不运行mOkHttpClient.dispatcher().executorService().shutdown();
        //客户端就一直存在，关闭长连接使用WebSocket的关闭吧。
        if (mOkHttpClient_get == null) {
            mOkHttpClient_get = new OkHttpClient.Builder()
                    .readTimeout(16000, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(16000, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(50000, TimeUnit.SECONDS)//设置连接超时时间
                    .build();
        }
        return mOkHttpClient_get;
    }

    /**
     * 新建长连接。  （调用前可以不判断MyApp.webSocket == null）
     * 如：ws://192.168.0.101:9980/?x=包含安全验证信息。
     * <p>
     * 调用此方法时，不需要关心是否重复新建。
     * <p>
     * 新建长连接成功后，运行一次短链接请求最新信息。。。每次短链接运行成功返回数据后，检查app是否有未处理的信息《包括是否需要发送给pc等》
     *
     * @return
     */
    public void newWS() {  //从新建立一个OkHttpClient，并关闭原来的OkHttpClient，如果存在。
        //  调用newWS前，请判断webSocket是否为null。将判断放到外面，可以把发送和创建连接分开，灵活使用ws和http
        if (MyApp.webSocket == null && canNewWs) { // 允许
            try {
//                    webSocket.close(5500, "重复创建。");
                canNewWs = false;//创建中，不允许重复请求。
                JsonObject into = new JsonObject();
                into.addProperty(MineUtil.para_uid, AppUtil.getUid());
                into.addProperty(LoginUtil.para_login_tid, AppUtil.getToken());
                Request request = new Request.Builder()
                        .url("ws://192.168.0.101:9980/?x=" + into.toString())//websocket连接地址::: ws://127.0.0.1:9981
                        .build();
                getOkHttpClient().newWebSocket(request, new WSListener());  // 这里也可以得到一个websocket，和OkHttpClient有什么区别呢？
            } catch (Exception e) {
                canNewWs = true;
            }
        }
    }

    //  --- ----------    获取消息     其他逻辑关系不可以再在MyApp中存放。-------
    // ------------  获取chat消息，可以使用http，也有ws推送，
    //           App发送请求删除chat消息，http，     -------------------------------------------------------


    // 关于获取消息时频率限制。可能忽略某次getChat。所以特殊的紧急获取，还是要有，比如，某个正在聊天的连接失败啦，还是应该马上再次请求，而不是因为时间限制忽略
    // 因为已经有_webing参数限制啦，所以，时间频率可以适当写小点，
    /**
     * 获取最新，消息通知。 《 Service 和 WSListener会调用此方法。    此方法不应该调用WSListener注意死循环》
     */
    public void getChat() {
        if (!ChatUtil.url_app_findChatsingle_webing &&
                ChatUtil.url_app_findChatsingle_tim_interval + ChatUtil.url_app_findChatsingle_tim_old < System.currentTimeMillis()) {
            // 每个请求，都应该有个时间限制，不允许频繁动作
            String uid = AppUtil.getUid();
            String tid = AppUtil.getToken();
            String aes = AppUtil.aesEncrypt(AppUtil.getSafeAes(), uid);

            JsonObject into = new JsonObject();

            into.addProperty(MineUtil.para_uid, uid);//测试使用--------------------

            into.addProperty(WxUtil.para_url, ChatUtil.url_app_findChatsingle);
            into.addProperty(LoginUtil.para_login_tid, tid);
            into.addProperty(LoginUtil.para_login_aes_safedes, aes);

            StringRequest wj = new StringRequest(getString(R.string.httpHomeAddress) + into.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String msg) {
                    ChatUtil.url_app_findChatsingle_webing = false;
                    ChatUtil.url_app_findChatsingle_tim_old = System.currentTimeMillis();
                    succGetChat(msg);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ChatUtil.url_app_findChatsingle_webing = false;
                    ChatUtil.url_app_findChatsingle_tim_old = System.currentTimeMillis();
                }
            });
            wj.setRetryPolicy(new DefaultRetryPolicy(ChatUtil.url_app_findChatsingle_tim_out, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            getVQ().add(wj);
            ChatUtil.url_app_findChatsingle_webing = true;
        } else {
            Log.e(TAG, "频繁发送chat信息请求。");
        }
    }

    //成功获取具体chat内容后处理。仅仅处理获取的数据，不管其他通讯状态（）
    public void succGetChat(String msg) {
        try {
            JsonObject jo = new JsonParser().parse(msg).getAsJsonObject();
            Integer r = jo.get(WxUtil.para_r).getAsInt();
            if (RetNumUtil.n_0 == r) {
                String listStr = jo.get(WxUtil.para_json).getAsString();
                List<ChatEntity> chatList = new Gson()
                        .fromJson(listStr,
                                new TypeToken<List<ChatEntity>>() {
                                }.getType());

                //所有已经的到的信息都加入到被删除行列。关于需要保留的如好友请求还需要安全验证。pc端其他解决办法吧。
                List<Long> tims = new ArrayList<Long>();

                for (int i = 0; i < chatList.size(); i++) {
                    ChatEntity pc = chatList.get(i);

                    tims.add(pc.getTim());

                    int typ = pc.getTyp();
                    if (FriendUtil.typ_add_fri == typ) {
                        Log.e(TAG, "-------被好友---请求-------------------------------");

                        //判断是否我的好友。
                        List<Friend> numFri = getDS().getFriendDao().queryBuilder()
                                .where(FriendDao.Properties.Fid.eq(pc.getReqid())).list();

                        if (numFri.size() != 1) {
                            //添加好友请求。
                            getDS().getFriendDao().deleteInTx(numFri);

                            //reqaccount ，reqnickname 、reqdes、met
                            JsonObject jpc = new JsonParser().parse(pc.getDes()).getAsJsonObject();

                            //{"m0t":"n","Q5L":"","R6c":"yes988a1","Fn6i":"yes988a1111昵称"}
                            String reqaccount = jpc.get(FriendUtil.para_reqacc).getAsString();
                            String reqnickname = jpc.get(FriendUtil.para_reqnickname).getAsString();
                            String reqdes = jpc.get(FriendUtil.para_reqdes).getAsString();
                            String met = jpc.get(FriendUtil.para_met).getAsString();

                            Frireq frireq = new Frireq();
                            frireq.setMet(met);
                            frireq.setRequid(pc.getReqid());
                            frireq.setReqaccount(reqaccount);
                            frireq.setReqnickname(reqnickname);
                            frireq.setReqdes(reqdes);

                            frireq.setResuid(pc.getUid());
                            frireq.setTim(pc.getTim());
                            frireq.setTyp(0);

                            getDS().getFrireqDao().insertOrReplace(frireq);

                            //插入active  ------------     ------------     ------------   ------------     ------------
                            List<Active> lActive = getDS().getActiveDao().queryBuilder()
                                    .where(ActiveDao.Properties.Uuid.eq(pc.getReqid())).list();
                            if (lActive.size() != 1) {
                                getDS().getActiveDao().deleteInTx(lActive);
                                Active active = new Active();
                                active.setUuid(pc.getReqid());
                                active.setTitle("好友添加请求");
                                active.setNum(0);
                                active.setType(AppUtil.n_typ_frireq);
                                active.setDes(reqnickname);
                                active.setTim(pc.getTim());
                                active.setTimstr(TimUtil.formatTimeToStr(pc.getTim()));
                                getDS().getActiveDao().save(active);
                            }
                            //广播到active
                            if (ActiveActivity.class.getSimpleName().equals(AppUtil.getTag())) {
                                EventBus.getDefault().post(new Refresh_active());
                            }
                        }
                    } else if (FriendUtil.typ_del_fri == typ) {
                        Log.e(TAG, "-------被好友删除-------------------------------");
                        //被好友删除
                        String fid = pc.getReqid();
                        List<Friend> fs = getDS().getFriendDao().queryBuilder().where(FriendDao.Properties.Fid.eq(fid)).list();
                        getDS().getFriendDao().deleteInTx(fs);
                        //缺少，应该定义一个，我已经被好友删除的记录？  从新设计如何展现是否删除。
                    } else{   // ----  ------ ----  要精确到 ==typ 从新定义 chat中typ，现在聊天存0123这种放弃。
                        //聊天。判断TAG，如果是在聊天的人，更新列表，如果没在聊天更新返回键的新消息数量。
                    }
                }
                delChat(tims);
            } else {
                //para_r不是0.
            }
        } catch (Exception e) {
            Log.e(TAG, "--------具体chat内容报错。。" + e.getMessage());
        }
    }

    //过期chat信息，删除。（app通知pc已经获取到某些信息。级别，弱）
    public void delChat(final List<Long> intoList) {

        final List<Long> tims = new ArrayList<Long>();
        if (intoList != null) {
            tims.addAll(intoList);
        }

        final List<ChatTims> oldTims = getDS().getChatTimsDao().queryBuilder().list();
        for (int i = 0; i < oldTims.size(); i++) {
            tims.add(oldTims.get(i).getOldTim());
        }
        if (tims.size() == 0 || AppUtil.getUid().equals("")) {
            Log.e(TAG,"-------报错。。。。its:"+tims.size()+"Uid:"+AppUtil.getUid().equals(""));
        } else {
            JsonObject into = new JsonObject();
            into.addProperty(WxUtil.para_url, ChatUtil.url_app_delChatsingleByTims);
            into.addProperty(MineUtil.para_uid, AppUtil.getUid());
            into.addProperty(WxUtil.para_tim, new Gson().toJson(tims));

            StringRequest wj = new StringRequest(getString(R.string.httpHomeAddress) + into.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String msg) {
                    try {
                        JsonObject jo = new JsonParser().parse(msg).getAsJsonObject();
                        //标记，已经发送给pc删除。
                        getDS().getChatTimsDao().deleteInTx(oldTims);
                        Log.e(TAG, "-------删除，过期，chat，成功。。。-------------------------------");
                    } catch (Exception e) {
                        Log.e(TAG,"------报错--------delChat(final失败啦。。。。。。。。。。。");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "-------失败，失败，删除，过期，chat，。。。-------------------------------");
                    //循环tims，再插入，有重复，暂时忽略放过吧。
                    List<ChatTims> llll = new ArrayList<ChatTims>();
                    for (int i = 0; i < intoList.size(); i++) {
                        ChatTims chatTims = new ChatTims();
                        chatTims.setOldTim(intoList.get(i));
                        llll.add(chatTims);
                    }
                    getDS().getChatTimsDao().insertInTx(llll);
                }
            });
            getVQ().add(wj);
        }
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "--- MyApp  onTerminate   程序终止的时候执行  -----");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "--- MyApp  onLowMemory   低内存的时候执行  -----");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "--- MyApp  onTrimMemory  程序在内存清理的时候执行  -----");
        super.onTrimMemory(level);
    }
}
