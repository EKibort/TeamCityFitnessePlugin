package Fitnesse.agent;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import org.jetbrains.annotations.NotNull;

import Fitnesse.common.Util;

public class FitnesseRunner implements AgentBuildRunner, AgentBuildRunnerInfo {

    @NotNull
    public BuildProcess createBuildProcess(@NotNull final AgentRunningBuild runningBuild, @NotNull final BuildRunnerContext context) throws RunBuildException
    {
        return  new FitnesseProcess(runningBuild, context);
    }

    @NotNull
    public AgentBuildRunnerInfo getRunnerInfo()
    {
        return this;
    }

    @NotNull
    public String getType() {
        return Util.RUNNER_TYPE;
    }

    public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
        return true;
    }
}
