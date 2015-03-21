package Fitnesse.agent.Results;

import Fitnesse.agent.Results.FitnesseResult;

public interface ResultReporter {

    public void StartRunning();
    public void FinishRunning();

    public void Start(String testName);
    public void Finish(FitnesseResult result);

    public void Report(FitnesseResult result);
    public void Exception(Throwable ex);
}
