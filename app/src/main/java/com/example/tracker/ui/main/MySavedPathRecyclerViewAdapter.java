package com.example.tracker.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tracker.DisplayPathEvent;
import com.example.tracker.R;
import com.example.tracker.SavedPath;
import com.example.tracker.TravelPath;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.example.tracker.SavedPath}.
 */
public class MySavedPathRecyclerViewAdapter extends RecyclerView.Adapter<MySavedPathRecyclerViewAdapter.ViewHolder> {

    private final List<TravelPath> mValues;
    private SavedPath savedPath;

    public MySavedPathRecyclerViewAdapter(List<TravelPath> items, SavedPath paths) {
        mValues = items;
        savedPath = paths;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_saved_path, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getSavedName());
        holder.mDateView.setText(mValues.get(position).getStartTime());
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EventBus.getDefault().post(new DisplayPathEvent(holder.mItem));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {

                savedPath.removeItem(position);
                notifyDataSetChanged();
                Toast.makeText(v.getContext(), "Path Deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mNameView;
        public final TextView mDateView;
        public TravelPath mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.nameView);
            mDateView = (TextView) view.findViewById(R.id.dateView);
        }
    }

}