package Fitnesse.agent;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.runner.*;
import org.jetbrains.annotations.NotNull;

public class FitnesseRunnerCommandLineBuildServiceFactory implements  CommandLineBuildServiceFactory{

    @NotNull
    public CommandLineBuildService createService() {
        return new FitnesseRunnerBuildServiceAdapter();
    }

    @NotNull
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return new FitnesseRunnerAgentBuildRunnerInfo();
    }
}
