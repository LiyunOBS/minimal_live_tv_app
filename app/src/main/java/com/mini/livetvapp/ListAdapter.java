package com.mini.livetvapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.tv.TvInputInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private Context context;


    public ListAdapter(List<String> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() { return mData.size(); }


    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.input_element, null);
        return new ListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
//        holder.cv.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));
        holder.bindData(mData.get(position));
    }


    public void setItems(List<String> items) { mData = items; }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView cv;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            cv = itemView.findViewById(R.id.cv_input);
        }


        void bindData(final String item) {
            name.setText(item);
        }
    }
}
