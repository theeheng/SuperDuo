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
    private static int counter = 0;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    private void setupWidgetData()
    {
        mScoreCount = 0;

        Date todayDate = new Date(System.currentTimeMillis());

        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

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

        counter ++;
    }

    public void onCreate() {
    }

    public void onDestroy() {
        mWidgetItems.clear();
    }

    public int getCount() {
        return mScoreCount;
    }

    public RemoteViews getViewAt(int position) {

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
        rv.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(mWidgetItems.get(position).getAwayName()));

        //Decorative Graphic Content Description to null
        rv.setContentDescription(R.id.home_crest, null);
        rv.setContentDescription(R.id.away_crest, null);

        Bundle extras = new Bundle();
        extras.putInt(ScoreWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_linear_layout, fillInIntent);

        // Return the remote views object.
        return rv;
    }

    public RemoteViews getLoadingView() {
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
        setupWidgetData();
    }
}