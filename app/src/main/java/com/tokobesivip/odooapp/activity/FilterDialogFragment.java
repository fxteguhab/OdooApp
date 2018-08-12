package com.tokobesivip.odooapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.tokobesivip.odooapp.R;
import com.tokobesivip.odooapp.model.Notification;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Dialog Fragment containing filter form.
 */
public class FilterDialogFragment extends DialogFragment {

    public static final String TAG = "FilterDialog";

    interface FilterListener {

        void onFilter(Filters filters);

    }

    private View mRootView;

    @BindView(com.tokobesivip.odooapp.R.id.spinner_category)
    Spinner mCategorySpinner;

    @BindView(com.tokobesivip.odooapp.R.id.spinner_sort)
    Spinner mSortSpinner;

    @BindView(R.id.spinner_sort_direction)
    Spinner mSortDirectionSpinner;

    private FilterListener mFilterListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(com.tokobesivip.odooapp.R.layout.dialog_filters, container, false);
        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterListener) {
            mFilterListener = (FilterListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(com.tokobesivip.odooapp.R.id.button_search)
    public void onSearchClicked() {
        if (mFilterListener != null) {
            mFilterListener.onFilter(getFilters());
        }
        dismiss();
    }

    @OnClick(com.tokobesivip.odooapp.R.id.button_cancel)
    public void onCancelClicked() {
        dismiss();
    }

    @Nullable
    private String getSelectedCategory() {
        String selected = (String) mCategorySpinner.getSelectedItem();
        if ("All Category".equals(selected)) {
            return null;
        } else {
            return selected;
        }
    }

    @Nullable
    private String getSelectedSortBy() {
        String selected = (String) mSortSpinner.getSelectedItem();
        if ("Sort by Alert".equals(selected)) {
            return Notification.FIELD_ALERT;
        } if ("Sort by Date".equals(selected)) {
            return Notification.FIELD_DATE;
        } else {
            return null;
        }
    }

    @Nullable
    private Query.Direction getSortDirection() {
        String selected = (String) mSortDirectionSpinner.getSelectedItem();
        if ("Ascending".equals(selected)) {
            return Query.Direction.ASCENDING;
        } else if ("Descending".equals(selected)) {
            return Query.Direction.DESCENDING;
        } else {
            return null;
        }
    }

    public void resetFilters() {
        if (mRootView != null) {
            mCategorySpinner.setSelection(0);
            mSortSpinner.setSelection(0);
            mSortDirectionSpinner.setSelection(0);
        }
    }

    public Filters getFilters() {
        Filters filters = new Filters();

        if (mRootView != null) {
            filters.setCategory(getSelectedCategory());
            filters.setSortBy(getSelectedSortBy());
            filters.setSortDirection(getSortDirection());
        }

        return filters;
    }
}
