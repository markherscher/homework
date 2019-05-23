package com.target.dealbrowserpoc.dealbrowser.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.target.dealbrowserpoc.dealbrowser.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.ViewHolder> {
    private final Context context;
    private final List<String> items;
    private final int layoutResourceId;
    private final Listener listener;

    /**
     *
     * @param context
     * @param items
     * @param layoutResourceId any layout, but it must contain a {@code TextView} with an ID of 'label'
     * @param listener
     */
    public SimpleListAdapter(@NonNull Context context,
                             @NonNull List<String> items,
                             int layoutResourceId,
                             @NonNull Listener listener) {
        this.context = context;
        this.items = items;
        this.layoutResourceId = layoutResourceId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(layoutResourceId, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(items.get(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.label) TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(SimpleListAdapter.this, getAdapterPosition());
                }
            });
        }
    }

    public interface Listener {
        void onItemClicked(SimpleListAdapter adapter, int index);
    }
}
