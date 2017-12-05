package com.w8.base.event;

import com.google.gson.JsonObject;

/**
 * 撤回单聊。
 */
public class Ret_chat {
    private JsonObject jo;

    public Ret_chat(JsonObject jo) {
        this.jo = jo;
    }

    public JsonObject getJo() {
        return jo;
    }

    public void setJo(JsonObject jo) {
        this.jo = jo;
    }
}
