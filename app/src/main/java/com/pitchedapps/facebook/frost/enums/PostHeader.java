package com.pitchedapps.facebook.frost.enums;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum PostHeader {

    PROFILE("profile", -1);

    private String typeName;
    private int typeInt;

    PostHeader(String s, int i) {
        typeName = s;
        typeInt = i;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getType() {
        return typeInt;
    }
}