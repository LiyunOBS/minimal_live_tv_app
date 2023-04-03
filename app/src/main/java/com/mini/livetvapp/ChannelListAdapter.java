package com.mini.livetvapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.tvprovider.media.tv.Program;

import com.mini.livetvapp.utils.ChannelInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {

    private static final String TAG = "TAG for ChannelListAdapter";
    private static final boolean DEBUG = true;
    protected OnItemClickListener OnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view);
    }

//    private List<String> mData;
    private List<ChannelInfo> channelInfoList;
    private List<Long> channelIdList;
    private List<List<Program>> programList;
    private LayoutInflater mInflater;
    private Context context;
    private final OnItemClickListener listener;

    public ChannelListAdapter(List<ChannelInfo> channels, List<Long> channelIds, List<List<Program>> programs, Context context, OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.channelInfoList = channels;
        this.channelIdList = channelIds;
        this.programList = programs;
        this.listener = listener;
        if(DEBUG) {
            Log.d(TAG, "after channelListAdapter is newly created.");
        }
    }

    public ChannelListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.channelInfoList = null;
        this.programList = null;
        this.listener = null;
        if(DEBUG) {
            Log.d(TAG, "after empty channelListAdapter is newly created.");
        }
    }

    @Override
    public int getItemCount() { return channelInfoList.size(); }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.channel_element, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChannelListAdapter.ViewHolder holder, final int position) {
//        holder.cv.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));
        holder.bindData(channelInfoList.get(position), channelIdList.get(position), programList.get(position), position, listener);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView channelNumber;
        TextView channelName;
        TextView programName;
        TextView channelIndex;
        TextView pos;
        CardView cv;

        ViewHolder(View itemView) {
            super(itemView);
            channelNumber = itemView.findViewById(R.id.channelNumber);
            channelName = itemView.findViewById(R.id.channelName);
            programName = itemView.findViewById(R.id.programList);
//            channelIndex = itemView.findViewById(R.id.channelId);
            pos = itemView.findViewById(R.id.channelId);
            cv = itemView.findViewById(R.id.channelCV);
        }


        void bindData(final ChannelInfo channel, final Long channelId, final List<Program> programs, int position, final OnItemClickListener listener) {

            String selectedChannelNumber = channel.getNumber();
            String selectedChannelName = channel.getName();
//            String selectedChannelId = channel.ge;
//            String programsForSelectedChannel = "aaa, bbb, ccc, ddd";
            String selectedChannelId = channelId.toString();
            String selectedPosition = Integer.valueOf(position).toString();
            String programsForSelectedChannel = createProgramList(programs);

            if (DEBUG) {

                Log.d(TAG, "channel num - " + selectedChannelNumber);
                Log.d(TAG, "channel name - " + selectedChannelName);
                Log.d(TAG, "program list - " + programsForSelectedChannel);
                Log.d(TAG, "channel position - " + selectedPosition);
            }

            channelNumber.setText(selectedChannelNumber);
            channelName.setText(selectedChannelName);
//            channelIndex.setText(selectedChannelId);
            programName.setText(programsForSelectedChannel);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(channelNumber);
                }
            });
        }

        private String createProgramList(final List<Program> programs) {

            StringBuilder sb = new StringBuilder();
            int count = 0;

            for (Program program : programs) {

                @SuppressLint("RestrictedApi") String title = program.getTitle();

                sb.append(title);
                sb.append(", ");
                if (count >= 3) {
                    break;
                }
                count++;
            }
            int len = sb.length();
            if (len < 2) {
                sb.append("No Program List Available.");
                return sb.toString();
            }
            return sb.substring(0, len - 2);

        }
    }
}
