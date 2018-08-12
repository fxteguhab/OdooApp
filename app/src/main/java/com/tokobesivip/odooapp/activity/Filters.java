package com.tokobesivip.odooapp.activity;

import android.content.Context;
import android.text.TextUtils;

import com.tokobesivip.odooapp.model.Notification;
import com.tokobesivip.odooapp.util.NotificationUtils;
import com.google.firebase.firestore.Query;

/**
 * Object for passing filters around.
 */
public class Filters {

    private String category = null;
    private int alert = -1;
    private String sortBy = null;
    private String sortDir = null;

    private String state = null;
    private Query.Direction sortDirection = null;

    public Filters() {}

    public static Filters getDefault() {
        Filters filters = new Filters();
        filters.setSortBy(Notification.FIELD_DATE);
        filters.setSortDirection(Query.Direction.DESCENDING);

        return filters;
    }

    public boolean hasCategory() {
        return !(TextUtils.isEmpty(category));
    }

    public boolean hasAlert() {
        return (alert > 0);
    }

    public boolean hasSortBy() {
        return !(TextUtils.isEmpty(sortBy));
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getAlert() {
        return alert;
    }

    public void setAlert(int alert) {
        this.alert = alert;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Query.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Query.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSearchDescription(Context context) {
        StringBuilder desc = new StringBuilder();

        if (category == null) {
            desc.append("<b>");
            desc.append("All Notification");
            desc.append("</b>");
        }

        if (category != null) {
            desc.append("<b>");
            desc.append(category);
            desc.append("</b>");
        }

        return desc.toString();
    }

    public String getOrderDescription(Context context) {
        if (Notification.FIELD_ALERT.equals(sortBy)) {
            return "Sort by Alert";
        } else if (Notification.FIELD_DATE.equals(sortBy)) return "Sort by Date";
        return null;
    }

    public String getOrderDirection(Context context) {
        if (this.sortDirection == Query.Direction.DESCENDING){
            return "Descending";} else return "Ascending";

    }

}
