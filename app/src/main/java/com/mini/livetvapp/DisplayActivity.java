package com.mini.livetvapp;

import android.content.Intent;
import android.media.tv.TvContract;
import android.media.tv.TvView;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity {

    private static final String TAG = "TAG for DisplayActivity";
    private static final boolean DEBUG = true;
    private String inputId;
    private Uri channelUri;
    private TvView mTvView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tvview);

        mTvView = findViewById(R.id.tvview);
        Intent intent = getIntent();
        inputId = intent.getStringExtra("selectedInputId");
//        inputId = "com.example.android.sampletvinput/.rich.RichTvInputService";
//        String channelId = intent.getStringExtra("channelId");
//        Long id = Long.valueOf(channelId);
        Long id = Long.valueOf(161);
        channelUri = TvContract.buildChannelUri(id);

        if (DEBUG) {

            Log.d(TAG, "create tvView");
            Log.d(TAG, "inputId " + inputId);
//            Log.d(TAG, "channelId " + channelId);
            Log.d(TAG, "channel Uri " + channelUri);
        }

        mTvView.tune(inputId, channelUri);

    }
}
