package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.service.ScoresWidgetViewService;
import barqsoft.footballscores.service.myFetchService;

/**
 * Implementation of App Widget functionality.
 */
public class ScoresWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {

//            Intent intent = new Intent(context, ScoresWidgetViewService.class);
//
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
//            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Construct the RemoteViews object
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.scores_widget);
//            views.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);
//            views.setTextViewText(R.id.appwidget_text, "");
//            views.setEmptyView(R.id.stack_view, R.id.empty_view);


            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.scores_widget);
            views.setOnClickPendingIntent(R.id.appwidget_home_team, pendingIntent);

            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String[] dates= {today};
        Cursor cursor = context.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null,
                DatabaseContract.scores_table.MATCH_DAY + "=?", dates, DatabaseContract.scores_table.DATE_COL + " desc");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.scores_widget);
        //views.setEmptyView(R.id.appwidget_text, R.id.empty_view);

        //Cursor scoresCursor = context.getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.TIME_COL));
            String home = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.HOME_COL));
            String away = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.AWAY_COL));
            String homeGoals = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.HOME_GOALS_COL));
            String awayGoals = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.AWAY_GOALS_COL));
            views.setTextViewText(R.id.appwidget_latest_match, "Latest match info, time: " + time);
            views.setTextViewText(R.id.appwidget_home_team, home);
            views.setTextViewText(R.id.appwidget_away_team, away);
            views.setTextViewText(R.id.appwidget_home_score, homeGoals);
            views.setTextViewText(R.id.appwidget_away_score, awayGoals);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Intent launchAppIntent = new Intent(context, MainActivity.class);
    }

}

