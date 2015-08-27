package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import barqsoft.footballscores.service.ScoreWidgetService;

public class ScoreWidgetProvider extends AppWidgetProvider {


    public static final String LOG_TAG = "ScoreWidgetProvider";
    public static final String SCORE_UPDATE_ACTION = "barqsoft.footballscores.ScoreWidget.SCORE_UPDATE_ACTION";
    public static final String CLICK_ACTION = "barqsoft.footballscores.ScoreWidget.CLICK_ACTION";
    public static final String EXTRA_ITEM = "barqsoft.footballscores.ScoreWidget.EXTRA_ITEM";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals(CLICK_ACTION)) {

            Intent mainActivityIntent = new Intent(context.getApplicationContext(), MainActivity.class);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);

        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; ++i) {

            Intent intent = new Intent(context, ScoreWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);

            rv.setEmptyView(R.id.stack_view, R.id.empty_view);

            Intent toastIntent = new Intent(context, ScoreWidgetProvider.class);
            toastIntent.setAction(ScoreWidgetProvider.CLICK_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.stack_view);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}