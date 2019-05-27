package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.model.CartEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class CartContainerFragment extends BaseFragment implements CartFragment.Listener {
    @BindView(R.id.container)
    View container;

    public static CartContainerFragment newInstance() {
        return new CartContainerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, CartFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }

        return view;
    }

    @Override
    public void onDealClicked(@NonNull CartFragment fragment, @NonNull String dealGuid) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, DealDetailFragment.newInstance(dealGuid))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCheckoutClicked(@NonNull CartFragment fragment) {
        Snackbar.make(container, R.string.checkout_snackbar, Snackbar.LENGTH_LONG).show();

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm r) {
                r.delete(CartEntry.class);
            }
        });

        realm.close();
    }
}
