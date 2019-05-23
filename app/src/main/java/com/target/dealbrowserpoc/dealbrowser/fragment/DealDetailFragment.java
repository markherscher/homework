package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.activity.PhotoActivity;
import com.target.dealbrowserpoc.dealbrowser.core.GlideApp;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class DealDetailFragment extends BaseFragment {
    private final static String GUID_KEY = "guid-key";
    private final int CENTS_PER_DOLLAR = 100;
    private final NumberFormat dollarFormat = new DecimalFormat("$0.00");

    @BindView(R.id.title)
    TextView titleLabel;

    @BindView(R.id.description)
    TextView descriptionLabel;

    @BindView(R.id.actual_price)
    TextView actualPriceLabel;

    @BindView(R.id.regular_price)
    TextView regularPriceLabel;

    @BindView(R.id.image)
    ImageView imageView;

    private String imageUrl;

    public static DealDetailFragment newInstance(@NonNull String dealGuid) {
        DealDetailFragment fragment = new DealDetailFragment();
        Bundle args = new Bundle();
        args.putString(GUID_KEY, dealGuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        String dealGuid = args == null ? null : args.getString(GUID_KEY);
        if (dealGuid == null) {
            throw new IllegalArgumentException("missing deal GUID");
        }

        Realm realm = Realm.getDefaultInstance();
        Deal deal = realm.where(Deal.class).equalTo(Deal.GUID, dealGuid).findFirst();
        if (deal == null) {
            throw new IllegalArgumentException("failed to find Deal");
        }
        imageUrl = deal.getImageUrl();
        showDeal(deal);
        realm.close();

        return view;
    }

    @OnClick(R.id.add_to_list)
    void onAddToListClicked() {

    }

    @OnClick(R.id.add_to_cart)
    void onAddToCartClicked() {

    }

    @OnClick(R.id.image)
    void onImageClicked() {
        Intent intent = new Intent(getContext(), PhotoActivity.class);
        intent.putExtra(PhotoActivity.IMAGE_URL_KEY, imageUrl);
        startActivity(intent);
    }

    private void showDeal(Deal deal) {
        // If there is no sales price then use the actual price only; otherwise, use the sale
        // price for the actual price and the price for the "regular" price
        int actualPrice = deal.getActualPrice();
        int regularPrice = deal.getRegularPrice();
        CharSequence regularPriceText = "";

        if (actualPrice != regularPrice) {
            // Strike out the price portion
            String pricePortion = dollarFormat.format(regularPrice / (float) CENTS_PER_DOLLAR);
            SpannableString span = new SpannableString(getString(R.string.regular_price_fmt, pricePortion));
            span.setSpan(new StrikethroughSpan(),
                    span.length() - pricePortion.length(),
                    span.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            regularPriceText = span;
        }

        regularPriceLabel.setText(regularPriceText);
        actualPriceLabel.setText(dollarFormat.format(actualPrice / (float) CENTS_PER_DOLLAR));

        titleLabel.setText(deal.getTitle());
        descriptionLabel.setText(deal.getDescription());
        GlideApp.with(this).load(deal.getImageUrl())
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView); // TODO: add placeholder
    }
}
