package Fitnesse.agent.Results;

import Fitnesse.agent.Results.FitnesseResult;

public interface ResultReporter {
    public void Report(FitnesseResult result);
    public void Exception(Throwable ex);
}
