/*
 * Created on: 2016 HMD Project Bov
 * Team : HMD, Electric Engineering & Electron Engineering, Pusan National University
 * Authors: Gu-hwan Bae
 * 			Hye-ri Bang
 * 			Jeong-wook Kim
 * 			Tae-hyeong Koo
 * 			Kyeong-Yeop Jeong
 */


package com.example.bov;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Bov extends AppCompatActivity {
    private TextToSpeech myTTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bov);
        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                String speech ="메인화면입니다. 기능을 선택해주세요";
                myTTS.setLanguage(Locale.KOREA);
                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            }
        });package com.example.bov;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Bov extends AppCompatActivity {
    private TextToSpeech myTTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bov);
        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                String speech ="메인화면입니다. 기능을 선택해주세요";
                myTTS.setLanguage(Locale.KOREA);
                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.Blue:
                Intent intent = new Intent(this, rooms.class);
                startActivity(intent);
                break;

            case R.id.Land:
                Intent intent1 = new Intent(this, Land.class);
                startActivity(intent1);
                break;
        }
    }
}
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.Blue:
                Intent intent = new Intent(this, rooms.class);
                startActivity(intent);
                break;

            case R.id.Land:
                Intent intent1 = new Intent(this, Land.class);
                startActivity(intent1);
                break;
        }
    }
}