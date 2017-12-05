package com.w8;

import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.NologinActivity;
import com.w8.base.RetNumUtil;
import com.w8.base.WxUtil;
import com.w8.base.data.Computer;
import com.w8.base.data.ComputerDao;
import com.w8.base.data.DaoMaster;
import com.w8.base.entity.UserrelationSimple;
import com.w8.base.pcurl.AccountUtil;
import com.w8.base.pcurl.LoginUtil;
import com.w8.base.pcurl.MineUtil;

import java.util.List;

public abstract class LoginTestAcc extends NologinActivity {

    public abstract void accPss(); // 尝试登陆， 账号密码不匹配

    public abstract void accErr(); //账号，不存在

    public abstract void startWeb(); // 开始访问网络。

    public abstract void endWeb(); // 访问结束，不管是报错还是成功。

    protected void webErr() { //服务器、网络错误。
        endWeb();
        alertDialogText(getString(R.string.fail));
    }

    protected void timOut() { //超时。
        endWeb();
        alertDialogText(getString(R.string.reg_web_err));
    }

    /**
     * 验证用户名，密码的合法性
     */
    protected void testAndLogin(String acc, String pass) {
        if (!AccountUtil.testAcc(acc)) {
            alertDialogText(getString(R.string.acc_error));
        } else {
            if (!testPass(pass)) {
                alertDialogText(getString(R.string.password_error));
            } else {
                loginStart(acc, pass);
            }
        }
    }

    //调用前，自己判断是否正在请求中。账号密码是否符合要求。
    //如果成功，跳转到active，不需要返回。
    private void loginStart(String acc, String pass) {
        //缺少加密信息。
        List<Computer> list = MyApp.mC.getDS().getComputerDao().queryBuilder().where(ComputerDao.Properties.Acc.eq(acc)).list();
        if (list.size() > 1) {
            MyApp.mC.getDS().getComputerDao().deleteInTx(list);
            loginGetSer(acc, pass);
        } else if (list.size() == 0) {
            loginGetSer(acc, pass);
        } else {
            loginComplete(acc, pass, list.get(0).getIpp());
        }
    }

    //获取，账号属于哪个服务器。
    private void loginGetSer(final String acc, final String pass) {

        JsonObject into = new JsonObject();
        into.addProperty(WxUtil.para_url, LoginUtil.url_app_testAcc);
        into.addProperty(AccountUtil.para_acc, acc);

        StringRequest srlog = new StringRequest(getString(R.string.httpHomeAddress) + into.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String msg) {
                        try {
                            JsonObject jo = new JsonParser().parse(msg).getAsJsonObject();
                            Integer r = jo.get(WxUtil.para_r).getAsInt();
                            if (RetNumUtil.n_0 == r) {
                                String loginIp = jo.get(LoginUtil.para_login_ip).getAsString();
                                ComputerDao computerDao = MyApp.mC.getDS().getComputerDao();
                                List<Computer> lc = computerDao.queryBuilder().where(ComputerDao.Properties.Acc.eq(acc)).list();
                                if (lc.size() > 0) {
                                    computerDao.deleteInTx(lc);
                                    alertDialogText(getString(R.string.fail));
                                    //日志
                                }
                                Computer cc = new Computer();
                                cc.setIpp(loginIp);
                                cc.setAcc(acc);
                                computerDao.insert(cc);

                                loginComplete(acc, pass, loginIp);

                            } else if (RetNumUtil.n_23 == r) {
                                endWeb();
                                accErr();
                            } else {
                                webErr();
                            }
                        } catch (Exception e) {
                            webErr();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                timOut();
            }
        });
        srlog.setRetryPolicy(new DefaultRetryPolicy(RetNumUtil.n_26 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        startWeb();
        MyApp.mC.getVQ().add(srlog);
    }

    //真正的登录所属服务器。
    private void loginComplete(final String acc, final String pass, final String ip) {

        JsonObject into = new JsonObject();
        into.addProperty(WxUtil.para_url, LoginUtil.url_app_LoginCompelete);
        into.addProperty(AccountUtil.para_acc, acc);
        into.addProperty(MineUtil.para_pas, pass);

        //http://192.168.0.101:9980/?-t=
        StringRequest srlog = new StringRequest(getString(R.string.http) + ip + getString(R.string.ht_suffix) + into.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String msg) {
                        try {
                            JsonObject jo = new JsonParser().parse(msg).getAsJsonObject();
                            Integer r = jo.get(WxUtil.para_r).getAsInt();
                            if (RetNumUtil.n_0 == r) {
                                List<UserrelationSimple> userList = new Gson().fromJson(jo.get(LoginUtil.para_fri_list).getAsString(),
                                        new TypeToken<List<UserrelationSimple>>() {
                                        }.getType());
                                String aesUUID = jo.get(LoginUtil.para_login_aes).getAsString();
                                String token = jo.get(LoginUtil.para_login_tid).getAsString();
                                String uid = jo.get(MineUtil.para_uid).getAsString();
                                String nickname = jo.get(MineUtil.para_nickname).getAsString();
                                Integer sound = jo.get(MineUtil.para_sound).getAsInt();
                                Long ctim = jo.get(LoginUtil.para_login_ctim).getAsLong();//应该配合ranid加密使用。

                                String randomid = md5Str(aesUUID + token);

                                String rawKeyB64 = AppUtil.getRawKey(aesUUID + Long.toString(ctim));

                                if (AppUtil.getUid().equals(uid)) {
                                    //和上次登录的是同一人，并且初始化friend列表正确
                                    AppUtil.clearAll();
                                } else {
                                    //不是同一人，清空数据
                                    AppUtil.clearAll();
                                    DaoMaster.dropAllTables(MyApp.mC.getDM().getDatabase(), true);
                                    DaoMaster.createAllTables(MyApp.mC.getDM().getDatabase(), true);
                                }
                                AppUtil.setUrl(ip);
                                AppUtil.setLogin(uid, randomid, token, rawKeyB64, sound, acc, nickname, ctim);
                                //插入好友列表数据。
                                AppUtil.insertFrind(userList);

                                //启动闹钟，不应该只是在这里。在MyApp中也有，用于用户强行结束App后再次打开
                                AppUtil.setTag(ActiveActivity.class.getSimpleName());

                                startActivity(new Intent(LoginTestAcc.this, ActiveActivity.class));
                                finish();
                            } else if (RetNumUtil.n_12 == r) {
                                endWeb();
                                accPss();
                            } else {//服务器返回3，安全加密有问题
                                //初始化未读取数据和app的Wxshare数据
                                webErr();
                            }
                        } catch (Exception e) {
                            webErr();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                timOut();
                List<Computer> dels = MyApp.mC.getDS().getComputerDao().queryBuilder()
                        .where(ComputerDao.Properties.Acc.eq(acc)).list();
                MyApp.mC.getDS().getComputerDao().deleteInTx(dels);

            }
        });
        srlog.setRetryPolicy(new DefaultRetryPolicy(RetNumUtil.n_26 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        startWeb();
        MyApp.mC.getVQ().add(srlog);
    }
}
