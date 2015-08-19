package barqsoft.footballscores;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies
{
    public static int CHAMPIONS_LEAGUE;
    public static int TESTING_LEAGUE = 357;
    public static String getLeague(int league_num, Context mContext)
    {
        if(league_num == TESTING_LEAGUE)
            return "TESTING LEAGUE";

        // Queries the user dictionary and returns results
        Cursor mCursor = mContext.getContentResolver().query(
                DatabaseContract.leagues_table.buildLeagueWithId(),   // The content URI of the words table
                null,                        // The columns to return for each row
                null,                    // Selection criteria
                new String[]{Integer.toString(league_num)},                     // Selection criteria
                null);

        if(mCursor != null) {
            mCursor.moveToFirst();
            String caption = mCursor.getString(1);
            return caption;
        }

        return "";
    }
    public static String getMatchDay(int match_day,int league_num)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return "Group Stages, Matchday : 6";
            }
            else if(match_day == 7 || match_day == 8)
            {
                return "First Knockout round";
            }
            else if(match_day == 9 || match_day == 10)
            {
                return "QuarterFinal";
            }
            else if(match_day == 11 || match_day == 12)
            {
                return "SemiFinal";
            }
            else
            {
                return "Final";
            }
        }
        else
        {
            return "Matchday : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        {
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            default: return R.drawable.no_icon;
        }
    }
}
