package Fitnesse.agent.Results;

import jetbrains.buildServer.agent.BuildProgressLogger;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlResultStreamProcessor implements ResultsStreamProcessor {

    private static final String[] RESULT_KEYS = new String[] { "right", "wrong", "ignores", "exceptions", "runTimeInMillis", "relativePageName", "pageHistoryLink" };

    private final ResultReporter reporter;
    private final BuildProgressLogger Logger;
    private final File TempDir;

    public XmlResultStreamProcessor(ResultReporter reporter, BuildProgressLogger logger, File tempDir){
        this.reporter = reporter;
        this.Logger = logger;
        this.TempDir = tempDir;
    }

    public void ProcessStream(InputStream stream) {
        BufferedWriter reportWriter = null;
        try
        {
            reporter.StartRunning();

            File reportFile = File.createTempFile("FitTest_Report",".html", TempDir);
            try
            {
                reportWriter = new BufferedWriter(new FileWriter(reportFile));
                Logger.message("Writing results to temp file: "+reportFile.getAbsolutePath());

                BufferedReader in = new BufferedReader(new InputStreamReader(stream));
                Pattern testNamePattern = Pattern.compile("<a href=\\\"(.*?)\\\"\\sid=\\\".*?\\\"\\sclass=\\\"test_name\\\">");
                Pattern testResultsPattern = Pattern.compile(">(\\d*)\\sright,\\s(\\d*)\\swrong,\\s(\\d*)\\signored,\\s(\\d*)\\sexceptions<");

                String line = null;
                String testName = null;
                while ((line = in.readLine()) != null) {
                    reportWriter.write(line);
                    reportWriter.newLine();

                    Matcher matcherName = testNamePattern.matcher(line);
                    if (matcherName.find())
                    {
                        testName = matcherName.group(1);
                        reporter.Start(testName);
                    }

                    Matcher matcherResults = testResultsPattern.matcher(line);
                    if (matcherResults.find())
                    {
                        Map<String, String> resultsMap = new HashMap<String, String>();
                        resultsMap.put("right",matcherResults.group(1));
                        resultsMap.put("wrong",matcherResults.group(2));
                        resultsMap.put("ignores",matcherResults.group(3));
                        resultsMap.put("exceptions",matcherResults.group(4));
                        resultsMap.put("relativePageName",testName);
                        resultsMap.put("pageHistoryLink",testName);
                        //TODO Support duration reporting from FitnesseResults

                        reporter.Finish(new FitnesseResult(resultsMap));
                    }
                }
                Logger.message("Results finished");
            }
            catch (Exception ex)
            {
                reporter.Exception(ex);
            }
            finally {
                if (reportWriter!= null)
                    try {
                        reportWriter.close();
                    } catch (IOException e) {
                        reporter.Exception(e);
                    }

            }
            //TODO Rename during publishing artifacts to suitename
            Logger.message(String.format("##teamcity[publishArtifacts '%s']",reportFile.getAbsolutePath()));
            reporter.FinishRunning();
        }
        catch (Exception ex)
        {
           reporter.Exception(ex);
        }
    }
}
