package com.mini.livetvapp;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.Bundle;

import com.mini.livetvapp.utils.ChannelInfo;
import com.mini.livetvapp.ChannelListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.tvprovider.media.tv.Program;

import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChannelActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
//    private ActivityChannel2Binding binding;
    private static final String TAG = "TAG for ChannelActivity";
    private static final boolean DEBUG = true;
    private static final long DEFAULT_IMMEDIATE_EPG_DURATION_MILLIS = 1000 * 60 * 60; // 1 Hour
    private Context mContext;
    private ContentResolver cr;
    private String inputId;
    private Map<Long, ChannelInfo> mChannels = new HashMap<>();
    private List<ChannelInfo> channelInfoList;
    private Map<Long, List<Program>> mPrograms;
    private List<List<Program>> programList;
    private ChannelListAdapter channelListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_channel);
        mContext = this;
        cr = mContext.getContentResolver();

        Intent intent = getIntent();
        inputId = intent.getStringExtra("inputId");

        if(DEBUG) {
            Log.d(TAG, "selected TvInputId" + inputId);
        }

        initChannelList();
        if (mChannels.size() > 0) {
            searchProgramListForChannels();
        }
//        if (mPrograms.size() > 0) {
//            handleListAdapter();
//        }
        handleListAdapter();

    }

    private void initChannelList() {

        Uri channelsUri = TvContract.buildChannelsUriForInput(inputId);

        String[] projections = new String[ChannelInfo.PROJECTION.length + 1];
        projections[0] = TvContract.Channels._ID;

        System.arraycopy(ChannelInfo.PROJECTION, 0, projections, 1, ChannelInfo.PROJECTION.length);


        try (Cursor cursor =
                     cr.query(channelsUri, projections,null, null, null)
        ) {
            if (cursor != null) {
                if(DEBUG) {
                    Log.d(TAG, "Channel total counts " + String.valueOf(cursor.getCount() ));
                }
                while (cursor.moveToNext()) {
                    mChannels.put(cursor.getLong(0), ChannelInfo.fromCursor(cursor));
                    if(DEBUG) {
                        Log.d(TAG, "Channel  " + String.valueOf(mChannels.size()) + " is found");
                    }
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void searchProgramListForChannels() {
        mPrograms = new HashMap<Long, List<Program>>();
        channelInfoList = new ArrayList<>();
        programList = new ArrayList<>();
        long durationMs = DEFAULT_IMMEDIATE_EPG_DURATION_MILLIS;
        long startMs = System.currentTimeMillis();
        long endMs = startMs + durationMs;
        if(DEBUG) {
            Log.d(TAG, "StartMs  " + String.valueOf(startMs));
            Log.d(TAG, "EndMs " + String.valueOf(endMs));
        }

        for (Map.Entry<Long, ChannelInfo> channel : mChannels.entrySet()) {

            List<Program> programs = new ArrayList<>();
            Uri channelUri = TvContract.buildChannelUri(channel.getKey());

            Uri uri = TvContract.buildProgramsUriForChannel(channelUri);
            if(DEBUG) {
                Log.d(TAG, "Channel Uri " + channelUri.toString());
                Log.d(TAG, "Program Uri " + uri.toString());
            }
            Cursor cursor = null;
            try {
                cursor = cr.query(uri, Program.PROJECTION, null, null, null);
                if(DEBUG) {
                    Log.d(TAG, "Program total counts " + String.valueOf(cursor.getCount() ));
                }
                while (cursor != null && cursor.moveToNext()) {
                    Program program = Program.fromCursor(cursor);
                    if(DEBUG) {
                        Log.d(TAG, "Program StartTimeUtcMillis " + String.valueOf(program.getStartTimeUtcMillis() ));
                        Log.d(TAG, "Program EndTimeUtcMillis " + String.valueOf(program.getEndTimeUtcMillis()));
                    }
                    if (program.getStartTimeUtcMillis() >= startMs && program.getEndTimeUtcMillis() <= endMs ) {
                        if(DEBUG) {
                            Log.d(TAG, "add to program list");
                        }
                        programs.add(program);
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "Unable to get programs for " + channelUri, e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            if(DEBUG) {
                Log.d(TAG, "Program " + String.valueOf(programs.size()) + " is found from Channel " + channel.getKey());
                Log.d(TAG, programs.toString());
            }

            mPrograms.put(channel.getKey(), programs);
            channelInfoList.add(channel.getValue());
            programList.add(programs);
        }

    }

    private void handleListAdapter() {

        RecyclerView recyclerView = findViewById(R.id.channelListRecyclerView);
        TextView emptyListView = findViewById(R.id.emptyChannelListTextView);

        if (mChannels.size() == 0 || mPrograms.size() == 0) {

            recyclerView.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);

        } else {

            recyclerView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);

            channelListAdapter = createListAdapter();
            if(DEBUG) {
                Log.d(TAG, "after new create");
            }
//            RecyclerView recyclerView = findViewById(R.id.channelListRecyclerView);
            if(DEBUG) {
                Log.d(TAG, "after findViewById " + recyclerView.toString());
            }
            recyclerView.setHasFixedSize(true);
            if(DEBUG) {
                Log.d(TAG, "after setHasFixedSize");
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            if(DEBUG) {
                Log.d(TAG, "setLayoutManager");
            }
            recyclerView.setAdapter(channelListAdapter);
            if(DEBUG) {
                Log.d(TAG, "after set adapter");
            }
        }
    }

//    private ChannelListAdapter createEmptyListAdapter() {
//
//        return new ChannelListAdapter(mContext);
//    }

    private ChannelListAdapter createListAdapter() {

        return new ChannelListAdapter(channelInfoList, programList, mContext, new ChannelListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TextView view) {
                Toast.makeText(mContext, "channel clicked", Toast.LENGTH_LONG).show();

                Intent channelActivityIntent = new Intent(ChannelActivity.this, ChannelActivity.class);
                channelActivityIntent.putExtra("selectedChannel", view.getText());
                startActivity(channelActivityIntent);
                if (DEBUG) {

                    Log.d(TAG, "selected channel" + view.getText());
                }
            }
        });
    }
}