package com.mini.livetvapp;

import static android.media.tv.TvInputInfo.TYPE_COMPONENT;

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

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.tvprovider.media.tv.Program;
import androidx.tvprovider.media.tv.TvContractCompat;

import com.mini.livetvapp.databinding.FragmentFirstBinding;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.core.app.ActivityCompat;

public class FirstFragment extends Fragment {

    private static final String TAG = "TAG for Test";

    private Context mContext;

    private FragmentFirstBinding binding;

//    private Context mContext;
    private TvInputManager mTvInputManager;

    private ContentResolver cr;

    private List<TvInputInfo> mInputs = new ArrayList<>();;
//    private final Map<String, Integer> mInputStateMap = new HashMap<>();

//    private ChannelData mData = new ChannelData();

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        mContext = getContext();
        mTvInputManager = (TvInputManager) mContext.getSystemService(Context.TV_INPUT_SERVICE);

        initInputList();



//        Log.d("TAG for permission1", String.valueOf(checkSelfPermission(mContext, "INTERNET")));
//        Log.d("TAG for permission2", String.valueOf(checkSelfPermission(mContext, "READ_EXTERNAL_STORAGE")));
//        Log.d("TAG for permission3", String.valueOf(checkSelfPermission(mContext, "READ_TV_LISTINGS")));
//        Log.d("TAG for permission4", String.valueOf(checkSelfPermission(mContext, "READ_CONTACTS")));
//        Log.d("TAG for permission5", String.valueOf(checkSelfPermission(mContext, "WRITE_EPG_DATA")));
//        Log.d("TAG for ACCESS_ALL_EPG_DATA", String.valueOf(checkSelfPermission(mContext, "com.android.providers.tv.permission.ACCESS_ALL_EPG_DATA") == PermissionChecker.PERMISSION_GRANTED ));
//
//        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
//                "android.permission.READ_TV_LISTINGS",
//                "com.android.providers.tv.permission.ACCESS_ALL_EPG_DATA"} , 0);
//
//        Log.d("TAG for ACCESS_ALL_EPG_DATA", String.valueOf(checkSelfPermission(mContext, "com.android.providers.tv.permission.ACCESS_ALL_EPG_DATA") == PermissionChecker.PERMISSION_GRANTED ));



//        String[] projection =  {
//                TvContract.Channels._ID,
//                TvContract.Channels.COLUMN_DISPLAY_NUMBER
//        };

//        ContentResolver cr = new ContentResolver();
//        Iterator<TvInputInfo> it = mInputs.iterator();

//        while(it.hasNext()) {
            TvInputInfo aux = mInputs.get(0);
            Uri channelsUri = TvContract.buildChannelsUriForInput(aux.getId());

            Map<Long, ChannelInfo> mChannels = new HashMap<>();

            String[] projections = new String[ChannelInfo.PROJECTION.length + 1];
            projections[0] = TvContract.Channels._ID;

            System.arraycopy(ChannelInfo.PROJECTION, 0, projections, 1, ChannelInfo.PROJECTION.length);
//            Log.d("TAG : projections length", String.valueOf(projections.length));
//            Log.d("TAG : _ID", projections[0]);
//            Log.d("TAG : COLUMN_DISPLAY_NUMBER", projections[1].toString());
//            Log.d("TAG : COLUMN_DISPLAY_NAME", projections[2]);
//            Log.d("TAG : COLUMN_ORIGINAL_NETWORK_ID", String.valueOf(projections[3]));

            ContentResolver cr = mContext.getContentResolver();

//        String[] projection = {TvContract.Channels._ID, TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID};

            try (Cursor cursor =
                         cr.query(channelsUri, projections,null, null, null)
            ) {
                if (cursor != null) {
//                    cursor.moveToPosition(0);
                    Log.d("TAG", "cursor is NOT null");
                    Log.d("TAG for columns", String.valueOf(cursor.getColumnCount() ));
                    Log.d("TAG for rows", String.valueOf(cursor.getCount() ));
//                    Log.d("TAG for rows", cursor.getString(1) );
//                    Log.d("TAG", String.valueOf(cursor.getPosition() ));
//                    cursor.moveToPosition(0);
                    Log.d("TAG", String.valueOf(cursor.getPosition() ));
                    Log.d("TAG", cursor.getColumnName(0));
//                    Log.d("TAG", String.valueOf(cursor.isLast()));
//                    Log.d("TAG", String.valueOf(cursor.moveToNext()));
                    while (cursor.moveToNext()) {
                        Log.d("TAG", "cursor moves to next");
                        mChannels.put(cursor.getLong(0), ChannelInfo.fromCursor(cursor));
                        Log.d("TAG", String.valueOf(mChannels.size()));
                    }
                }
            }
//        Log.d("TAG", String.valueOf(map.size()));


