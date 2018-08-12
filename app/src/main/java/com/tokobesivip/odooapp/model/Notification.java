package com.tokobesivip.odooapp.model;

import android.text.format.DateFormat;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Notification {

    public static final String COLLECTION = "notification";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_ALERT = "alert";
    public static final String FIELD_BRANCH = "branch";
    public static final String FIELD_DATE = "timestamp";


    //@ServerTimestamp
    private String date;
    private String title;
    private String message;
    private String state;
    private String lines;
    private String alert;

    public Notification() {}

    public Notification(String date, String title,String message, String state,String lines,String alert) {
        this.date = date;
        this.title = title;
        this.message = message;
        this.state = state;
        this.lines = lines;
        this.alert = alert;
    }

    public String getDate() {return date ; }
    public void setDate(String date) {this.date = date;}

    public String getState() {return state; }
    public void setState(String state){this.state =  state; }

    public String getMessage() { return message;  }
    public void setMessage(String message) { this.message = message; }

    public String getTitle() { return title;  }
    public void setTitle(String title) { this.title = title; }

    public String getLines() { return lines;  }
    public void setLines(String lines) { this.lines = lines; }

    public String getAlert() { return alert;  }
    public void setAlert(String alert) { this.alert = alert; }

}
