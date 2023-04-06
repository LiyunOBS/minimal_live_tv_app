package com.mini.livetvapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.tvprovider.media.tv.Program;
import com.mini.livetvapp.utils.ChannelInfo;

import java.util.List;

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder>{

    private static final String TAG = "TAG for ChannelsAdapter";
    private static final boolean DEBUG = true;
    private Context mContext;
    private String inputId;
    private List<ChannelInfo> channelInfoList;
    private List<Long> channelIdList;
    private List<List<Program>> programList;

    // RecyclerView recyclerView;
    public ChannelsAdapter(Context mContext, String inputId, List<ChannelInfo> channels, List<Long> channelIds, List<List<Program>> programs) {
        this.mContext = mContext;
        this.inputId = inputId;
        this.channelInfoList = channels;
        this.channelIdList = channelIds;
        this.programList = programs;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.channel_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ChannelInfo channel = channelInfoList.get(position);
        List<Program> programs = programList.get(position);

        String selectedChannelNumber = channel.getNumber();
        String selectedChannelName = channel.getName();
        String programsForSelectedChannel = createProgramList(programs);

        holder.channelName.setText(selectedChannelName);
        holder.channelNumber.setText(selectedChannelNumber);
        holder.programName.setText(programsForSelectedChannel);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+channelIdList.get(position),Toast.LENGTH_LONG).show();

                Intent tvViewIntent = new Intent(mContext, DisplayActivity.class);
                tvViewIntent.putExtra("selectedInputId", inputId);
                String selectedChannelId = channelIdList.get(position).toString();
                tvViewIntent.putExtra("selectedChannelId", selectedChannelId);

                if (DEBUG) {
                    Log.d(TAG, "selected inputId - " + inputId);
                    Log.d(TAG, "selected channel - " + selectedChannelId);
                }
                mContext.startActivity(tvViewIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return channelInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView channelNumber;
        private TextView channelName;
        private TextView programName;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            channelNumber = (TextView) itemView.findViewById(R.id.channelNumber);
            channelName = (TextView) itemView.findViewById(R.id.channelName);
            programName = (TextView) itemView.findViewById(R.id.programList);
            cardView = (CardView)itemView.findViewById(R.id.channelCV);
        }
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
