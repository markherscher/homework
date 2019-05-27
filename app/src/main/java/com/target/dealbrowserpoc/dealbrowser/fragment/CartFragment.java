package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.adapter.CartAdapter;
import com.target.dealbrowserpoc.dealbrowser.model.CartEntry;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;
import com.target.dealbrowserpoc.dealbrowser.util.DollarFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CartFragment extends BaseFragment implements CartAdapter.Delegate, CartAdapter.Listener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.total_price)
    TextView totalPrice;

    @BindView(R.id.check_out)
    View checkOut;

    private Realm realm;
    private RealmResults<CartEntry> cartResults;
    private CartAdapter cartAdapter;
    private Listener listener;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        cartAdapter = new CartAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(cartAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();

        cartResults = realm.where(CartEntry.class).findAllAsync();
        cartResults.addChangeListener(new CartChangeListener());
    }

    @Override
    public void onPause() {
        super.onPause();
        cartResults.removeAllChangeListeners();
        cartResults = null;
        cartAdapter.setCartEntries(null);
        realm.close();
        realm = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else if (getParentFragment() instanceof Listener) {
            listener = (Listener) getParentFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public Deal getDeal(@NonNull CartEntry cartEntry) {
        Deal deal = null;
        if (cartResults != null && realm != null) {
            // Single queries on the primary key are generally quick enough to run on the main thread
            deal = realm.where(Deal.class).equalTo(Deal.GUID, cartEntry.getDealGuid()).findFirst();
        }
        return deal;
    }

    @Override
    public void onClicked(@NonNull CartAdapter adapter, @NonNull CartEntry cartEntry) {
        if (listener != null) {
            listener.onDealClicked(this, cartEntry.getDealGuid());
        }
    }

    @Override
    public void onDecrementCartEntry(@NonNull CartAdapter adapter, @NonNull CartEntry cartEntry) {
        final String cartGuid = cartEntry.getDealGuid();
        if (realm != null) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm r) {
                    // Must look up again on this thread
                    CartEntry ce = r.where(CartEntry.class).equalTo(CartEntry.DEAL_GUID, cartGuid)
                            .findFirst();

                    if (ce != null) {
                        if (ce.getCount() <= 1) {
                            ce.deleteFromRealm();
                        } else {
                            ce.setCount(ce.getCount() - 1);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onIncrementCartEntry(@NonNull CartAdapter adapter, @NonNull CartEntry cartEntry) {
        final String cartGuid = cartEntry.getDealGuid();
        if (realm != null) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm r) {
                    // Must look up again on this thread
                    CartEntry ce = r.where(CartEntry.class).equalTo(CartEntry.DEAL_GUID, cartGuid)
                            .findFirst();

                    if (ce != null) {
                        ce.setCount(ce.getCount() + 1);
                    }
                }
            });
        }
    }

    @OnClick(R.id.check_out)
    void onCheckOutClicked() {
        if (listener != null) {
            listener.onCheckoutClicked(this);
        }
    }

    private void updateTotalPrice() {
        // TODO: This is a candidate for some sort of AsyncTask/worker thread. Each query on
        // the primary key (GUID) is probably fast enough for the main thread, but I get a bad
        // feeling about this all the same.
        int total = 0;
        if (cartResults != null && realm != null) {
            for (CartEntry cartEntry : cartResults) {
                Deal deal = realm.where(Deal.class).equalTo(Deal.GUID, cartEntry.getDealGuid())
                        .findFirst();

                if (deal != null) {
                    total += deal.getActualPrice() * cartEntry.getCount();
                }
            }
        }

        totalPrice.setText(getString(R.string.total_price_fmt, DollarFormatter.fromCents(total)));
    }

    private class CartChangeListener implements RealmChangeListener<RealmResults<CartEntry>> {
        @Override
        public void onChange(@NonNull RealmResults<CartEntry> results) {
            boolean isCartEmpty = results.isEmpty();
            checkOut.setEnabled(!isCartEmpty);
            checkOut.setAlpha(isCartEmpty ? 0.5f : 1.0f);

            cartAdapter.setCartEntries(results);
            updateTotalPrice();
        }
    }


    public interface Listener {
        void onDealClicked(@NonNull CartFragment fragment, @NonNull String dealGuid);

        void onCheckoutClicked(@NonNull CartFragment fragment);
    }
}
