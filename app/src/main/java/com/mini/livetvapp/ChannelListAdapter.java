package com.mini.livetvapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        void onItemClick(TextView view);
    }

//    private List<String> mData;
    private List<ChannelInfo> channelInfoList;
    private List<List<Program>> programList;
    private LayoutInflater mInflater;
    private Context context;
    private final OnItemClickListener listener;

    public ChannelListAdapter(List<ChannelInfo> channels, List<List<Program>> programs, Context context, OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.channelInfoList = channels;
        this.programList = programs;
        this.listener = listener;
        if(DEBUG) {
            Log.d(TAG, "after channelListAdapter is newly created.");
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
        holder.bindData(channelInfoList.get(position), programList.get(position), listener);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView channelNumber;
        TextView channelName;
        TextView programName;
//        CardView cv;

        ViewHolder(View itemView) {
            super(itemView);
            channelNumber = itemView.findViewById(R.id.channelNumber);
            channelName = itemView.findViewById(R.id.channelName);
            programName = itemView.findViewById(R.id.programList);
//            cv = itemView.findViewById(R.id.cv_input);
        }


        void bindData(final ChannelInfo channel, final List<Program> programs, final OnItemClickListener listener) {

            String selectedChannelNumber = channel.getNumber();
            String selectedChannelName = channel.getName();
//            String programsForSelectedChannel = "aaa, bbb, ccc, ddd";
            String programsForSelectedChannel = createProgramList(programs);

            if (DEBUG) {

                Log.d(TAG, "channel num - " + selectedChannelNumber);
                Log.d(TAG, "channel name - " + selectedChannelName);
                Log.d(TAG, "program list - " + programsForSelectedChannel);
            }

            channelNumber.setText(selectedChannelNumber);
            channelName.setText(selectedChannelName);
            programName.setText(programsForSelectedChannel);
            channelName.setOnClickListener(new TextView.OnClickListener() {
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
                sb.append(" ,");
                if (count >= 3) {
                    break;
                }
                count++;
            }

            sb.substring(0, sb.length() - 2);

            return sb.toString();

        }
    }
}
