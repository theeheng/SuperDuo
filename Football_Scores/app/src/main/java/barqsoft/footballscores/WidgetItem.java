package barqsoft.footballscores;

public class WidgetItem {

    private String mHomeName;
    private String mAwayName;
    private String mHomeGoal;
    private String mAwayGoal;
    private String mTime;

    public String getHomeName()
    {
        return mHomeName;
    }

    public String getAwayName()
    {
        return mAwayName;
    }

    public int getHomeGoal()
    {
        return Integer.parseInt(mHomeGoal);
    }

    public int getAwayGoal()
    {
        return Integer.parseInt(mAwayGoal);
    }

    public String getTime()
    {
        return mTime;
    }

    public WidgetItem(String homeName, String awayName, String homeGoal, String awayGoal, String time) {
        this.mHomeName = homeName;
        this.mAwayName = awayName;
        this.mHomeGoal = homeGoal;
        this.mAwayGoal = awayGoal;
        this.mTime = time;
    }
}
