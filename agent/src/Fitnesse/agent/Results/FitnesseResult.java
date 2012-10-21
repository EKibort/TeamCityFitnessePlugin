package Fitnesse.agent.Results;

public class FitnesseResult {
    private int rightCount = 0;
    private int wrongCount = 0;
    private int ignoresCount = 0;
    private int exceptionsCount = 0;
    private int runTimeInMillis = 0;
    private String relativePageName = "";
    private String pageHistoryLink = "";

    public FitnesseResult(int right, int wrong, int ignore, int exception,
                int durationMs, String pageName, String historyLink) {

        this.rightCount = right;
        this.wrongCount = wrong;
        this.ignoresCount = ignore;
        this.exceptionsCount = exception;
        this.runTimeInMillis = durationMs;
        this.relativePageName = pageName;
        this.pageHistoryLink =  historyLink;
    }

    public int getRights(){
        return this.rightCount;
    }

    public int getWrongs(){
        return this.wrongCount;
    }

    public int getIgnores(){
        return this.ignoresCount;
    }

    public int getExceptions(){
        return this.exceptionsCount;
    }

    public int getDurationMs(){
        return this.runTimeInMillis;
    }

    public String getPageName(){
        return this.relativePageName;
    }

    public String getHistoryLink(){
        return this.pageHistoryLink;
    }
}
