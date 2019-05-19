package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.adapter.DealListAdapter;
import com.target.dealbrowserpoc.dealbrowser.core.GlideApp;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DealListFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private DealListAdapter adapter;
    private Realm realm;

    public static DealListFragment newInstance() {
        return new DealListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_list, container, false);
        ButterKnife.bind(this, view);

        adapter = new DealListAdapter(getContext(), GlideApp.with(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

    private void executeQuery() {
        adapter.updateData(realm.where(Deal.class).sort(Deal.ORDER).findAllAsync());
    }
}
