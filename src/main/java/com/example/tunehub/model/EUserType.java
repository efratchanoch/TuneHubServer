package com.example.tunehub.model;

public enum EUserType {
    STUDENT , MANAGER , TEACHER, MUSIC_LOVER, MUSICIAN;

    public static EUserType fromValue(int value) {
        return EUserType.values()[value];
    }
}
