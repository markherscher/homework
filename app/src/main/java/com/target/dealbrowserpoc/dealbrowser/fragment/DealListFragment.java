package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.adapter.DealListAdapter;
import com.target.dealbrowserpoc.dealbrowser.adapter.LinearLayoutMarginDecorator;
import com.target.dealbrowserpoc.dealbrowser.core.GlideApp;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DealListFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindDimen(R.dimen.deal_list_separator_height)
    int rowSeparatorHeight;

    private DealListAdapter adapter;
    private Realm realm;
    private Listener listener;

    public static DealListFragment newInstance() {
        return new DealListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_list, container, false);
        ButterKnife.bind(this, view);

        adapter = new DealListAdapter(getContext(),
                GlideApp.with(this),
                new DealListAdapter.Listener() {
                    @Override
                    public void onItemClicked(@NonNull Deal deal) {
                        handleDealClicked(deal);
                    }
                });
        recyclerView.setAdapter(adapter);

        showAsLinearList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        executeQuery();
    }

    @Override
    public void onPause() {
        super.onPause();
        realm.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void handleDealClicked(Deal deal) {
        if (listener != null) {
            listener.onDealClicked(this, deal);
        }
    }

    private void showAsLinearList() {
        int count = recyclerView.getItemDecorationCount();
        for (int i = 0; i < count; i++) {
            recyclerView.removeItemDecorationAt(i);
        }

        recyclerView.addItemDecoration(new LinearLayoutMarginDecorator(rowSeparatorHeight));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
    }

    private void executeQuery() {
        adapter.updateData(realm.where(Deal.class).sort(Deal.ORDER).findAllAsync());
    }

    public interface Listener {
        void onDealClicked(@NonNull DealListFragment sender, @NonNull Deal deal);
    }
}
