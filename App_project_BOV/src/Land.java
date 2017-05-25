package com.example.bov;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Land extends Activity {
    LocationManager locManager;
    LocationListener locationListener;
    String mSdPath;
    Geocoder mCoder;
    static double lat1, lon1, lat2, lon2, lat3, lon3;
    int a = 0, b = 0, c = 0, d = 0, e = 0, f = 0, j;
    TextView mText1, mText2, mText3;
    private ArrayList<String> mResult1;                                    //음성인식 결과 저장할 list
    private String mSelectedString1;
    private ArrayList<String> mResult2;                                    //음성인식 결과 저장할 list
    private String mSelectedString2;
    private ArrayList<String> mResult3;                                    //음성인식 결과 저장할 list
    private String mSelectedString3;
    private final int GOOGLE_STT = 1000, MY_UI = 1001;
    Intent i;
    EditText edit1,edit2,edit3;
    ListView listView;
    ListViewAdapter adapter;
    private TextToSpeech myTTS;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land);
        TabHost tab_host = (TabHost) findViewById(R.id.tabhost);
        tab_host.setup();

        TabHost.TabSpec ts1 = tab_host.newTabSpec("tab1");
        ts1.setIndicator("Landmark");
        ts1.setContent(R.id.tab1);
        tab_host.addTab(ts1);

        TabHost.TabSpec ts2 = tab_host.newTabSpec("tab2");
        ts2.setIndicator("Bookmark");
        ts2.setContent(R.id.tab2);
        tab_host.addTab(ts2);
        tab_host.setCurrentTab(0);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListViewAdapter(this);
        edit1 =(EditText)findViewById(R.id.land1);
        edit2 =(EditText)findViewById(R.id.land2);
        edit3 =(EditText)findViewById(R.id.land3);

        findViewById(R.id.book2).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                final String list1 = ((EditText)findViewById(R.id.land1)).getText().toString();
                final String list2 = ((EditText)findViewById(R.id.land2)).getText().toString();
                final String list3 = ((EditText)findViewById(R.id.land3)).getText().toString();
                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        String speech = list1+"       " +list2 +"       "+list3 +"경로를 즐겨찾기에 추가합니다.";
                        myTTS.setLanguage(Locale.KOREA);
                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                adapter.addItem(new TextItem(list1,list2,list3));
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String out1 = ((TextView)view.findViewById(R.id.textView1)).getText().toString();
                final String out2 = ((TextView)view.findViewById(R.id.textView2)).getText().toString();
                final String out3 = ((TextView)view.findViewById(R.id.textView3)).getText().toString();
                edit1.setText(out1);
                edit2.setText(out2);
                edit3.setText(out3);
                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        String speech = out1+"  " +out2 +"  "+out3 +"경로를 선택합니다";
                        myTTS.setLanguage(Locale.KOREA);
                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String out1 = ((TextView)view.findViewById(R.id.textView1)).getText().toString();
                final String out2 = ((TextView)view.findViewById(R.id.textView2)).getText().toString();
                final String out3 = ((TextView)view.findViewById(R.id.textView3)).getText().toString();
                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        String speech = out1+"     " +out2 +"     "+out3 +"경로를 즐겨찾기에서 제거합니다.";
                        myTTS.setLanguage(Locale.KOREA);
                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                adapter.mItem.remove(position);
                listView.clearChoices();
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        mCoder = new Geocoder(this);
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final TextView dis1 = (TextView) findViewById(R.id.distance1);
        final TextView dis2 = (TextView) findViewById(R.id.distance2);
        final TextView dis3 = (TextView) findViewById(R.id.distance3);
        mText1 = (TextView)findViewById(R.id.land1);
        mText2 = (TextView)findViewById(R.id.land2);
        mText3 = (TextView)findViewById(R.id.land3);
        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                String speech = "랜드마크기능을 실행합니다.";
                myTTS.setLanguage(Locale.KOREA);
                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        findViewById(R.id.button).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        String speech = "입력하신 랜드마크를 등록합니다.";
                        myTTS.setLanguage(Locale.KOREA);
                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                final String saddr1 = ((EditText) findViewById(R.id.land1)).getText().toString();
                final String saddr2 = ((EditText) findViewById(R.id.land2)).getText().toString();
                final String saddr3 = ((EditText) findViewById(R.id.land3)).getText().toString();
                List<Address> addr1, addr2, addr3;
                try {
                    addr1 = mCoder.getFromLocationName(saddr1, 5);
                } catch (IOException e) {
                    return;
                }
                try {
                    addr2 = mCoder.getFromLocationName(saddr2, 5);
                } catch (IOException e) {
                    return;
                }
                try {
                    addr3 = mCoder.getFromLocationName(saddr3, 5);
                } catch (IOException e) {
                    return;
                }

                for (int i = 0; i < addr1.size(); ++i) {
                    Address lating1 = addr1.get(i);
                    lat1 = lating1.getLatitude(); //위도가져오기
                    lon1 = lating1.getLongitude(); //경도가져오기
                }

                for (int i = 0; i < addr2.size(); ++i) {
                    Address lating2 = addr2.get(i);
                    lat2 = lating2.getLatitude(); //위도가져오기
                    lon2 = lating2.getLongitude(); //경도가져오기
                }

                for (int i = 0; i < addr3.size(); ++i) {
                    Address lating3 = addr3.get(i);
                    lat3 = lating3.getLatitude(); //위도가져오기
                    lon3 = lating3.getLongitude(); //경도가져오기
                }

                locationListener = new LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {
                        mSdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();
                        //locationText.setText("위도 : " + location.getLatitude()
                        //        + " 경도 : " + location.getLongitude());
                        float dist1[] = new float[1];
                        Location.distanceBetween(lat1, lon1, latitude, longitude, dist1);
                        dis1.setText(saddr1 + "까지 거리는" + dist1[0] + "m입니다.");
                        if (dist1[0] < 30) {
                            a++;
                            if (a == 1) {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = saddr1 + "에 접근중입니다.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                                b = 1;
                            }
                        } else {
                            if (b == 1) {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = saddr1 + "에서 벗어납니다.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                                b++;
                            }
                        }

                        float dist2[] = new float[1];
                        Location.distanceBetween(lat2, lon2, latitude, longitude, dist2);
                        dis2.setText(saddr2 + "까지 거리는" + dist2[0] + "m입니다.");
                        if (dist2[0] < 30) {
                            c++;
                            if (c == 1) {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = saddr2 + "에 접근중입니다.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                                d = 1;
                            }
                        } else {
                            if (d == 1) {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = saddr2 + "에서 벗어납니다.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                                d++;
                            }
                        }
                        float dist3[] = new float[1];
                        Location.distanceBetween(lat3, lon3, latitude, longitude, dist3);
                        dis3.setText(saddr3 + "까지 거리는" + dist3[0] + "m입니다.");
                        if (dist3[0] < 30) {
                            e++;
                            if (e == 1) {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = saddr3 + "에 접근중입니다.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                                f = 1;
                            }
                        } else {
                            if (f == 1) {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = saddr2 + "에서 벗어납니다.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                                f++;
                            }
                        }

                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        // TODO Auto-generated method stub
                    }
                };

                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, locationListener);
            }
        });

        findViewById(R.id.voice1).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        String speech = "첫번째 랜드마크를 말씀해주세요.";
                        myTTS.setLanguage(Locale.KOREA);
                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });

                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        j=1;
                        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);            //intent 생성
                        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());    //음성인식을 호출한 패키지
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");                            //음성인식 언어 설정
                        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "1st Landmark.");
                        startActivityForResult(i, GOOGLE_STT);
                    }
                }, 3000);
            }
        });
        findViewById(R.id.voice2).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        String speech = "두번째 랜드마크를 말씀해주세요.";
                        myTTS.setLanguage(Locale.KOREA);
                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        j=2;
                        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);            //intent 생성
                        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());    //음성인식을 호출한 패키지
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");                            //음성인식 언어 설정
                        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "2nd Landmark.");
                        startActivityForResult(i, GOOGLE_STT);
                    }
                }, 3000);
            }
        });
        findViewById(R.id.voice3).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        String speech = "세번째 랜드마크를 말씀해주세요.";
                        myTTS.setLanguage(Locale.KOREA);
                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        j=3;
                        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);            //intent 생성
                        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());    //음성인식을 호출한 패키지
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");                            //음성인식 언어 설정
                        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "3rd Landmark.");
                        startActivityForResult(i, GOOGLE_STT);
                    }
                }, 3000);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && (requestCode == GOOGLE_STT || requestCode == MY_UI)) {        //결과가 있으면
            showSelectDialog(requestCode, data);                //결과를 다이얼로그로 출력.
        } else {                                                            //결과가 없으면 에러 메시지 출력
            String msg = null;

            //내가 만든 activity에서 넘어오는 오류 코드를 분류
            switch (resultCode) {
                case SpeechRecognizer.ERROR_AUDIO:
                    msg = "오디오 입력 중 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    msg = "단말에서 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    msg = "권한이 없습니다.";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    msg = "네트워크 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    msg = "일치하는 항목이 없습니다.";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    msg = "음성인식 서비스가 과부하 되었습니다.";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    msg = "서버에서 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    msg = "입력이 없습니다.";
                    break;
            }

            if (msg != null)        //오류 메시지가 null이 아니면 메시지 출력
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void showSelectDialog(int requestCode, Intent data) {
        String key = "";
        if (requestCode == GOOGLE_STT)                    //구글음성인식이면
            key = RecognizerIntent.EXTRA_RESULTS;    //키값 설정
        else if (requestCode == MY_UI)                    //내가 만든 activity 이면
            key = SpeechRecognizer.RESULTS_RECOGNITION;
        if(j==1) {
            mResult1 = data.getStringArrayListExtra(key);        //인식된 데이터 list 받아옴.
            String[] result = new String[mResult1.size()];            //배열생성. 다이얼로그에서 출력하기 위해
            mResult1.toArray(result);                                    //	list 배열로 변환
            mSelectedString1 = result[0];
            mText1.setText( mSelectedString1);
        }
        if(j==2) {
            mResult2 = data.getStringArrayListExtra(key);        //인식된 데이터 list 받아옴.
            String[] result = new String[mResult2.size()];            //배열생성. 다이얼로그에서 출력하기 위해
            mResult2.toArray(result);                                    //	list 배열로 변환
            mSelectedString2 = result[0];
            mText2.setText(mSelectedString2);
        }
        if(j==3) {
            mResult3 = data.getStringArrayListExtra(key);        //인식된 데이터 list 받아옴.
            String[] result = new String[mResult3.size()];            //배열생성. 다이얼로그에서 출력하기 위해
            mResult3.toArray(result);                                    //	list 배열로 변환
            mSelectedString3 = result[0];
            mText3.setText( mSelectedString3);
        }
    }

}






