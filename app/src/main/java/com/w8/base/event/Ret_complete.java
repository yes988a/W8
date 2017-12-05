package com.w8.base.event;

import com.google.gson.JsonObject;

public class Ret_complete {
    private JsonObject jo;

    public Ret_complete(JsonObject jo) {
        this.jo = jo;
    }

    public JsonObject getJo() {
        return jo;
    }

    public void setJo(JsonObject jo) {
        this.jo = jo;
    }
}
