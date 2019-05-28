package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.adapter.DealListAdapter;
import com.target.dealbrowserpoc.dealbrowser.adapter.LinearLayoutMarginDecorator;
import com.target.dealbrowserpoc.dealbrowser.core.GlideApp;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;
import com.target.dealbrowserpoc.dealbrowser.model.Sort;
import com.target.dealbrowserpoc.dealbrowser.service.DealApiService;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;

public class DealListFragment extends BaseFragment
        implements SimpleChoiceDialog.Delegate<Sort>, SimpleChoiceDialog.Listener<Sort> {
    private static final String LAYOUT_MODE_KEY = "layout-mode-key";
    private static final int LAYOUT_MODE_LIST = 0;
    private static final int LAYOUT_MODE_GRID = 1;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.layout_switcher)
    ImageView layoutSwitcher;

    @BindView(R.id.search)
    SearchView searchView;

    @BindView(R.id.loading)
    View loadingIndicator;

    @BindView(R.id.loading_error)
    View loadingError;

    @BindView(R.id.refresh)
    View refresh;

    @BindDimen(R.dimen.deal_list_separator_height)
    int rowSeparatorHeight;

    @BindInt(R.integer.deal_list_max_columns)
    int maxColumns;

    private DealListAdapter adapter;
    private SimpleChoiceDialog sortDialog;
    private RecyclerView.ItemDecoration linearDecorator;
    private Realm realm;
    private Listener listener;
    private int layoutMode;
    private Sort sort;

    public static DealListFragment newInstance() {
        return new DealListFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DealApiService.getState().isRunning().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                boolean isLoading = aBoolean == null ? false : aBoolean;
                refresh.setEnabled(!isLoading);
                refresh.setAlpha(isLoading ? 0.5f : 1.0f);
                loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });

        DealApiService.getState().wasLastSuccessful().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isSuccessful) {
                if (isSuccessful != null) {
                    loadingError.setVisibility(isSuccessful ? View.GONE : View.VISIBLE);
                    recyclerView.setVisibility(isSuccessful ? View.VISIBLE : View.GONE);
                } else {
                    loadingError.setVisibility(View.GONE);
                }
            }
        });
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
        linearDecorator = new LinearLayoutMarginDecorator(rowSeparatorHeight);

        layoutMode = savedInstanceState == null
                ? LAYOUT_MODE_LIST
                : savedInstanceState.getInt(LAYOUT_MODE_KEY, LAYOUT_MODE_LIST);

        setupAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        searchView.setOnQueryTextListener(searchViewListener);
        executeQuery();
    }

    @Override
    public void onPause() {
        // Clear query text listener because it depends on Realm being open
        super.onPause();
        searchView.setOnQueryTextListener(null);
        realm.close();

        if (sortDialog != null) {
            sortDialog.dismiss();
            sortDialog = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else if (getParentFragment() instanceof Listener) {
            listener = (Listener) getParentFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void onSaveInstanceState(@NonNull Bundle out) {
        super.onSaveInstanceState(out);
        out.putInt(LAYOUT_MODE_KEY, layoutMode);
    }

    @OnClick(R.id.layout_switcher)
    void onLayoutChangeClicked() {
        layoutMode = layoutMode == LAYOUT_MODE_GRID ? LAYOUT_MODE_LIST : LAYOUT_MODE_GRID;
        setupAdapter();
    }

    @OnClick(R.id.sort)
    void onSortClicked() {
        Context context = getContext();
        if (context != null) {
            sortDialog = new SimpleChoiceDialog<>(
                    context,
                    this,
                    this,
                    Sort.values(),
                    R.layout.dialog_sort,
                    R.layout.view_simple_list_adapter);
            sortDialog.show();
        }
    }

    @OnClick(R.id.refresh)
    void onRefreshClicked() {
        Context context = getContext();
        if (context != null) {
            context.startService(new Intent(context, DealApiService.class));
        }
    }

    @NonNull
    @Override
    public String getStringForItem(Sort value) {
        switch (value) {
            case NAME:
                return getString(R.string.sort_name);
            case PRICE_ASCENDING:
                return getString(R.string.sort_price_ascending);
            case PRICE_DESCENDING:
                return getString(R.string.sort_price_descending);
            default:
                return "";
        }
    }

    @Override
    public void onItemSelected(Sort item) {
        if (sort != item) {
            sort = item;
            executeQuery();
        }
    }

    private void handleDealClicked(Deal deal) {
        if (listener != null) {
            listener.onDealClicked(this, deal);
        }
    }

    private void setupAdapter() {
        int layoutModeToUse = layoutMode;
        if (maxColumns == 1) {
            // Don't allow switching to grid view because it does nothing. Don't overwrite the
            // value for the field 'layoutMode' though, because if it was in grid mode that
            // should be activated when they rotate back
            layoutModeToUse = LAYOUT_MODE_LIST;
            layoutSwitcher.setVisibility(View.GONE);
        }

        int columnCount;

        switch (layoutModeToUse) {
            default:
                columnCount = 1;
                recyclerView.addItemDecoration(linearDecorator);
                layoutSwitcher.setImageResource(R.drawable.list_option);
                break;

            case LAYOUT_MODE_GRID:
                columnCount = maxColumns;
                recyclerView.removeItemDecoration(linearDecorator);
                layoutSwitcher.setImageResource(R.drawable.grid_option);
                break;
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
    }

    private void executeQuery() {
        String nameSearch = searchView.getQuery().toString().trim();
        RealmQuery<Deal> query = realm.where(Deal.class);
        if (!nameSearch.isEmpty()) {
            query = query.contains(Deal.TITLE, nameSearch, Case.INSENSITIVE);
        }

        if (sort == Sort.PRICE_ASCENDING) {
            query = query.sort(Deal.ACTUAL_PRICE, io.realm.Sort.ASCENDING);
        } else if (sort == Sort.PRICE_DESCENDING) {
            query = query.sort(Deal.ACTUAL_PRICE, io.realm.Sort.DESCENDING);
        } else {
            query = query.sort(Deal.TITLE, io.realm.Sort.ASCENDING);
        }

        adapter.updateData(query.findAllAsync());
    }

    private final SearchView.OnQueryTextListener searchViewListener =
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    executeQuery();
                    return false;
                }
            };

    public interface Listener {
        void onDealClicked(@NonNull DealListFragment sender, @NonNull Deal deal);
    }
}
