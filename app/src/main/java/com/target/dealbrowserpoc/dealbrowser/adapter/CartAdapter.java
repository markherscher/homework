package com.target.dealbrowserpoc.dealbrowser.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.model.CartEntry;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;
import com.target.dealbrowserpoc.dealbrowser.util.DollarFormatter;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final Delegate delegate;
    private final Listener listener;

    private List<CartEntry> cartEntries;

    public CartAdapter(@NonNull Delegate delegate, @NonNull Listener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    public void setCartEntries(@Nullable List<CartEntry> cartEntries) {
        this.cartEntries = cartEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_cart_adapter, viewGroup, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder viewHolder, int i) {
        if (cartEntries != null) {
            CartEntry cartEntry = cartEntries.get(i);

            if (cartEntry != null) {
                Deal deal = delegate.getDeal(cartEntry);

                if (deal != null) {
                    // Jeez that's a lot of nested ifs...I miss Kotlin
                    viewHolder.update(cartEntry, deal);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return cartEntries == null ? 0 : cartEntries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.deal_name)
        TextView name;

        @BindView(R.id.deal_quantity)
        TextView quantity;

        @BindView(R.id.deal_price)
        TextView price;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void update(@NonNull CartEntry cartEntry, @NonNull Deal deal) {
            name.setText(deal.getTitle());
            quantity.setText(String.format(Locale.US, "%d", cartEntry.getCount()));
            price.setText(DollarFormatter.fromCents(deal.getActualPrice() * cartEntry.getCount()));
        }

        @OnClick(R.id.container)
        void onThisClicked() {
            CartEntry cartEntry = getCartEntry();
            if (cartEntry != null) {
                listener.onClicked(CartAdapter.this, cartEntry);
            }
        }

        @OnClick(R.id.decrease)
        void onDecrementClicked() {
            CartEntry cartEntry = getCartEntry();
            if (cartEntry != null) {
                listener.onDecrementCartEntry(CartAdapter.this, cartEntry);
            }
        }

        @OnClick(R.id.increase)
        void onIncrementClicked() {
            CartEntry cartEntry = getCartEntry();
            if (cartEntry != null) {
                listener.onIncrementCartEntry(CartAdapter.this, cartEntry);
            }
        }

        private CartEntry getCartEntry() {
            return cartEntries.get(getAdapterPosition());
        }
    }

    public interface Delegate {
        @Nullable
        Deal getDeal(@NonNull CartEntry cartEntry);
    }

    public interface Listener {
        void onClicked(@NonNull CartAdapter adapter, @NonNull CartEntry cartEntry);

        void onDecrementCartEntry(@NonNull CartAdapter adapter, @NonNull CartEntry cartEntry);

        void onIncrementCartEntry(@NonNull CartAdapter adapter, @NonNull CartEntry cartEntry);
    }
}
