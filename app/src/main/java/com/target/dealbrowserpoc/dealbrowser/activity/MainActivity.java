package com.target.dealbrowserpoc.dealbrowser.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.fragment.DealListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private static final int DEAL_TAB_INDEX = 0;
    private static final int CART_TAB_INDEX = 1;
    private static final int TAB_COUNT = 2;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Don't care
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Don't care
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                resetTab();
            }
        });
    }

    private void resetTab() {
        // TODO: reset to root container fragment
    }

    private class TabAdapter extends FragmentPagerAdapter {
        TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case DEAL_TAB_INDEX:
                    return DealListFragment.newInstance();
                case CART_TAB_INDEX:
                    return DealListFragment.newInstance(); // TODO
                default:
                    throw new IllegalArgumentException("unknown position");
            }
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case DEAL_TAB_INDEX:
                    return getString(R.string.tab_deals);
                case CART_TAB_INDEX:
                    return getString(R.string.tab_cart);
                default:
                    throw new IllegalArgumentException("unknown position");
            }
        }
    }
}
