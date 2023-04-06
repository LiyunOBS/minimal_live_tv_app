package com.mini.livetvapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG for MainActivity";
    private static final boolean DEBUG = true;
    private Context mContext;
    private TvInputManager mTvInputManager;
//    private List<TvInputInfo> mInputs = new ArrayList<>();
    private List<String> mInputIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mTvInputManager = (TvInputManager) mContext.getSystemService(Context.TV_INPUT_SERVICE);

        initInputList();
        handleListAdapter();

    }

    private void initInputList() {

//        mInputs.clear();
        for (TvInputInfo input : mTvInputManager.getTvInputList()) {

            if (!input.isPassthroughInput()) {

                String inputId = input.getId();
//                mInputs.add(input);
                mInputIds.add(inputId);

                if (DEBUG) {
                    Log.d(TAG, "Available input " + inputId);
                }
            }
        }
    }

    private void handleListAdapter() {

        ListAdapter listAdapter = new ListAdapter(mInputIds, mContext, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                Toast.makeText(mContext, "item clicked", Toast.LENGTH_LONG).show();

                Intent channelActivityIntent = new Intent(MainActivity.this, ChannelActivity.class);
                channelActivityIntent.putExtra("inputId", item);
                startActivity(channelActivityIntent);
                if(DEBUG) {
                    Log.d(TAG, "selected TvInputId" + item);
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

}