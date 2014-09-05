package Fitnesse.agent.Results;

import java.util.Map;

public class FitnesseResult {
    private int rightCount = 0;
    private int wrongCount = 0;
    private int ignoresCount = 0;
    private int exceptionsCount = 0;
    private int runTimeInMillis = 0;
    private String relativePageName = "";
    private String pageHistoryLink = "";
    private String suiteUrl = "";

    public static int parseWithDefault(String number, int defaultVal) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public FitnesseResult(Map<String, String> map)
    {
        this.rightCount = parseWithDefault(map.get("right"),0);
        this.wrongCount = parseWithDefault(map.get("wrong"),0);
        this.ignoresCount = parseWithDefault(map.get("ignores"),0);
        this.exceptionsCount = parseWithDefault(map.get("exceptions"),0);
        this.runTimeInMillis = parseWithDefault(map.get("runTimeInMillis"),0);
        this.relativePageName = map.get("relativePageName");
        this.pageHistoryLink  = map.get("pageHistoryLink");
        this.suiteUrl  = map.get("suiteUrl");
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
	if (this.pageHistoryLink == null || this.pageHistoryLink.trim().length() == 0)
		return this.suiteUrl;
        return this.pageHistoryLink;
    }
}
