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
import java.net.ServerSocket;
import java.net.URL;
import java.util.*;

public class FitnesseProcess extends  FutureBasedBuildProcess {

    private final static String LOCAL_URL = "http://localhost";

    @NotNull
    private final BuildRunnerContext Context;
    @NotNull
    private final BuildProgressLogger Logger;
    @NotNull
    private final ResultsStreamProcessor ResultsProcessor;

    private int Port;


    public FitnesseProcess(@NotNull final AgentRunningBuild build, @NotNull final BuildRunnerContext context) {
        Context = context;
        Logger = build.getBuildLogger();
        ResultsProcessor = ResultsProcessorFactory.getProcessor(Logger);
        Port = -1;
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
        return new String[]{"java", "-jar", jarFitnesse.getAbsolutePath(), "-p", "" + getPort()};
    }

    private Process runFitnesseInstance() {
        try {
            String[] cmdFitnesse = getFitnesseCmd();
            String rootFolder = getFitnesseRoot();
            Logger.progressMessage(String.format("Running fitnesse use cmd '%s' in '%s'", Util.join(Arrays.asList(cmdFitnesse)), rootFolder));
            return Runtime.getRuntime().exec(cmdFitnesse, null, new File(rootFolder));
        } catch (IOException e) {
            Logger.exception(e);
        }
        return null;
    }


    public void getSuiteResults(String relUrl) throws MalformedURLException {
        URL pageCmdTarget = getTestAbsoluteUrl(relUrl);
        InputStream inputStream = null;
        String suiteName = String.format("Fitnesse %s", relUrl);
        try {
            Logger.progressMessage(String.format("Connecting to '%s'", pageCmdTarget));
            HttpURLConnection connection = (HttpURLConnection) pageCmdTarget.openConnection();
            Logger.progressMessage(String.format("Connected: '%d/%s'", connection.getResponseCode(), connection.getResponseMessage()));

            inputStream = connection.getInputStream();
            ResultsProcessor.ProcessStream(inputStream, pageCmdTarget);
        } catch (Exception ex) {
            Logger.exception(ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Logger.exception(e);
                }
            }
            Logger.logSuiteFinished(suiteName);
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
        int code = ping(getFitnesseRootUrl(), 500);
        return code;
    }

    private boolean waitWhileUnpackingByCode() throws MalformedURLException, InterruptedException {
        long timeout = System.currentTimeMillis() + 3 * 60 * 1000;
        Logger.progressMessage("Fitnesse starting...");
        while (GetServerResponseCode() != 200 && System.currentTimeMillis() < timeout) {
            Thread.sleep(1000);
        }
        return GetServerResponseCode() == 200;
    }

    private int getPort() {
        if (this.Port == -1) {
            this.Port = detectPort();
        }
        return this.Port;
    }

    private int detectPort() {
        String portText = getParameter(Util.PROPERTY_FITNESSE_PORT);
        if (portText.contains("-")) {
            // We have a range of ports
            String[] portsArr = portText.split("-");
            if (portsArr.length == 2) {
                int portFrom = Integer.parseInt(portsArr[0]);
                int portTo = Integer.parseInt(portsArr[1]);
                Random random = new Random();
                // Try and find an available port in the range given
                int port = random.nextInt(portTo - portFrom) + portFrom;
                while (!isPortAvailable(port)) {
                    port = random.nextInt(portTo - portFrom) + portFrom;
                }
                return port;
            }
        }
        return Integer.parseInt(portText);
    }

