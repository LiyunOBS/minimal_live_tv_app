package com.mini.livetvapp.utils;

import android.database.Cursor;
import android.media.tv.TvContract;

public final class ChannelInfo {

    public static final String[] PROJECTION = {
            TvContract.Channels.COLUMN_DISPLAY_NUMBER,
            TvContract.Channels.COLUMN_DISPLAY_NAME,
            TvContract.Channels.COLUMN_APP_LINK_ICON_URI,
            TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID,
            TvContract.Channels.COLUMN_SEARCHABLE,
            TvContract.Channels.COLUMN_BROWSABLE
    };

    private static final String TAG = "TAG for ChannelInfo";
    private static final boolean DEBUG = true;
    public final String number;
    public final String name;
    public final String logoUrl;
    public final int originalNetworkId;
    public final int searchable;
    public final int browsable;
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
        index = c.getColumnIndex(TvContract.Channels.COLUMN_APP_LINK_ICON_URI);
        if (index >= 0) {
            builder.setLogoUrl(c.getString(index));
        }
        index = c.getColumnIndex(TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID);
        if (index >= 0) {
            builder.setOriginalNetworkId(c.getInt(index));
        }
        index = c.getColumnIndex(TvContract.Channels.COLUMN_SEARCHABLE);
        if (index >= 0) {
            builder.setSearchable(c.getInt(index));
        }
        index = c.getColumnIndex(TvContract.Channels.COLUMN_BROWSABLE);
        if (index >= 0) {
            builder.setBrowsable(c.getInt(index));
        }
        return builder.build();
    }

    private ChannelInfo(
            String number,
            String name,
            String logoUrl,
            int originalNetworkId,
            int searchable,
            int browsable
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
        this.searchable = searchable;
        this.browsable = browsable;
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

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public int getSearchable() {
        return searchable;
    }

    public int getBrowsable() {
        return browsable;
    }

    public static class Builder {
        private String mNumber;
        private String mName;
        private String mLogoUrl;
        private int mOriginalNetworkId;
        private int mSearchable;
        private int mBrowsable;

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

        public Builder setSearchable(int searchable) {
            mSearchable = searchable;
            return this;
        }

        public Builder setBrowsable(int browsable) {
            mBrowsable = browsable;
            return this;
        }

        public ChannelInfo build() {
            return new ChannelInfo(
                    mNumber,
                    mName,
                    mLogoUrl,
                    mOriginalNetworkId,
                    mSearchable,
                    mBrowsable
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

}