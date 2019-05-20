package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.core.GlideApp;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class DealDetailFragment extends BaseFragment {
    private final static String GUID_KEY ="guid-key";

    @BindView(R.id.title)
    TextView titleLabel;

    @BindView(R.id.description)
    TextView descriptionLabel;

    @BindView(R.id.price)
    TextView priceLabel;

    @BindView(R.id.regular_price)
    TextView regularPriceLabel;

    @BindView(R.id.image)
    ImageView imageView;

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

    private void showDeal(Deal deal) {
        titleLabel.setText(deal.getTitle());
        descriptionLabel.setText(deal.getDescription());
        priceLabel.setText(deal.getPrice());
        regularPriceLabel.setText(deal.getSalePrice()); // TODO fix
        GlideApp.with(this).load(deal.getImageUrl())
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView); // TODO: add placeholder
    }
}