    private boolean isPortAvailable(int port) {
        try {
            ServerSocket socket = new ServerSocket(port);
            try {
                socket.close();
            } catch (IOException e) {
                // Ignore IOException on close()
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private Collection<String> getTestRelativeUrls() {
        Collection<String> testsRelUrls = new ArrayList<String>();

        for (String relUrl : getParameter(Util.PROPERTY_FITNESSE_TEST, "").split(";")) {
            String trimmedUrl = relUrl.trim();
            if (!trimmedUrl.isEmpty() && trimmedUrl.indexOf('?') > 0) {
                testsRelUrls.add(trimmedUrl);
            }
        }
        return testsRelUrls;
    }

    private URL getFitnesseRootUrl() throws MalformedURLException {
        return new URL(String.format("%s:%d/", LOCAL_URL, getPort()));
    }

    private URL getTestAbsoluteUrl(String relUrl) throws MalformedURLException {

        return new URL(String.format("%s%s&format=xml", getFitnesseRootUrl(), relUrl));
    }

    private void runSuites(Collection<String> relTestUrls) throws Exception {
        List<Thread> allThreads = new ArrayList<Thread>();
        Integer hostIndex = 0;
        List<String> hostnames = getHostNames(new File("c:\\hostnames.txt"));
        for (final String url : relTestUrls) {
            try {
                final String currentHostName = hostnames.get(hostIndex);
                Logger.progressMessage(url);
                Logger.progressMessage(currentHostName);
                hostIndex++;
                Thread thread = new Thread() {
                    public void run() {
                        for (String relTestUrl : url.split(",")) {
                            Logger.progressMessage(relTestUrl);
                            try {
                                String filePath = String.format("%s\\FitNesseRoot\\%s\\content.txt",
                                        getFitnesseRoot(),
                                        strJoin(relTestUrl.substring(0, relTestUrl.indexOf("?")).split("\\."), "\\"));


                                replaceWithHostName(filePath,currentHostName );

                                getSuiteResults(relTestUrl);
                            } catch (Exception ex) {
                                Logger.progressMessage(ex.toString());
                                Logger.exception(ex);
                            }
                        }
                    }
                };
                thread.start();
                allThreads.add(thread);
            } catch (Exception ex) {
                Logger.progressMessage(ex.toString());
                Logger.exception(ex);
            }
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
        Collection<String> testsSuitesToRun = getTestRelativeUrls();

        if (testsSuitesToRun.isEmpty()) {
            Logger.message("Nothing to run");
            return BuildFinishedStatus.FINISHED_SUCCESS;
        }

        try {
            //TODO detect fitnesse version
            //TODO add http timeout
            Process fitProcess = null;

            try {
                fitProcess = runFitnesseInstance();

                Logger.progressMessage("Fitnesse ran");
                if (waitWhileUnpackingByCode()) {
                    runSuites(testsSuitesToRun);

                    Logger.progressMessage("terminating");
                } else {
                    Logger.error("Could not start fitnesse or interrupted");
                    return isInterrupted() ? BuildFinishedStatus.INTERRUPTED : BuildFinishedStatus.FINISHED_FAILED;
                }
            } finally {
                if (fitProcess != null) {
                    fitProcess.destroy();
                    Logger.message("STDOUT:" + convertStreamToString(fitProcess.getInputStream()));
                    Logger.message("STDERR:" + convertStreamToString(fitProcess.getErrorStream()));
                }
            }

            return BuildFinishedStatus.FINISHED_SUCCESS;
        } catch (Exception e) {
            Logger.exception(e);
            return BuildFinishedStatus.FINISHED_FAILED;
        }
    }

    private List<String> getHostNames(File fin) throws IOException {

        List<String> hosts = new ArrayList<String>();

        FileInputStream fis = new FileInputStream(fin);

        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = null;
        while ((line = br.readLine()) != null) {
            hosts.add(line);
        }

        br.close();

        return hosts;
    }

    private void replaceWithHostName(String filePath, String hostName) throws IOException {
        File fin = new File(filePath);
        if(!fin.exists()) {
            fin = new File(fin.getAbsolutePath().replace("ItSearchZhuhaiSuite", "ItSearchDevSuite"));
        }

        Logger.progressMessage(fin.getAbsolutePath());

        List<String> lineCollection = new ArrayList<String>();
        FileInputStream fis = new FileInputStream(fin);

        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = null;
        lineCollection.add(String.format("!define CONSOLELAB {%s}", hostName));
        while ((line = br.readLine()) != null) {
            if (!line.contains("define CONSOLELAB"))
                lineCollection.add(line);
        }

        br.close();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fin));
            for (String newline : lineCollection) {
                writer.write(newline);
                writer.newLine();
            }
        } catch (Exception ex) {
            Logger.exception(ex);
        } finally {
            if ( writer != null ) writer.close();
        }
    }

    public String strJoin(String[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0; i < aArr.length; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }
}
