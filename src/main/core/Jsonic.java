package main.core;

import core.JSONObject;

public interface Jsonic<T> {
    public JSONObject toJson();
    public T fromJson(JSONObject json);
}
