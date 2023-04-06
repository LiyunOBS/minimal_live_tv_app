package com.mini.livetvapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    private List<String> mData;
    private LayoutInflater mInflater;
    private Context context;
    private final OnItemClickListener listener;


    public ListAdapter(List<String> itemList, Context context, OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
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
        holder.bindData(mData.get(position), listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
        }


        void bindData(final String item, final OnItemClickListener listener) {
            name.setText(item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
