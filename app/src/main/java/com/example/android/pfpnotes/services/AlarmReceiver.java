package com.example.android.pfpnotes.services;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.pfpnotes.PFPAppWidget;
import com.example.android.pfpnotes.R;
import com.example.android.pfpnotes.data.DateHelper;
import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.models.Note;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int START_HOUR = 8;
    public static final int END_HOUR = 17;
    public static final int WORK_HOURS = END_HOUR - START_HOUR;
    public static final double WAGE_PER_DAY = 120;

    private int numberOfNotes;
    private double price;
    private double expectedPrice;
    private int passedHours;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!setWidgetData(context)){
            return;
        }
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pfpapp_widget);
        views.setTextViewText(R.id.widget_tv_price,
                String.format(context.getResources().getString(R.string.price_text), price));
        views.setTextViewText(R.id.widget_tv_info,
                String.format(context.getResources().getString(R.string.widget_info_text),
                        numberOfNotes, expectedPrice));
        views.setProgressBar(R.id.widget_progress_bar, WORK_HOURS, passedHours, false);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context, PFPAppWidget.class), views);
    }

    private boolean setWidgetData(Context context) {
        String currentDate = DateHelper.getCurrentDate();
        String currentHourString = currentDate.substring(11, 13);
        int currentHour = Integer.valueOf(currentHourString);
        passedHours = currentHour - START_HOUR;
        if (0 < passedHours && passedHours <= WORK_HOURS) {
            expectedPrice = passedHours * (WAGE_PER_DAY / WORK_HOURS);
        } else {
            if(passedHours < 0){
                passedHours = 0;
            } else {
                passedHours = WORK_HOURS;
            }
            return false;
        }
        ArrayList<Note> noteList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                DbContract.NoteEntry.CONTENT_URI,
                null,
                DbContract.NoteEntry.COLUMN_DATE + " LIKE ?",
                new String[]{"%" + DateHelper.getCurrentDate().substring(0, 11) + "%"},
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                noteList.add(new Note(cursor));
            }
            cursor.close();
        }

        numberOfNotes = noteList.size();
        price = 0;
        for (Note note : noteList) {
            price += note.getPrice();
        }

        return true;
    }
}
