/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.service.myFetchService;


public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String LOG_TAG = "StackRemoteViewsFactory";
    private int mScoreCount = 0;
    private List<WidgetItem> mWidgetItems = new ArrayList<WidgetItem>();
    private Context mContext;
    private int mAppWidgetId;
    private BroadcastReceiver mIntentListener;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        //setupIntentListener();
    }

    private void setupIntentListener() {
        if (mIntentListener == null) {
            mIntentListener = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // Update mUrl through BroadCast Intent
                    setupWidgetData();
                    //mWidgetItems = (ArrayList<WidgetItem>)intent.getExtras().getBundle(ScoreWidgetProvider.WIDGET_ITEM).get(ScoreWidgetProvider.WIDGET_ITEM);
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(ScoreWidgetProvider.SCORE_UPDATE_ACTION);
            mContext.registerReceiver(mIntentListener, filter);
        }
    }

    private void setupWidgetData()
    {
        mScoreCount = 0;

        Date todayDate = new Date(System.currentTimeMillis()+86400000);
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

        Log.e(LOG_TAG, "Loading widget data for date : " + mformat.format(todayDate));

        Cursor mCursor = mContext.getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithDate(),   // The content URI of the words table
                null,                        // The columns to return for each row
                null,                    // Selection criteria
                new String[]{mformat.format(todayDate)},                     // Selection criteria
                null);

        if(mCursor != null) {
            while(mCursor.moveToNext()) {

                mWidgetItems.add(new WidgetItem(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL)),mCursor.getString(mCursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL)),mCursor.getString(mCursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL)), mCursor.getString(mCursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL)), mCursor.getString(mCursor.getColumnIndex(DatabaseContract.scores_table.TIME_COL))));

                mScoreCount++;
            }
        }
    }
    private void teardownIntentListener() {
        if (mIntentListener != null) {
            mContext.unregisterReceiver(mIntentListener);
            mIntentListener = null;
        }
    }

    public void onCreate() {
    }

    public void onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mWidgetItems.clear();
        //teardownIntentListener();
    }

    public int getCount() {
        return mScoreCount;
    }

    public RemoteViews getViewAt(int position) {

        Log.e(LOG_TAG, "Loading widget stack view " + position);

        // position will always range from 0 to getCount() - 1.

        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        Resources res = mContext.getResources();

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.home_name, mWidgetItems.get(position).getHomeName());
        rv.setContentDescription(R.id.home_name, mWidgetItems.get(position).getHomeName());
        rv.setTextViewText(R.id.away_name, mWidgetItems.get(position).getAwayName());
        rv.setContentDescription(R.id.away_name, mWidgetItems.get(position).getAwayName());
        rv.setTextViewText(R.id.score_textview, Utilies.getScores(res, mWidgetItems.get(position).getHomeGoal(), mWidgetItems.get(position).getAwayGoal(), false));
        rv.setContentDescription(R.id.score_textview, Utilies.getScores(res, mWidgetItems.get(position).getHomeGoal(), mWidgetItems.get(position).getAwayGoal(), true));
        rv.setTextViewText(R.id.data_textview, mWidgetItems.get(position).getTime());
        rv.setContentDescription(R.id.data_textview, mWidgetItems.get(position).getTime());
        rv.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(mWidgetItems.get(position).getHomeName()));
        rv.setContentDescription(R.id.home_crest, res.getString(R.string.image_crest_content_description, mWidgetItems.get(position).getHomeName()));
        rv.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(mWidgetItems.get(position).getAwayName()));
        rv.setContentDescription(R.id.away_crest, res.getString(R.string.image_crest_content_description, mWidgetItems.get(position).getAwayName()));

        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        extras.putInt(ScoreWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_linear_layout, fillInIntent);

        // Return the remote views object.
        return rv;
    }

    public RemoteViews getLoadingView() {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.

        setupWidgetData();
    }
}