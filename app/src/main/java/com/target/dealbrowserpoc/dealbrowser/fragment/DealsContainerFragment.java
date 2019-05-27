package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;

public class DealsContainerFragment extends BaseFragment implements DealListFragment.Listener,
        DealDetailFragment.Listener {
    public static DealsContainerFragment newInstance() {
        return new DealsContainerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, DealListFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }

        return view;
    }

    @Override
    public void onDealClicked(@NonNull DealListFragment sender, @NonNull Deal deal) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, DealDetailFragment.newInstance(deal.getGuid()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackArrowPressed(@NonNull DealDetailFragment fragment) {
        handleBackPressed();
    }
}
