package com.w8.base.event;

import com.google.gson.JsonObject;

public class Ret_back_groupchat {
    private JsonObject jo;

    public Ret_back_groupchat(JsonObject jo) {
        this.jo = jo;
    }

    public JsonObject getJo() {
        return jo;
    }

    public void setJo(JsonObject jo) {
        this.jo = jo;
    }
}
