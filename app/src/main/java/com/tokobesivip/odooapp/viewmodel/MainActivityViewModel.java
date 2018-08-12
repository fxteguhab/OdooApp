package com.tokobesivip.odooapp.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.tokobesivip.odooapp.activity.Filters;

/**
 * ViewModel for {@link com.tokobesivip.odooapp.activity.MainActivity}.
 */

public class MainActivityViewModel extends ViewModel {

    private Filters mFilters;

    public MainActivityViewModel() {
        mFilters = Filters.getDefault();
    }

    public Filters getFilters() {
        return mFilters;
    }

    public void setFilters(Filters mFilters) {
        this.mFilters = mFilters;
    }
}
