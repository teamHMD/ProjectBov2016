package com.example.bov;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class rooms extends Activity {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    BluetoothDevice device;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    int stat;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    TextView TextView3;
    TextView TextView4;
    TextView TextView5;
    Handler handler = new Handler();
    private TextToSpeech myTTS;
    EditText Height;
    private final int GOOGLE_STT = 1000, MY_UI = 1001;
    Intent i;
    private ArrayList<String> mResult1;                                    //음성인식 결과 저장할 list
    private String mSelectedString1;
    String data;
    int j=0;
    StringBuffer sb1,sb2;
    String mag1,mag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rooms);
        TextView3 = (TextView) findViewById(R.id.textView3);
        TextView4 = (TextView) findViewById(R.id.textView4);
        TextView5 = (TextView) findViewById(R.id.textView5);

        Height = (EditText) findViewById(R.id.editText);
        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                String speech = "보브 제어기능을 시작합니다.";
                myTTS.setLanguage(Locale.KOREA);
                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
       startActivityForResult(enableBluetooth, 1);

        handler.postDelayed(new Runnable() {
            //@Override
            public void run()
            {
                try {       findBT();
                    stat = 1;
                }
                catch (IOException e) {
                    stat = 0;
                    e.printStackTrace();
                }
            }
        }, 5000);
        final Button input = (Button) findViewById(R.id.height);
        input.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (stat == 1) {
                    try {
                        final String height = ((EditText)findViewById(R.id.editText)).getText().toString();
                        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int i) {
                                String speech = "사용자의 신장은 "+height+" 센치미터 입니다";
                                myTTS.setLanguage(Locale.KOREA);
                                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        });
                        mmOutputStream.write(height.getBytes());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(rooms.this,
                                "Connection not established with your home",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();}
            }
        });

        final Button voice = (Button) findViewById(R.id.stt);
        voice.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        String speech = "원하는 제어기능을 말씀해 주세요.";
                        myTTS.setLanguage(Locale.KOREA);
                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);            //intent 생성
                        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());    //음성인식을 호출한 패키지
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");                            //음성인식 언어 설정
                        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Control");
                        startActivityForResult(i, GOOGLE_STT);
                    }
                }, 3000);
            }
        });

        final Button light1 = (Button) findViewById(R.id.Button1);
        light1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (stat == 1) {
                    try {
                        mmOutputStream.write('1');
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int i) {
                                String speech = "장애물 인식을 시작합니다";
                                myTTS.setLanguage(Locale.KOREA);
                                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        });
                            }
                        }, 3000);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(rooms.this,
                                "Connection not established with your home",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();}
            }
        });


        final Button light2 = (Button) findViewById(R.id.Button2);
        light2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (stat == 1) {
                    try {
                        mmOutputStream.write('2');
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = "장애물 인식을 중지합니다.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                            }
                        },3000);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(rooms.this,
                                "Connection not established with your home",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();}
            }
        });

        final Button light3 = (Button) findViewById(R.id.Button3);
        light3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (stat == 1) {
                    try {
                        mmOutputStream.write('3');
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = "점자블록 인식을 시작합니다.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                            }
                        },3000);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(rooms.this,
                                "Connection not established with your home",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();}
            }
        });

        final Button light4 = (Button) findViewById(R.id.Button4);
        light4.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (stat == 1) {
                    try {
                        mmOutputStream.write('4');
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = "점자블록 인식을 중지합니다.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                            }
                        },3000);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(rooms.this,
                                "Connection not established with your home",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();}
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
            mResult1 = data.getStringArrayListExtra(key);        //인식된 데이터 list 받아옴.
            String[] result = new String[mResult1.size()];            //배열생성. 다이얼로그에서 출력하기 위해
            mResult1.toArray(result);                                    //	list 배열로 변환
            mSelectedString1 = result[0];
            Height.setText(mSelectedString1);
        final String num = ((EditText)findViewById(R.id.editText)).getText().toString();
        if(num.equals("장애물 인식 시작")){
            if (stat == 1) {
                try {
                    mmOutputStream.write('1');
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    String speech = "장애물 인식을 시작합니다";
                                    myTTS.setLanguage(Locale.KOREA);
                                    myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            });
                        }
                    }, 3000);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(rooms.this,
                        "Connection not established with your home",
                        Toast.LENGTH_LONG).show();}
        }
        if(num.equals("장애물 인식 중지")){
            if (stat == 1) {
                try {
                    mmOutputStream.write('2');
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    String speech = "장애물 인식을 중지합니다";
                                    myTTS.setLanguage(Locale.KOREA);
                                    myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            });
                        }
                    }, 3000);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(rooms.this,
                        "Connection not established with your home",
                        Toast.LENGTH_LONG).show();}
        }
        if(num.equals("점자블록 인식 시작")){
            if (stat == 1) {
                try {
                    mmOutputStream.write('3');
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    String speech = "점자블록 인식을 시작합니다";
                                    myTTS.setLanguage(Locale.KOREA);
                                    myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            });
                        }
                    }, 3000);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(rooms.this,
                        "Connection not established with your home",
                        Toast.LENGTH_LONG).show();}
        }
        if(num.equals("점자블록 인식 중지")){
            if (stat == 1) {
                try {
                    mmOutputStream.write('4');
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    String speech = "점자블록 인식을 중지합니다";
                                    myTTS.setLanguage(Locale.KOREA);
                                    myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            });
                        }
                    }, 3000);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(rooms.this,
                        "Connection not established with your home",
                        Toast.LENGTH_LONG).show();}
        }
    }


    void findBT() throws IOException {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mmDevice = mBluetoothAdapter.getRemoteDevice("98:D3:31:70:9A:93");
        //mmDevice = mBluetoothAdapter.getRemoteDevice("20:16:05:06:22:46");

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID

        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                String speech = "보브제어기능이 활성화됐습니다";
                myTTS.setLanguage(Locale.KOREA);
                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        beginListenForData();
    }

    void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = mmInputStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    sb1 = new StringBuffer();
                                    sb2 = new StringBuffer();
                                    if(data!=null){
                                        int len = data.length();
                                        for(int k =0 ; k<len ; k++){
                                            char c =data.charAt(k);
                                            if(c == '='){
                                                j++;
                                            }
                                            if(j%2 == 1){
                                                if((c >='0' && c<='9')){
                                                    sb1.append(c);
                                                }
                                            }
                                            if(j%2 == 0){
                                                if((c >='0' && c<='9')){
                                                    sb2.append(c);
                                                }
                                            }
                                        }
                                        mag1 = sb1.toString();
                                        mag2 = sb2.toString();
                                    }

                                    handler.post(new Runnable() {
                                        public void run() {
                                            TextView3.setText(data);
                                            TextView4.setText(sb1);
                                            TextView5.setText(sb2);
                                        }
                                    });

                                    int mag=Integer.parseInt(mag1);
                                    if( mag>=0 && mag<=60){
                                        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int i) {
                                                String speech = "우측 "+mag2+" 센치미터 앞에 점자블록이 있습니다.";
                                                myTTS.setLanguage(Locale.KOREA);
                                                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        });
                                    }
                                    if(mag >60 && mag<=120){
                                        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int i) {
                                                String speech = "전방 "+mag2+" 센치미터 앞에 점자블록이 있습니다.";
                                                myTTS.setLanguage(Locale.KOREA);
                                                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        });
                                    }
                                    if(mag >120 && mag<=180){
                                        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int i) {
                                                String speech = "좌측 "+mag2+" 센치미터 앞에 점자블록이 있습니다.";
                                                myTTS.setLanguage(Locale.KOREA);
                                                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        });
                                    }
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });
        workerThread.start();
    }


    /*@Override
    protected void onDestroy() {
        if (mmInputStream != null) {
            try {
                mmInputStream.close();
            } catch (Exception e) {
            }
            mmInputStream = null;
        }

        if (mmOutputStream != null) {
            try {
                mmOutputStream.close();
            } catch (Exception e) {
            }
            mmOutputStream = null;
        }

        if (mmSocket != null) {
            try {
                mmSocket.close();
            } catch (Exception e) {
            }
            mmSocket = null;
        }
        super.onDestroy();
        System.exit(0);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater h = getMenuInflater();
        h.inflate(R.menu.hardmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.abou:
                startActivity(new Intent("com.test.smarthome.ABOUT"));
                return true;
        }
        return false;
    }

}