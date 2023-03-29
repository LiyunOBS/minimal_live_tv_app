package com.mini.livetvapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.os.Bundle;

import com.mini.livetvapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import static android.media.tv.TvInputInfo.TYPE_COMPONENT;
import com.mini.livetvapp.ListAdapter;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.media.tv.TvView;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.MutableInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.tvprovider.media.tv.Program;
import androidx.tvprovider.media.tv.TvContractCompat;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG for MainActivity";
    private static final boolean DEBUG = true;
    private Context mContext;
    private ActivityMainBinding binding;
    private TvInputManager mTvInputManager;
    private List<TvInputInfo> mInputs = new ArrayList<>();
    private List<String> mInputIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        mContext = this;
        mTvInputManager = (TvInputManager) mContext.getSystemService(Context.TV_INPUT_SERVICE);

        if (DEBUG) {
            Log.d(TAG, "main");
        }

        initInputList();
        handleListAdapter();

    }

    private void initInputList() {

        mInputs.clear();
        for (TvInputInfo input : mTvInputManager.getTvInputList()) {

            String inputId = input.getId();
            mInputs.add(input);
            mInputIds.add(inputId);
            if (DEBUG) {
                Log.d(TAG, inputId);
                int state = mTvInputManager.getInputState(inputId);
                Log.d(TAG, "state " + String.valueOf(state));
                Log.d(TAG, "isPassThroughInput " + Boolean.toString(input.isPassthroughInput()));
                Log.d(TAG, "getServiceInfo " + input.getServiceInfo().toString());
                Log.d(TAG, "type " + String.valueOf(input.getType()));
                Log.d(TAG, "tunerCount " + String.valueOf(input.getTunerCount()));
                Log.d(TAG, "type_component " + String.valueOf(TYPE_COMPONENT));
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
        if(DEBUG) {
            Log.d(TAG, "after findViewById " + recyclerView.toString());
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

}