        for (Map.Entry<Long, ChannelInfo> channel : mChannels.entrySet()) {
            Uri channelUri = TvContract.buildChannelUri(channel.getKey());
            Log.d("TAG for channelUri", channelUri.toString());

            List<Program> programs = new ArrayList<>();
            Uri uri = TvContract.buildProgramsUriForChannel(channelUri);

            Cursor cursor = null;
            try {
                cursor = cr.query(uri, Program.PROJECTION, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    programs.add(Program.fromCursor(cursor));
                }
            } catch (Exception e) {
                Log.w(TAG, "Unable to get programs for " + channelUri, e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            Log.d(TAG, String.valueOf(programs.size()));
            Log.d(TAG, programs.toString());

        }

//        TvView mTvView = findViewById(R.id.tv_view);;
//        mTvView.tune(inputId, channelUri);



        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

//    public void init() {
//
//        ArrayList<String> elements = new ArrayList<>();
//
//        ListAdapter listAdapter = new ListAdapter(elements, mContext);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listRecyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.setAdapter(listAdapter);
//    }

    public static final class ChannelInfo {

        public static final String[] PROJECTION = {
                TvContract.Channels.COLUMN_DISPLAY_NUMBER,
                TvContract.Channels.COLUMN_DISPLAY_NAME,
                TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID,
        };


        public final String number;
        public final String name;
        public final String logoUrl;
        public final int originalNetworkId;
//        public final int videoWidth;
//        public final int videoHeight;
//        public final float videoPixelAspectRatio;
//        public final int audioChannel;
//        public final int audioLanguageCount;
//        public final boolean hasClosedCaption;
//        public final ProgramInfo program;
//        public final String appLinkText;
//        public final int appLinkColor;
//        public final String appLinkIconUri;
//        public final String appLinkPosterArtUri;
//        public final String appLinkIntentUri;

        public static ChannelInfo fromCursor(Cursor c) {
            // TODO: Fill other fields.
            Builder builder = new Builder();
            int index = c.getColumnIndex(TvContract.Channels.COLUMN_DISPLAY_NUMBER);
            if (index >= 0) {
                builder.setNumber(c.getString(index));
            }
            index = c.getColumnIndex(TvContract.Channels.COLUMN_DISPLAY_NAME);
            if (index >= 0) {
                builder.setName(c.getString(index));
            }
            index = c.getColumnIndex(TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID);
            if (index >= 0) {
                builder.setOriginalNetworkId(c.getInt(index));
            }
            return builder.build();
        }

        private ChannelInfo(
                String number,
                String name,
                String logoUrl,
                int originalNetworkId
//                int videoWidth,
//                int videoHeight,
//                float videoPixelAspectRatio,
//                int audioChannel,
//                int audioLanguageCount,
//                boolean hasClosedCaption,
//                ProgramInfo program,
//                String appLinkText,
//                int appLinkColor,
//                String appLinkIconUri,
//                String appLinkPosterArtUri,
//                String appLinkIntentUri
        ) {
            this.number = number;
            this.name = name;
            this.logoUrl = logoUrl;
            this.originalNetworkId = originalNetworkId;
//            this.videoWidth = videoWidth;
//            this.videoHeight = videoHeight;
//            this.videoPixelAspectRatio = videoPixelAspectRatio;
//            this.audioChannel = audioChannel;
//            this.audioLanguageCount = audioLanguageCount;
//            this.hasClosedCaption = hasClosedCaption;
//            this.program = program;
//            this.appLinkText = appLinkText;
//            this.appLinkColor = appLinkColor;
//            this.appLinkIconUri = appLinkIconUri;
//            this.appLinkPosterArtUri = appLinkPosterArtUri;
//            this.appLinkIntentUri = appLinkIntentUri;
        }

    }


    public static class Builder {
        private String mNumber;
        private String mName;
        private String mLogoUrl = null;
        private int mOriginalNetworkId;

        public Builder() {}

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setNumber(String number) {
            mNumber = number;
            return this;
        }

        public Builder setLogoUrl(String logoUrl) {
            mLogoUrl = logoUrl;
            return this;
        }

        public Builder setOriginalNetworkId(int originalNetworkId) {
            mOriginalNetworkId = originalNetworkId;
            return this;
        }

        public ChannelInfo build() {
            return new ChannelInfo(
                    mNumber,
                    mName,
                    mLogoUrl,
                    mOriginalNetworkId
//                    mVideoWidth,
//                    mVideoHeight,
//                    mVideoPixelAspectRatio,
//                    mAudioChannel,
//                    mAudioLanguageCount,
//                    mHasClosedCaption,
//                    mProgram,
//                    mAppLinkText,
//                    mAppLinkColor,
//                    mAppLinkIconUri,
//                    mAppLinkPosterArtUri,
//                    mAppLinkIntentUri
            );
        }

    }

    private void initInputList() {

        mInputs.clear();

        for (TvInputInfo input : mTvInputManager.getTvInputList()) {

            String inputId = input.getId();
            Log.d("test for inputId", inputId);

            int state = mTvInputManager.getInputState(inputId);
            Log.d("test for state", String.valueOf(state));
            Log.d("test for isPassThroughInput", Boolean.toString(input.isPassthroughInput()));
            Log.d("test for getServiceInfo", input.getServiceInfo().toString());
//            Log.d("test for parentId", input.getParentId());
            Log.d("test for type", String.valueOf(input.getType()));
            Log.d("test for tunerCount", String.valueOf(input.getTunerCount()));
            Log.d("test for type_component", String.valueOf(TYPE_COMPONENT));

            mInputs.add(input);
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ChannelWrapper {
//        final Set<ChannelListener> mChannelListeners = new ArraySet<>();
        final Channel mChannel;
        boolean mBrowsableInDb;
        boolean mLockedInDb;
        boolean mInputRemoved;

        ChannelWrapper(Channel channel) {
            mChannel = channel;
//            mBrowsableInDb = channel.isBrowsable();
//            mLockedInDb = channel.isLocked();
//            mInputRemoved = !mInputManager.hasTvInputInfo(channel.getInputId());
        }

//        void addListener(ChannelListener listener) {
//            mChannelListeners.add(listener);
//        }
//
//        void removeListener(ChannelListener listener) {
//            mChannelListeners.remove(listener);
//        }
//
//        void notifyChannelUpdated() {
//            for (ChannelListener l : mChannelListeners) {
//                l.onChannelUpdated(mChannel);
//            }
//        }
//
//        void notifyChannelRemoved() {
//            for (ChannelListener l : mChannelListeners) {
//                l.onChannelRemoved(mChannel);
//            }
//        }
    }

    private static class ChannelData {
        final Map<Long, ChannelWrapper> channelWrapperMap;
        final Map<String, MutableInt> channelCountMap;
        final List<Channel> channels;

        ChannelData() {
            channelWrapperMap = new HashMap<>();
            channelCountMap = new HashMap<>();
            channels = new ArrayList<>();
        }

        ChannelData(ChannelData data) {
            channelWrapperMap = new HashMap<>(data.channelWrapperMap);
            channelCountMap = new HashMap<>(data.channelCountMap);
            channels = new ArrayList<>(data.channels);
        }

        ChannelData(
                Map<Long, ChannelWrapper> channelWrapperMap,
                Map<String, MutableInt> channelCountMap,
                List<Channel> channels) {
            this.channelWrapperMap = channelWrapperMap;
            this.channelCountMap = channelCountMap;
            this.channels = channels;
        }
    }

    private static class UnmodifiableChannelData extends ChannelData {
        UnmodifiableChannelData() {
            super(
                    Collections.unmodifiableMap(new HashMap<>()),
                    Collections.unmodifiableMap(new HashMap<>()),
                    Collections.unmodifiableList(new ArrayList<>()));
        }

        UnmodifiableChannelData(ChannelData data) {
            super(
                    Collections.unmodifiableMap(data.channelWrapperMap),
                    Collections.unmodifiableMap(data.channelCountMap),
                    Collections.unmodifiableList(data.channels));
        }
    }
}