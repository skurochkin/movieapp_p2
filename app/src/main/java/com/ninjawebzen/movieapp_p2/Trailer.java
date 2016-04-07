package com.ninjawebzen.movieapp_p2;

import org.json.JSONException;
import org.json.JSONObject;

public class Trailer {

    private static final String KEY_KEY = "key";
    private static final String KEY_NAME = "name";

    private String key;
    private String name;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public static Trailer fromJson(JSONObject jsonObject) throws JSONException {
        Trailer trailer = new Trailer();
        trailer.key = jsonObject.getString(KEY_KEY);
        trailer.name = jsonObject.getString(KEY_NAME);
        return trailer;
    }
}
