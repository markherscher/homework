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
import com.target.dealbrowserpoc.dealbrowser.model.CartEntry;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;
import com.target.dealbrowserpoc.dealbrowser.util.DollarFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class DealDetailFragment extends BaseFragment {
    private final static String GUID_KEY = "guid-key";

    @BindView(R.id.title)
    TextView titleLabel;

    @BindView(R.id.description)
    TextView descriptionLabel;

    @BindView(R.id.actual_price)
    TextView actualPriceLabel;

    @BindView(R.id.regular_price)
    TextView regularPriceLabel;

    @BindView(R.id.add_to_cart)
    TextView addToCartButton;

    @BindView(R.id.image)
    ImageView imageView;

    private String dealGuid;
    private String imageUrl;
    private Realm realm;
    private RealmResults<CartEntry> cartResults;

    public static DealDetailFragment newInstance(@NonNull String dealGuid) {
        DealDetailFragment fragment = new DealDetailFragment();
        Bundle args = new Bundle();
        args.putString(GUID_KEY, dealGuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        dealGuid = args == null ? null : args.getString(GUID_KEY);
        if (dealGuid == null) {
            throw new IllegalArgumentException("missing deal GUID");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();

        Deal deal = realm.where(Deal.class).equalTo(Deal.GUID, dealGuid).findFirst();
        if (deal == null) {
            throw new IllegalArgumentException("failed to find Deal");
        }

        imageUrl = deal.getImageUrl();
        showDeal(deal);
        cartResults = realm.where(CartEntry.class).equalTo(CartEntry.DEAL_GUID, dealGuid)
                .findAllAsync();
        cartResults.addChangeListener(new CartChangeListener());
    }

    @Override
    public void onPause() {
        super.onPause();
        cartResults.removeAllChangeListeners();
        realm.close();
    }

    @OnClick(R.id.add_to_list)
    void onAddToListClicked() {

    }

    @OnClick(R.id.add_to_cart)
    void onAddToCartClicked() {
        incrementCartCount();
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
            String pricePortion = DollarFormatter.fromCents(regularPrice);
            SpannableString span = new SpannableString(getString(R.string.regular_price_fmt, pricePortion));
            span.setSpan(new StrikethroughSpan(),
                    span.length() - pricePortion.length(),
                    span.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            regularPriceText = span;
        }

        regularPriceLabel.setText(regularPriceText);
        actualPriceLabel.setText(DollarFormatter.fromCents(actualPrice));

        titleLabel.setText(deal.getTitle());
        descriptionLabel.setText(deal.getDescription());
        GlideApp.with(this).load(deal.getImageUrl())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView); // TODO: add placeholder
    }

    private void incrementCartCount() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm r) {
                CartEntry cartEntry = r.where(CartEntry.class)
                        .equalTo(CartEntry.DEAL_GUID, dealGuid)
                        .findFirst();

                if (cartEntry == null) {
                    cartEntry = new CartEntry(dealGuid, 0);
                }

                cartEntry.setCount(cartEntry.getCount() + 1);
                r.copyToRealmOrUpdate(cartEntry);
            }
        });
    }

    private class CartChangeListener implements RealmChangeListener<RealmResults<CartEntry>> {
        @Override
        public void onChange(@NonNull RealmResults<CartEntry> cartEntries) {
            CartEntry cart = cartEntries.first(null);
            int count = cart == null ? 0 : cart.getCount();

            if (count <= 0) {
                addToCartButton.setText(getString(R.string.add_to_cart));
            } else {
                addToCartButton.setText(getString(R.string.add_to_cart_fmt, count));
            }
        }
    }
}
