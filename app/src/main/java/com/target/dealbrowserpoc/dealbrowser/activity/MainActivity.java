package com.target.dealbrowserpoc.dealbrowser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.fragment.DealListFragment;
import com.target.dealbrowserpoc.dealbrowser.service.DealApiService;

public class MainActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_main);

        if (savedInstancedState == null) {
            startService(new Intent(this, DealApiService.class));
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, DealListFragment.newInstance())
                    .commit();
        }
    }
}
