package com.target.dealbrowserpoc.dealbrowser.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.core.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoActivity extends BaseActivity {
    public static final String IMAGE_URL_KEY = "image-url-key";

    @BindView(R.id.image)
    ImageView imageView;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        String imageUrl = getIntent().getStringExtra(IMAGE_URL_KEY);
        if (imageUrl == null) {
            throw new IllegalArgumentException("missing image URL");
        }

        GlideApp.with(this).load(imageUrl).centerInside().into(imageView);
    }

    @OnClick(R.id.back_arrow)
    void onBackArrowClicked() {
        finish();
    }
}
