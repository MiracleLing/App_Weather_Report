package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


public class Search extends AppCompatActivity {
    private Button mbtn_serarch;
    private EditText met_search;
    private RecyclerView mRecyclerView;
    private TextView mtv_deleteAll;
    private Button mtv_tv_record;
    private SeachRecordAdapter mAdapter;

    private DbDao mDbDao;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        initViews();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initViews() {
        mDbDao = new DbDao(this);
        mbtn_serarch = (Button) findViewById(R.id.btn_serarch);
        met_search = (EditText) findViewById(R.id.et_search);
        mtv_deleteAll = (TextView) findViewById(R.id.tv_deleteAll);
        mtv_tv_record=(Button) findViewById(R.id.tv_record);
        mtv_deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbDao.deleteData();
                mAdapter.updata(mDbDao.queryData(""));
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SeachRecordAdapter(mDbDao.queryData(""), this);
        mAdapter.setRvItemOnclickListener(new BaseRecycleAdapter.RvItemOnclickListener() {
            @Override
            public void RvItemOnclick(int position) {
                // mDbDao.delete(mDbDao.queryData("").get(position));
                // mAdapter.updata(mDbDao.queryData(""));
                Intent intent=new Intent(Search.this,MainActivity.class);
                String data_Record=mDbDao.queryData("").get(position);
                intent.putExtra("data_record",data_Record);
                startActivity(intent);
                //sendRequestWithHttpURLConnection("101010100");
            }
        });
        mAdapter.setRvItemOnclickListener_del(new BaseRecycleAdapter.RvItemOnclickListener_del() {
            @Override
            public void RvItemOnclick_del(int position) {
                 mDbDao.delete(mDbDao.queryData("").get(position));
                 mAdapter.updata(mDbDao.queryData(""));
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        //事件监听
        mbtn_serarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (met_search.getText().toString().trim().length() != 0) {
                    boolean hasData = mDbDao.hasData(met_search.getText().toString().trim());
                    if (!hasData) {
                        mDbDao.insertData(met_search.getText().toString().trim());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("data_return", met_search.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                    finish();
                    mAdapter.updata(mDbDao.queryData(""));
                } else {
                    Toast.makeText(Search.this, "请输入内容", Toast.LENGTH_SHORT).show();
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
                .setName("Search Page") // TODO: Define a title for the content shown.
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