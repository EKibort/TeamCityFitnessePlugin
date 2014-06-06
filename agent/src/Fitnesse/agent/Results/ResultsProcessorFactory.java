package Fitnesse.agent.Results;

import jetbrains.buildServer.agent.BuildProgressLogger;

public class ResultsProcessorFactory {
    public static ResultsStreamProcessor getProcessor(BuildProgressLogger logger){
        return new XmlResultStreamProcessor(new TeamCityResultReporter(logger), logger);
    }
}
