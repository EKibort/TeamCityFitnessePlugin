package Fitnesse.agent;

import Fitnesse.agent.Results.ResultsProcessorFactory;
import Fitnesse.agent.Results.ResultsStreamProcessor;
import Fitnesse.common.Util;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import org.jetbrains.annotations.NotNull;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class FitnesseProcess extends  FutureBasedBuildProcess {

    private final static String LOCAL_URL = "http://localhost";

    @NotNull
    private final BuildRunnerContext Context;
    @NotNull
    private final BuildProgressLogger Logger;
    @NotNull
    private  final ResultsStreamProcessor ResultsProcessor;



    public FitnesseProcess (@NotNull final AgentRunningBuild build, @NotNull final BuildRunnerContext context){
        Context = context;
        Logger = build.getBuildLogger();
        ResultsProcessor = ResultsProcessorFactory.getProcessor(Logger);
    }

    private String getParameter(@NotNull final String parameterName) {
        return getParameter(parameterName, null);
    }

    private String getParameter(@NotNull final String parameterName, String defaultValue) {
        final String value = Context.getRunnerParameters().get(parameterName);
        if (value == null || value.trim().length() == 0)
            return defaultValue;
        return value.trim();
    }

    private String getFitnesseRoot() {
        File jarFitnesse = new File(getParameter("fitnesseJarPath"));
        return jarFitnesse.getParent();
    }

    private String[] getFitnesseCmd() {
        File jarFitnesse = new File(getParameter("fitnesseJarPath"));
        return new String[] {"java", "-jar", jarFitnesse.getAbsolutePath(), "-p", ""+getPort()};
    }

    private Process runFitnesseInstance() {
        try {
            String[] cmdFitnesse = getFitnesseCmd();
            String rootFolder = getFitnesseRoot();
            Logger.progressMessage(String.format("Running fitnesse use cmd '%s' in '%s'",  Util.join(Arrays.asList(cmdFitnesse), " "), rootFolder));
            return Runtime.getRuntime().exec(cmdFitnesse, null, new File(rootFolder));
        }
        catch (IOException e) {
            Logger.exception(e);
        }
        return null;
    }


    public boolean  getSuiteResults(String relUrl) throws MalformedURLException {
        URL pageCmdTarget = getTestAbsoluteUrl(relUrl);
        InputStream  inputStream =null;
        String suiteName = "Fitnesse "+relUrl;
        try {
            Logger.progressMessage("Connecting to " + pageCmdTarget);
            HttpURLConnection connection = (HttpURLConnection) pageCmdTarget.openConnection();
            Logger.progressMessage("Connected: " + connection.getResponseCode() + "/" + connection.getResponseMessage());

            inputStream = connection.getInputStream();
            ResultsProcessor.ProcessStream(inputStream );
        }
        catch (Exception ex) {
            Logger.exception(ex);
        }
        finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                }
                catch (Exception e){
                    Logger.exception(e);
                }
            }
            Logger.logSuiteFinished(suiteName);
        }
        return true;
    }

    private boolean isLatestLineOfHeader(String lineHeader)
    {
        return lineHeader != null && (
                lineHeader.contains("page version expiration set to") ||
               lineHeader.contains("page theme:"));
    }

    private boolean waitWhileUnpacking(Process fitProcess) throws  Exception {
        BufferedReader is = new BufferedReader(new InputStreamReader(fitProcess.getInputStream()));
        int timeout = 60;
        int count = 0;
        String line;
        do {
            line = is.readLine();
            if (line != null)
                Logger.progressMessage("\t"+line);
            else {
                Thread.sleep(1000);
                count++;
            }

        } while (!isLatestLineOfHeader(line) && count<timeout && !isInterrupted());
        return isLatestLineOfHeader(line);
    }

    private int getPort() {
        return Integer.parseInt(getParameter(Util.PROPERTY_FITNESSE_PORT));
    }

    private Collection<String> getTestRelativeUrls() {
        Collection<String> testsRelUrls = new ArrayList<String>();

        for(String relUrl :  getParameter(Util.PROPERTY_FITNESSE_TEST, "").split(";")) {
            String trimmedUrl = relUrl.trim();
            if (!trimmedUrl.isEmpty() && trimmedUrl.indexOf('?') > 0)
                testsRelUrls.add(trimmedUrl);
        }
        return testsRelUrls;
    }

    private URL getTestAbsoluteUrl(String relUrl) throws MalformedURLException {
        return new URL(String.format("%s:%d/%s&format=xml",LOCAL_URL, getPort(), relUrl));
    }


    private void runSuites(Collection<String> relTestUrls) throws Exception {
        //TODO Support running multiple tests in parallel
        for (String relTestUrl : relTestUrls) {
            getSuiteResults(relTestUrl);
        }
    }

    @NotNull
    public BuildFinishedStatus call() throws Exception {
        Collection<String> testsSuitesToRun =  getTestRelativeUrls();

        if (testsSuitesToRun.isEmpty()) {
            Logger.message("Nothing to run");
            return BuildFinishedStatus.FINISHED_SUCCESS;
        }

        try {
            //TODO Support detecting free port in range
            //TODO detect fitnesse version
            //TODO add http timeout
            Process fitProcess = null;

            try {
                fitProcess = runFitnesseInstance();

                Logger.progressMessage("Fitnesse ran "+fitProcess.toString());
                if (waitWhileUnpacking(fitProcess)) {
                    runSuites(testsSuitesToRun);

                    Logger.progressMessage("terminating");
                }
                else {
                    Logger.error("Could not start fitnesse or interrupted");
                    return  isInterrupted()?BuildFinishedStatus.INTERRUPTED:BuildFinishedStatus.FINISHED_FAILED;
                }
            }
            finally {
                if (fitProcess != null)
                    fitProcess.destroy();
            }

            return BuildFinishedStatus.FINISHED_SUCCESS;
        }
        catch (Exception e){
            Logger.exception(e);
            return BuildFinishedStatus.FINISHED_FAILED;
        }
    }
}
