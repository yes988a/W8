package com.w8.services;

import android.content.Intent;
import android.os.IBinder;

import com.w8.base.WXBaseService;

/**
 * 获取群聊具体信息，收到服务器群通知后获取群服务器详情
 */
public class ChatgroupService extends WXBaseService {
    public static String TAG = ChatgroupService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
        RequestQueue volleyQeue = app.getVolleyQeue();
        final List<Chatgroupnotice> listNotice = app.getDaoSession().getChatgroupnoticeDao().queryBuilder().where(ChatgroupnoticeDao.Properties.Stat.eq(WxUtil.c_ab)).list();
        for (int i = listNotice.size() - 1; i >= 0; i--) {
            JSONObject jo = new JSONObject();
            jo.put(WxUtil.gid, listNotice.get(i).getGid());
            jo.put(WxUtil.tim, listNotice.get(i).getTim());
            final int indx = i;
            WXStringRequest wj = new WXStringRequest(this, AppUtil.getBurl(this) + WxUtil.u_getchatgroup, jo, new Response.Listener<String>() {
                @Override
                public void onResponse(String str) {
                    JSONObject jo = null;
                    try {
                        jo = JSON.parseObject(str);
                    } catch (Exception e) {
                        mwErr(TAG, "u_getchatgroup.onResponse", e);
                    }
                    if (jo != null) {
                        Integer r = jo.getInteger(WxUtil.r);
                        if (r != null && RetNumUtilA.n_1 == r) {
                            String groupliststring = jo.getString("gl");
                            List<Chatgroup> list = null;
                            try {
                                list = JSON.parseArray(groupliststring, Chatgroup.class);
                            } catch (Exception e) {
                                mwErr(TAG, "u_getchatgroup.gl", e);
                            }
                            if (list != null && list.size() > 0) {
                                ChatgroupDao cd = app.getDaoSession().getChatgroupDao();
                                List<Chatgroup> appgc = new ArrayList<Chatgroup>();//最终需要插入的qunlaio
                                for (int ii = 0; ii < list.size(); ii++) {
                                    Chatgroup pcen = list.get(ii);
                                    long num = cd.queryBuilder().where(ChatgroupDao.Properties.Gid.eq(pcen.getGid()), ChatgroupDao.Properties.Tim.eq(pcen.getTim())).count();
                                    if (num == 0 || num > 1) { //正确，插入到群聊
                                        if (num > 1) {
                                            List<Chatgroup> llllll = cd.queryBuilder().where(ChatgroupDao.Properties.Gid.eq(pcen.getGid()), ChatgroupDao.Properties.Tim.eq(pcen.getTim())).list();
                                            cd.deleteInTx(llllll);
                                        }
                                        Chatgroup cgaa = new Chatgroup();
                                        cgaa.setStat(WxUtil.c_abc);
                                        cgaa.setGid(pcen.getGid());
                                        cgaa.setRequid(pcen.getRequid());
                                        cgaa.setTxt(pcen.getTxt());
                                        cgaa.setTyp(pcen.getTyp());
                                        cgaa.setTim(pcen.getTim());
                                        appgc.add(cgaa);
                                    }
                                }
                                cd.insertInTx(appgc);
//-----------------------------active操作--------
                                Long cti = null;
                                try {
                                    cti = jo.getLong("tm");//时间
                                } catch (Exception e) {
                                    mwErr(TAG, "u_getchatgroup.tm", e);
                                }
                                if (cti == null) {
                                    return;
                                }
                                Calendar dateCur = new GregorianCalendar();
                                dateCur.setTimeInMillis(cti);
                                int yearCurr = dateCur.get(Calendar.YEAR);
                                int yeardayCurr = dateCur.get(Calendar.DAY_OF_YEAR);
                                updateActiveForChatgroup(appgc, yearCurr, yeardayCurr);
                            } else {
                                listNotice.remove(indx);
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listNotice.remove(indx);
                }
            });
            volleyQeue.add(wj);
        }
        app.getDaoSession().getChatgroupnoticeDao().deleteInTx(listNotice);*/
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

}
