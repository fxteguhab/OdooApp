package com.tokobesivip.odooapp.activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import com.tokobesivip.odooapp.R;
import com.tokobesivip.odooapp.model.Notification;
import com.tokobesivip.odooapp.util.NotificationUtils;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;



public class NotificationDetailActivity extends AppCompatActivity
        implements EventListener<DocumentSnapshot> {


    private static final String TAG = "NotificationDetail";

    public static final String KEY_NOTIFICATION_ID = "key_notification_id";

    @BindView(R.id.item_detail_tanggal)
    TextView detaildateView;

    @BindView(R.id.item_detail_title)
    TextView detailtitleView;

    @BindView(R.id.item_detail_message)
    TextView detailmessageView;

    @BindView(R.id.item_detail_state)
    CheckBox detailstateView;

    @BindView(R.id.item_detail_lines)
    TextView detaillinesView;

    @BindView(R.id.item_detail_alert)
    TextView detailalertView;

    private FirebaseFirestore mFirestore;
    private DocumentReference mNotificationRef;
    private ListenerRegistration mNotificationRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        ButterKnife.bind(this);

        // Get notification ID from extras
        String notificationId = getIntent().getExtras().getString(KEY_NOTIFICATION_ID);
        if (notificationId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_NOTIFICATION_ID);
        }

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the notificattion
        mNotificationRef = mFirestore.collection("notification").document(notificationId);

    }

    @Override
    public void onStart() {
        super.onStart();
        mNotificationRegistration = mNotificationRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mNotificationRegistration != null) {
            mNotificationRegistration.remove();
            mNotificationRegistration = null;
        }
    }

    private Task<Void> addState(final DocumentReference notificationRef,
                                 final String state)
                                {

        // In a transaction, add the new rating and update the aggregate totals
        return mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction)
                    throws FirebaseFirestoreException {

                Notification notification = transaction.get(notificationRef)
                        .toObject(Notification.class);

                notification.setState(state);

                // Commit to Firestore
                transaction.set(notificationRef, notification);

                return null;
            }
        });
    }

    private Task<Void> deleteRecord(final DocumentReference notificationRef)
    {

        return mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction)
                    throws FirebaseFirestoreException {

                Notification notification = transaction.get(notificationRef).toObject(Notification.class);

                notificationRef.delete();

                // Commit to Firestore
                transaction.set(notificationRef, notification);

                return null;
            }
        });
    }

    /**
     * Listener for the Notification document ({@link #mNotificationRef}).
     */
    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "notification:onEvent", e);
            return;
        }

        onNotificationLoaded(snapshot.toObject(Notification.class));
    }

    private void onNotificationLoaded(Notification notification) {
        detaildateView.setText(notification.getDate());
        detailtitleView.setText(notification.getTitle());
        detailmessageView.setText(notification.getMessage());
        detaillinesView.setText(notification.getLines());
        detailalertView.setText(notification.getAlert());

        //State condition
        if (notification.getState().equals("unread")){
            detailstateView.setChecked(false);
        } else {
            detailstateView.setChecked(true);
        }
    }

    @OnClick(R.id.button_back)
    public void onBackArrowClicked(View view) {
        onBackPressed();
    }

    @OnClick(R.id.button_delete)
    public void onDeleteArrowClicked(View view) {
        onDelete();
        onBackPressed();
    }
/*
    @Override
    public void onBackPressed() {
        // your code.
        mAdapter.notifyDataSetChanged();
    }
*/
    @OnCheckedChanged(R.id.item_detail_state)
    public void onStateChange(CompoundButton buttonView,boolean isChange){
        if (buttonView.isChecked()){
            onState("read");
        } else
        {
            onState("unread");
        }

    }

    public void onState(String state) {
        // In a transaction, add the new rating and update the aggregate totals
        addState(mNotificationRef, state)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "State changed");
                        // Hide keyboard and scroll to top
                        hideKeyboard();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Change state failed", e);

                        // Show failure message and hide keyboard
                        hideKeyboard();
                        Snackbar.make(findViewById(android.R.id.content), "Failed to change state",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    public void onDelete() {
        deleteRecord(mNotificationRef)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Record Delete");
                        // Hide keyboard and scroll to top
                       // hideKeyboard();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Record Delete failed", e);

                        // Show failure message and hide keyboard
                        //hideKeyboard();
                        //Snackbar.make(findViewById(android.R.id.content), "Failed to change state", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }


    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

