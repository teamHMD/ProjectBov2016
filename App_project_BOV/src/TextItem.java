package com.example.bov;

public class TextItem {
    private String[] mData;
    public TextItem  (String obj01, String obj02 , String obj03) {
        mData = new String[3];
        mData[0] = obj01;
        mData[1] = obj02;
        mData[2] = obj03;
    }

    public String getData(int index) {
        return mData[index];
    }
}
