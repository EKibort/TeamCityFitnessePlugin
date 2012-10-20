package Fitnesse.agent;

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
import java.util.Date;
import javax.xml.stream.*;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


/**
 * Created with IntelliJ IDEA.
 * User: Advard
 * Date: 31.03.12
 * Time: 23:33
 * To change this template use File | Settings | File Templates.
 */
public class FitnesseProcess extends  FutureBasedBuildProcess {

    private final static String LOCAL_URL = "http://localhost";

    @NotNull
    private final AgentRunningBuild Build;
    @NotNull
    private final BuildRunnerContext Context;
    @NotNull
    private final BuildProgressLogger Logger;

    public FitnesseProcess (@NotNull final AgentRunningBuild build, @NotNull final BuildRunnerContext context)
    {
        Build = build;
        Context = context;
        Logger = build.getBuildLogger();
    }

    private String getParameter(@NotNull final String parameterName)
    {
        return getParameter(parameterName, null);
    }

    private String getParameter(@NotNull final String parameterName, String defaultValue)
    {
        final String value = Context.getRunnerParameters().get(parameterName);
        if (value == null || value.trim().length() == 0) return defaultValue;
        String result = value.trim();
        return result;
    }

    private String getFitnesseRoot()
    {
        File jarFitnesse = new File(getParameter("fitnesseJarPath"));
        return jarFitnesse.getParent();
    }

    private String[] getFitNesseCmd()
    {
        File jarFitnesse = new File(getParameter("fitnesseJarPath"));
        return new String[] {"java", "-jar", jarFitnesse.getAbsolutePath(), "-p", ""+getPort()};
    }

    private Process runFitnesseInstance()
    {
        try
        {
            String[] cmdFitnesse = getFitNesseCmd();
            String rootFolder = getFitnesseRoot();
            Logger.progressMessage(String.format("Running fitnesse use cmd '%s' in '%s'",  Util.join(Arrays.asList(cmdFitnesse), " "), rootFolder));

            return Runtime.getRuntime().exec(cmdFitnesse, null, new File(rootFolder));
        }
        catch (IOException e) {
            Logger.exception(e);
        }
        return null;
    }


