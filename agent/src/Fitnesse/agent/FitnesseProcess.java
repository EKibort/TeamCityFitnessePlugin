package Fitnesse.agent;

import Fitnesse.agent.Results.ResultsProcessorFactory;
import Fitnesse.agent.Results.ResultsStreamProcessor;
import Fitnesse.common.Util;
import jetbrains.buildServer.agent.*;
import org.jetbrains.annotations.NotNull;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FitnesseProcess extends  FutureBasedBuildProcess {

    private final static String LOCAL_URL = "http://localhost";

    @NotNull
    private final BuildRunnerContext Context;
    @NotNull
    private final BuildProgressLogger Logger;


    public FitnesseProcess (@NotNull final AgentRunningBuild build, @NotNull final BuildRunnerContext context){
        Context = context;
        Logger = build.getBuildLogger();
    }

    private ResultsStreamProcessor getResultsProcessor(String suiteName, FlowLogger logger){
        return ResultsProcessorFactory.getProcessor(logger, Context.getBuild().getBuildTempDirectory(), suiteName);
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
            Logger.progressMessage(String.format("Running fitnesse use cmd '%s' in '%s'",  Util.join(Arrays.asList(cmdFitnesse)), rootFolder));
            return Runtime.getRuntime().exec(cmdFitnesse, null, new File(rootFolder));
        }
        catch (IOException e) {
            Logger.exception(e);
        }
        return null;
    }


    public void  getSuiteResults(String relUrl, FlowLogger logger) throws MalformedURLException {
        URL pageCmdTarget = getTestAbsoluteUrl(relUrl);
        InputStream  inputStream =null;
        String suiteName = String.format("Fitnesse %s", relUrl);
        try {
            logger.progressMessage(String.format("Connecting to '%s'", pageCmdTarget));
            HttpURLConnection connection = (HttpURLConnection) pageCmdTarget.openConnection();
            logger.progressMessage(String.format("Connected: '%d/%s'", connection.getResponseCode(), connection.getResponseMessage()));

            inputStream = connection.getInputStream();

            ResultsStreamProcessor resultsProcessor = getResultsProcessor(suiteName, logger);
            resultsProcessor.ProcessStream(inputStream );
        }
        catch (Exception ex) {
            logger.exception(ex);
        }
        finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                }
                catch (Exception e){
                    logger.exception(e);
                }
            }
        }
    }

    public int ping(URL url, int timeout) {

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            Logger.progressMessage(String.format("\t response from (%s): %d", url.toString(), responseCode));
            return responseCode;
        } catch (IOException exception) {
            Logger.progressMessage(String.format("\t response from (%s): %s", url.toString(), exception.getMessage()));
            return -1;
        }
    }

    private int GetServerResponseCode() throws MalformedURLException {
        int code =  ping(getFitnesseRootUrl(), 500);
        return code;
    }

    private boolean waitWhileUnpackingByCode() throws MalformedURLException, InterruptedException {
        long timeout = System.currentTimeMillis() + 3*60*1000;
        Logger.progressMessage("Fitnesse starting...");
        while (GetServerResponseCode() != 200 && System.currentTimeMillis() < timeout)
        {
            Thread.sleep(1000);
        }
        return GetServerResponseCode() == 200 ;
    }

    private int getPort() {
        return Integer.parseInt(getParameter(Util.PROPERTY_FITNESSE_PORT));
    }

    private Collection<String> getTestRelativeUrls() {
        Collection<String> testsRelUrls = new ArrayList<String>();

        for(String relUrl :  getParameter(Util.PROPERTY_FITNESSE_TEST, "").split(";")) {
            String trimmedUrl = relUrl.trim();
            if (!trimmedUrl.isEmpty() && trimmedUrl.indexOf('?') > 0) {
                testsRelUrls.add(trimmedUrl);
            }
        }
        return testsRelUrls;
    }

    private boolean getRunInParallelParameterValue(){
        String parameterValue = getParameter(Util.PROPERTY_FITNESSE_TEST_RUN_PARALLEL);
        return Boolean.parseBoolean(parameterValue);
    }

    private URL getFitnesseRootUrl()throws MalformedURLException {
        return new URL(String.format("%s:%d/",LOCAL_URL, getPort()));
    }

    private URL getTestAbsoluteUrl(String relUrl) throws MalformedURLException {

        return new URL(String.format("%s%s",getFitnesseRootUrl(), relUrl));
    }

    private void runSuites(Collection<String> relTestUrls, Boolean runInParallel) throws Exception {
        if(runInParallel)
            runSuitesAsync(relTestUrls);
        else
            runSuitesSync(relTestUrls);
    }

    private void runSuitesSync(Collection<String> relTestUrls) throws Exception {
        for (String relTestUrl : relTestUrls) {
            getSuiteResults(relTestUrl,Logger.getFlowLogger(relTestUrl));
        }
    }

    private void runSuitesAsync(Collection<String> relTestUrls) throws Exception {
        List<Thread> allThreads = new ArrayList<Thread>();

        for (final String relTestUrl : relTestUrls) {
            Thread thread = new Thread() {
                public void run() {
                    FlowLogger logger = Logger.getFlowLogger(relTestUrl);
                    try {
                        logger.progressMessage("Starting " + relTestUrl);
                        getSuiteResults(relTestUrl,logger);
                    } catch (Exception ex) {
                        logger.progressMessage(ex.toString());
                        logger.exception(ex);
                    }
                }
            };
            thread.start();
            allThreads.add(thread);
        }

        for (Thread thread : allThreads) {
            thread.join();
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
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

                Boolean runInParallel = getRunInParallelParameterValue();
                if(runInParallel) Logger.message("Tests will be run in parallel");

                Logger.progressMessage("Fitnesse ran");

                if (waitWhileUnpackingByCode()) {
                    runSuites(testsSuitesToRun, runInParallel);

                    Logger.progressMessage("terminating");
                }
                else {
                    Logger.error("Could not start fitnesse or interrupted");
                    return  isInterrupted()?BuildFinishedStatus.INTERRUPTED:BuildFinishedStatus.FINISHED_FAILED;
                }
            }
            finally {
                if (fitProcess != null)
                {
                    fitProcess.destroy();
                    Logger.message("STDOUT:"+convertStreamToString(fitProcess.getInputStream()));
                    Logger.message("STDERR:"+convertStreamToString(fitProcess.getErrorStream()));
                }
            }

            return BuildFinishedStatus.FINISHED_SUCCESS;
        }
        catch (Exception e){
            Logger.exception(e);
            return BuildFinishedStatus.FINISHED_FAILED;
        }
    }
}
