package Fitnesse.agent.Results;

import jetbrains.buildServer.agent.BuildProgressLogger;

import java.io.File;

public class ResultsProcessorFactory {
    public static ResultsStreamProcessor getProcessor(BuildProgressLogger logger, File tempDir, String suiteName){
        return new XmlResultStreamProcessor(new TeamCityResultReporter(logger, suiteName), logger, tempDir);
    }
}
