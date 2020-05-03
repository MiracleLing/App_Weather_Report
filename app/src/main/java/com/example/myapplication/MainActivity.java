package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    Button sendRequest;
    TextView tv_tem;
    TextView tv_tem2;
    TextView tv_tem3;
    Button tv_city;
    TextView tv_wea;
    TextView tv_update_time;
    ImageView tv_wea_img;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        sendRequestWithHttpURLConnection(" ");
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        sendRequestWithHttpURLConnection(" ");
                }
            });

    tv_city.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, Search.class);
            startActivityForResult(intent, 1);
        }
    });

        Intent intent = getIntent();
        String data_record = intent.getStringExtra("data_record");
        sendRequestWithHttpURLConnection(data_record);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String city2 = data.getStringExtra("data_return");
                    sendRequestWithHttpURLConnection(city2);
                }
                break;
            default:
        }
    }

    private void initialize() {
        sendRequest = (Button) findViewById(R.id.send_request);
        tv_tem = (TextView) findViewById(R.id.tv_tem);
        tv_tem2 = (TextView) findViewById(R.id.tv_tem2);
        tv_tem3 = (TextView) findViewById(R.id.tv_tem3);
        tv_city = (Button)findViewById(R.id.tv_city);
        tv_wea = (TextView) findViewById(R.id.tv_wea);
        tv_update_time = (TextView) findViewById(R.id.tv_update_time);
        tv_wea_img = (ImageView) findViewById(R.id.wea_img);
    }

    public void sendRequestWithHttpURLConnection(final String city) {
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.tianqiapi.com/free/day?appid=23373458&appsecret=c4hueOqm&city=" + city);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    String jsondata = response.toString();//StringBuild可以自增长，与String不太一样
                    parseJSONWithJSONObject(jsondata);//解析数据的方法

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject jsonobject = new JSONObject(jsonData);
                String city = jsonObject.getString("city");
                String update_time = jsonObject.getString("update_time");
                String wea = jsonObject.getString("wea");
                String wea_img = jsonObject.getString("wea_img");
                String tem = jsonObject.getString("tem");
                String tem_day = jsonObject.getString("tem_day");
                String tem_night = jsonObject.getString("tem_night");
                String win = jsonObject.getString("win");
                String win_speed = jsonObject.getString("win_speed");
                String air = jsonObject.getString("air");
                showResponse(city, tem, tem_day, tem_night, wea, wea_img, update_time, win, air);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showResponse(final String city, final String tem, final String tem_day, final String tem_night,
                              final String wea, final String wea_img, final String update_time, final String win,
                              final String air) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //在这里进行UI操作，将结果显示到界面上来

                tv_tem.setText(tem);
                tv_tem2.setText("℃");
                tv_tem3.setText(tem_day + "℃/" + tem_night + "℃");
                tv_city.setText(city);
                tv_wea.setText(wea + "  风向：" + win + "\n空气污染指数：" + air);
                tv_update_time.setText("更新时间：" + update_time);
                if (wea_img.equals("yu")) {
                    tv_wea_img.setImageResource(R.drawable.yu);
                }

                if (wea_img.equals("qing")) {
                    tv_wea_img.setImageResource(R.drawable.qing);
                }

                if (wea_img.equals("wu")) {
                    tv_wea_img.setImageResource(R.drawable.wu);
                }

                if (wea_img.equals("xue")) {
                    tv_wea_img.setImageResource(R.drawable.xue);
                }

                if (wea_img.equals("lei")) {
                    tv_wea_img.setImageResource(R.drawable.lei);
                }

                if (wea_img.equals("shachen")) {
                    tv_wea_img.setImageResource(R.drawable.shachen);
                }

                if (wea_img.equals("yun")) {
                    tv_wea_img.setImageResource(R.drawable.yun);
                }

                if (wea_img.equals("yin")) {
                    tv_wea_img.setImageResource(R.drawable.yin);
                }


            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
