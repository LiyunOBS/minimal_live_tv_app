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
import com.mini.livetvapp.utils.PermissionUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.tvprovider.media.tv.Program;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChannelActivity extends AppCompatActivity {

    private static final String TAG = "TAG for ChannelActivity";
    private static final boolean DEBUG = true;
    private static final long DEFAULT_IMMEDIATE_EPG_DURATION_MILLIS = 1000 * 60 * 60; // 1 Hour
    private Context mContext;
    private ContentResolver cr;
    private String inputId;
    private Map<Long, ChannelInfo> mChannels = new HashMap<>();
    private List<ChannelInfo> channelInfoList;
    private List<Long> channelIdList;
//    private Map<Long, List<Program>> mPrograms;
    private List<List<Program>> programList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_channel);

        cr = mContext.getContentResolver();

        Intent intent = getIntent();
        inputId = intent.getStringExtra("inputId");

        if(DEBUG) {
            Log.d(TAG, "Searching channels from selected TvInputId " + inputId);
        }

        if (PermissionUtils.hasReadTvListings(mContext)
                || PermissionUtils.hasAccessAllEpg(mContext)) {
            initChannelList();
            searchProgramListForChannels();
        }
        handleListAdapter();

    }

    private void initChannelList() {

        if(DEBUG) {
            Log.d("TAG for ACCESS_ALL_EPG_DATA", String.valueOf(PermissionUtils.hasAccessAllEpg(mContext)));
        }

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
                    Long channelId = cursor.getLong(0);
                    ChannelInfo channelInfo = ChannelInfo.fromCursor(cursor);
                    mChannels.put(channelId, channelInfo);
                    if(DEBUG) {
                        Log.d(TAG, "Channel  " + String.valueOf(mChannels.size()) + " is found");
                        Log.d(TAG, "Channel is searchable? " + String.valueOf(channelInfo.getSearchable()));
                        Log.d(TAG, "Channel is browsable? " + String.valueOf(channelInfo.getBrowsable()));
                    }
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void searchProgramListForChannels() {
//        mPrograms = new HashMap<>();
        channelInfoList = new ArrayList<>();
        channelIdList = new ArrayList<>();
        programList = new ArrayList<>();
        long durationMs = DEFAULT_IMMEDIATE_EPG_DURATION_MILLIS;
        long startMs = System.currentTimeMillis();
        long endMs = startMs + durationMs;

        for (Map.Entry<Long, ChannelInfo> channel : mChannels.entrySet()) {

            List<Program> programs = new ArrayList<>();
            Long channelId = channel.getKey();
            Uri channelUri = TvContract.buildChannelUri(channelId);
            Uri uri = TvContract.buildProgramsUriForChannel(channelUri);

            Cursor cursor = null;
            try {
                cursor = cr.query(uri, Program.PROJECTION, null, null, null);
                if(DEBUG) {
                    Log.d(TAG, "Program total counts " + String.valueOf(cursor.getCount() ));
                }
                while (cursor != null && cursor.moveToNext()) {
                    Program program = Program.fromCursor(cursor);
                    if (program.getStartTimeUtcMillis() >= startMs && program.getEndTimeUtcMillis() <= endMs ) {
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
                Log.d(TAG, String.valueOf(programs.size()) + " programs found from Channel " + channel.getKey());
                Log.d(TAG, programs.toString());
            }

//            mPrograms.put(channel.getKey(), programs);
            channelInfoList.add(channel.getValue());
            channelIdList.add(channel.getKey());
            programList.add(programs);
        }

    }

    private void handleListAdapter() {

        RecyclerView recyclerView = findViewById(R.id.channelListRecyclerView);
        TextView emptyListView = findViewById(R.id.emptyChannelListTextView);

        if (mChannels.size() == 0) {

            recyclerView.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);

        } else {

            recyclerView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);

            ChannelsAdapter channelsAdapter = createListAdapter();

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(channelsAdapter);
        }
    }

    private ChannelsAdapter createListAdapter() {

        return new ChannelsAdapter(mContext, inputId, channelInfoList, channelIdList, programList);
    }

}