    public boolean  getSuiteResults(String relUrl) throws MalformedURLException
    {
        URL pageCmdTarget = getTestAbsoluteUrl(relUrl);
        InputStream  inputStream =null;
        String suiteName = "FitNesse "+relUrl;
        try
        {
            Logger.progressMessage("Connnecting to " + pageCmdTarget);
            HttpURLConnection connection = (HttpURLConnection) pageCmdTarget.openConnection();
            Logger.progressMessage("Connected: " + connection.getResponseCode() + "/" + connection.getResponseMessage());

            inputStream = connection.getInputStream();

            XMLEventReader xmlReader  = XMLInputFactory.newInstance().createXMLEventReader(inputStream);

            //TODO Move to separated class
            Integer rightCount = 0;
            Integer wrongCount = 0;
            Integer ignoresCount = 0;
            Integer exceptionsCount = 0;
            Integer runTimeInMillis = 0;
            String relativePageName = "";
            String pageHistoryLink = "";

            Logger.logSuiteStarted(suiteName);

            while (xmlReader.hasNext())
            {
                XMLEvent event = xmlReader.nextEvent();
                if (event.isStartElement())
                {
                    StartElement startElement = event.asStartElement();
                    String elName = startElement.getName().getLocalPart();

                    if (elName.equalsIgnoreCase("result"))
                    {
                        rightCount = 0;
                        wrongCount = 0;
                        ignoresCount = 0;
                        exceptionsCount = 0;
                        runTimeInMillis = 0;
                        relativePageName = "";
                        pageHistoryLink = "";
                    }
                    else if (elName.equalsIgnoreCase("right"))
                    {
                        //TODO remove dupl
                        event = xmlReader.nextEvent();
                        String data = event.asCharacters().getData();
                        rightCount = Integer.parseInt(data );
                    }
                    else if (elName.equalsIgnoreCase("wrong"))
                    {
                        event = xmlReader.nextEvent();
                        String data = event.asCharacters().getData();
                        wrongCount = Integer.parseInt(data );
                    }
                    else if (elName.equalsIgnoreCase("ignores"))
                    {
                        event = xmlReader.nextEvent();
                        String data = event.asCharacters().getData();
                        ignoresCount = Integer.parseInt(data );
                    }
                    else if (elName.equalsIgnoreCase("exceptions"))
                    {
                        event = xmlReader.nextEvent();
                        String data = event.asCharacters().getData();
                        exceptionsCount = Integer.parseInt(data );
                    }
                    else if (elName.equalsIgnoreCase("runTimeInMillis"))
                    {
                        event = xmlReader.nextEvent();
                        String data = event.asCharacters().getData();
                        runTimeInMillis = Integer.parseInt(data );
                    }
                    else if (elName.equalsIgnoreCase("relativePageName"))
                    {
                        event = xmlReader.nextEvent();
                        relativePageName= event.asCharacters().getData();
                    }
                    else if (elName.equalsIgnoreCase("pageHistoryLink"))
                    {
                        event = xmlReader.nextEvent();
                        pageHistoryLink= event.asCharacters().getData();
                    }
                }
                else
                if (event.isEndElement())
                {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("result"))
                    {
                        String testName = pageHistoryLink;
                        if ((rightCount == 0) && (wrongCount ==0) && (exceptionsCount == 0))
                        {
                            Logger.logTestIgnored(testName, "empty test");
                        }
                        else
                        {
                            Logger.logTestStarted(testName, new Date(System.currentTimeMillis()-runTimeInMillis));

                            if ((wrongCount >0) || (exceptionsCount > 0))
                            {
                                Logger.logTestFailed(testName, String.format("wrong:%d  exception:%d", wrongCount, exceptionsCount), "" );
                            }

                            Logger.logTestFinished(testName,  new Date());
                        }
                }
                }
            }
            xmlReader.close();
        }
        catch (Exception e)
        {
            Logger.exception(e);
        } finally{
            if (inputStream != null){
                try {
                    inputStream.close();
                }
                catch (Exception e){
                }
            }
            Logger.logSuiteFinished(suiteName);
        }
        return true;

    }

    private boolean waitWhileUnpacking(Process fitProcess) throws  Exception
    {
        InputStream inStream = fitProcess.getInputStream();

        BufferedReader is = new BufferedReader(new InputStreamReader(fitProcess.getInputStream()));

        int timeout = 60;
        int count = 0;
        String line = "";
        do{
            line = is.readLine();
            if (line != null)
                Logger.progressMessage("\t"+line);
            else
            {
                Thread.sleep(1000);
                count++;
            }

        }while (!line.contains("page version expiration set to") && count<timeout && !isInterrupted());
        return line.contains("page version expiration set to");
    }

    private int getPort()
    {
        return Integer.parseInt(getParameter(Util.PROPERTY_FITNESSE_PORT));
    }

    private Collection<String> getTestRelativeUrls()
    {
        Collection<String> testsRelUrls = new ArrayList<String>();

        for(String relUrl :  getParameter(Util.PROPERTY_FITNESSE_TEST, "").split(";"))
        {
            String trimmedUrl = relUrl.trim();
            if (!trimmedUrl.isEmpty() && trimmedUrl.indexOf('?') > 0)
                testsRelUrls.add(trimmedUrl);
        }
        return testsRelUrls;
    }

    private URL getTestAbsoluteUrl(String relUrl) throws MalformedURLException
    {
        return new URL(String.format("%s:%d/%s&format=xml",LOCAL_URL, getPort(), relUrl));
    }

    @NotNull
    public BuildFinishedStatus call() throws Exception
    {
        Collection<String> testsToRun =  getTestRelativeUrls();

        if (testsToRun.isEmpty()) {
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

                Logger.progressMessage("Fitnesse runned "+fitProcess.toString());
                if (waitWhileUnpacking(fitProcess)) {
                    //TODO Support running multiple tests in parallel

                    for (String relUrl : testsToRun)
                    {
                        getSuiteResults(relUrl);
                    }

                    Logger.progressMessage("terminating");
                }
                else {
                    Logger.error("Could not start fitnesse or interupted");
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
