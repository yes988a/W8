package com.w8.base.event;

import com.google.gson.JsonObject;

/**
 * Created by fei on 2017/8/17.
 */

public class Ret_testnum {
    private JsonObject jo;

    public Ret_testnum(JsonObject jo) {
        this.jo = jo;
    }

    public JsonObject getJo() {
        return jo;
    }

    public void setJo(JsonObject jo) {
        this.jo = jo;
    }
}
