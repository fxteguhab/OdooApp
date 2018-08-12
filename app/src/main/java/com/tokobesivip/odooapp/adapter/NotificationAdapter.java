package com.tokobesivip.odooapp.adapter;

import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.firebase.firestore.Query;
import com.tokobesivip.odooapp.R;
import com.tokobesivip.odooapp.model.Notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationAdapter extends FirestoreAdapter<NotificationAdapter.ViewHolder> {

    public interface OnNotificationSelectedListener {

        void onNotificationSelected(DocumentSnapshot notification);
        void onButtonDelete(DocumentSnapshot notification);
    }

    private OnNotificationSelectedListener mListener;

    public NotificationAdapter(Query query, OnNotificationSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_tanggal)
        TextView dateView;

        @BindView(R.id.item_title)
        TextView titleView;

        @BindView(R.id.item_message)
        TextView messageView;

        @BindView(R.id.item_alert)
        TextView alertView;

        @BindView(R.id.button_delete2)
        FloatingActionButton button_Delete2;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bind(final DocumentSnapshot snapshot,
                         final OnNotificationSelectedListener listener
                         ) {

            Notification notification = snapshot.toObject(Notification.class);
            Resources resources = itemView.getResources();

            dateView.setText(notification.getDate());
            titleView.setText(notification.getTitle());
            messageView.setText(notification.getMessage());
            alertView.setText((notification.getAlert()));

            /*
            //State condition
            if (notification.getState().equals("unread")){
                stateView.setChecked(false);
            } else {
                stateView.setChecked(true);
            }
            */

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onNotificationSelected(snapshot);
                    }
                }
            });

            button_Delete2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onButtonDelete(snapshot);
                    }
                }
            });
        }

    }
}
