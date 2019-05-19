package com.target.dealbrowserpoc.dealbrowser.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.core.GlideRequests;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;

public class DealListAdapter extends RealmRecyclerViewAdapter<Deal, DealListAdapter.ViewHolder> {
    private final Context context;
    private final GlideRequests glide;

    public DealListAdapter(Context context, GlideRequests glide) {
        super(null, true);
        this.context = context;
        this.glide = glide;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_deal_list_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.update(getItem(i));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView titleLabel;

        @BindView(R.id.price)
        TextView priceLabel;

        @BindView(R.id.aisle)
        TextView aisleLabel;

        @BindView(R.id.image)
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void update(Deal deal) {
            titleLabel.setText(deal.getTitle());
            priceLabel.setText(deal.getPrice()); // TODO: display sale if cheaper?
            aisleLabel.setText(deal.getAisle());
            glide.load(deal.getImageUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView); // TODO: add placeholder
        }
    }
}
