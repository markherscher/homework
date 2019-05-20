package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.target.dealbrowserpoc.dealbrowser.R;

import butterknife.ButterKnife;

public class DealDetailFragment extends BaseFragment {
    private final static String GUID_KEY ="guid-key";

    public static DealDetailFragment newInstance(@NonNull String dealGuid) {
        DealDetailFragment fragment = new DealDetailFragment();
        Bundle args = new Bundle();
        args.putString(GUID_KEY, dealGuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        String dealGuid = args == null ? null : args.getString(GUID_KEY);
        if (dealGuid == null) {
            throw new IllegalArgumentException("missing deal GUID");
        }

        return view;
    }
}
