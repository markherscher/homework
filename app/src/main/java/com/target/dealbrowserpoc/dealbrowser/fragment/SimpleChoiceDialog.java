package com.target.dealbrowserpoc.dealbrowser.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.target.dealbrowserpoc.dealbrowser.R;
import com.target.dealbrowserpoc.dealbrowser.adapter.SimpleListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleChoiceDialog<T> extends Dialog {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private final T[]values;
    private final Delegate<T> delegate;
    private final Listener<T> listener;
    private final int dialogLayoutResourceId;
    private final int itemLayoutResourceId;

    public SimpleChoiceDialog(@NonNull Context context,
                              @NonNull Delegate<T> delegate,
                              @NonNull Listener<T> listener,
                              @NonNull T[] values,
                              int dialogLayoutResourceId,
                              int itemLayoutResourceId) {
        super(context);

        this.delegate = delegate;
        this.listener = listener;
        this.values = values;
        this.dialogLayoutResourceId = dialogLayoutResourceId;
        this.itemLayoutResourceId = itemLayoutResourceId;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogLayoutResourceId);
        ButterKnife.bind(this);

        List<String> textList = new ArrayList<>();
        for (T t : values) {
            textList.add(delegate.getStringForItem(t));
        }

        SimpleListAdapter adapter = new SimpleListAdapter(
                getContext(),
                textList,
                itemLayoutResourceId,
                new SimpleListAdapter.Listener() {
                    @Override
                    public void onItemClicked(SimpleListAdapter adapter, int index) {
                        if (index >= 0 && index < values.length) {
                            listener.onItemSelected(values[index]);
                        }

                        dismiss();
                    }
                });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public interface Delegate<T> {
        @NonNull
        String getStringForItem(T value);
    }

    public interface Listener<T> {
        void onItemSelected(T item);
    }
}
