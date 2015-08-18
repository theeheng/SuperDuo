package barqsoft.footballscores;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class DatabaseContract
{
    public static final String SCORES_TABLE = "scores_table";
    public static final String LEAGUES_TABLE = "leagues_table";

    public static final class scores_table implements BaseColumns
    {
        //Table data
        public static final String LEAGUE_COL = "league";
        public static final String DATE_COL = "date";
        public static final String TIME_COL = "time";
        public static final String HOME_COL = "home";
        public static final String AWAY_COL = "away";
        public static final String HOME_GOALS_COL = "home_goals";
        public static final String AWAY_GOALS_COL = "away_goals";
        public static final String MATCH_ID = "match_id";
        public static final String MATCH_DAY = "match_day";

        //public static Uri SCORES_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH)
                //.build();

        //Types
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCORE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCORE;

        public static Uri buildScoreWithLeague()
        {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCORE).appendPath("league").build();
        }
        public static Uri buildScoreWithId()
        {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCORE).appendPath("id").build();
        }
        public static Uri buildScoreWithDate()
        {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCORE).appendPath("date").build();
        }
    }

    public static final class leagues_table implements BaseColumns {

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LEAGUE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LEAGUE;

        //Table data
        public static final String CAPTION_COL = "caption";
        public static final String LEAGUE_COL = "league";
        public static final String YEAR_COL = "year";

        public static Uri buildLeagueWithId()
        {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_LEAGUE).appendPath("id").build();
        }

    }
    //URI data
    public static final String CONTENT_AUTHORITY = "barqsoft.footballscores";
    public static final String PATH_SCORE = "scores";
    public static final String PATH_LEAGUE = "leagues";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
}
