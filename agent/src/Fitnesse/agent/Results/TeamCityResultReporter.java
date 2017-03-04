package Fitnesse.agent.Results;

import Fitnesse.agent.Results.FitnesseResult;
import Fitnesse.agent.Results.ResultReporter;
import jetbrains.buildServer.agent.BuildProgressLogger;

import java.util.Date;

public class TeamCityResultReporter implements ResultReporter {
    private final BuildProgressLogger Logger;
    private final String SuiteName;

    public TeamCityResultReporter(BuildProgressLogger logger, String suiteName) {
        this.Logger = logger;
        this.SuiteName = suiteName;
    }

    public void StartRunning()
    {
        Logger.logSuiteStarted(SuiteName);
    }

    public void FinishRunning() { Logger.logSuiteFinished(SuiteName); }

    public void Start(String testName) { Logger.logTestStarted(testName); }

    public void Finish(FitnesseResult result) {
        String testName = result.getHistoryLink();
        if ((result.getRights() == 0) && (result.getWrongs() ==0) && (result.getExceptions() == 0)) {
            Logger.logTestIgnored(testName, "empty test");
        }
        else {
            if ((result.getWrongs() >0) || (result.getExceptions() > 0)) {
                Logger.logTestFailed(testName, String.format("wrong:%d  exception:%d", result.getWrongs(), result.getExceptions()), "" );
            }

            Logger.logTestFinished(testName);
        }
    }


    public void Report(FitnesseResult result) {
        String testName = result.getHistoryLink();
        if ((result.getRights() == 0) && (result.getWrongs() ==0) && (result.getExceptions() == 0)) {
            Logger.logTestIgnored(testName, "empty test");
        }
        else {
            Logger.logTestStarted(testName, new Date(System.currentTimeMillis()-result.getDurationMs()));

            if ((result.getWrongs() >0) || (result.getExceptions() > 0)) {
                Logger.logTestFailed(testName, String.format("wrong:%d  exception:%d", result.getWrongs(), result.getExceptions()), "" );
            }

            Logger.logTestFinished(testName,  new Date());
        }
    }

    public void Exception(Throwable ex) {
        Logger.exception(ex);
    }

}
