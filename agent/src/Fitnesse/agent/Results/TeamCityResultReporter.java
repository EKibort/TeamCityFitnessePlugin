package Fitnesse.agent.Results;

import Fitnesse.agent.Results.FitnesseResult;
import Fitnesse.agent.Results.ResultReporter;
import jetbrains.buildServer.agent.BuildProgressLogger;

import java.util.Date;

public class TeamCityResultReporter implements ResultReporter {
    private final BuildProgressLogger Logger;

    public TeamCityResultReporter(BuildProgressLogger logger) {
        this.Logger = logger;
    }

    public void Report(FitnesseResult result) {
        String testName = result.getHistoryLink();
        if ((result.getRights() == 0) && (result.getWrongs() ==0) && (result.getExceptions() == 0)) {
            Logger.logTestIgnored(testName, "empty test");
        }
        else {
            Logger.logTestStarted(testName, new Date(System.currentTimeMillis()-result.getDurationMs()));

            if ((result.getWrongs() >0) || (result.getExceptions() > 0)) {

                String errorMessage =String.format("wrong:%d  exception:%d", result.getWrongs(), result.getExceptions());
                Logger.logTestFailed(testName, errorMessage, "");
            }

            Logger.logTestFinished(testName,  new Date());
        }
    }

    public void Exception(Throwable ex) {
        Logger.exception(ex);
    }

}
