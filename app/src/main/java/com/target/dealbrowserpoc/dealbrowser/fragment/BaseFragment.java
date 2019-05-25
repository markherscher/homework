package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public boolean handleBackPressed() {
        boolean isHandled = getChildFragmentManager().getBackStackEntryCount() > 1;

        if (isHandled) {
            getChildFragmentManager().popBackStack();
        }

        return isHandled;
    }
}
