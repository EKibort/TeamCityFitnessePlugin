package Fitnesse.agent;

import com.intellij.execution.util.EnvironmentVariable;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import org.jetbrains.annotations.NotNull;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
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
        final String value = Context.getRunnerParameters().get(parameterName);
        if (value == null || value.trim().length() == 0) return null;
        String result = value.trim();
        return result;
    }

    private String getFitnesseRoot()
    {
        File jarFitnesse = new File(getParameter("fitnesseJarPath"));
        return jarFitnesse.getParent();
    }



    private String getFitnesseCmd()
    {
        File jarFitnesse = new File(getParameter("fitnesseJarPath"));
        //File rootFitnesse = new File(jarFitnesse.getParent());

        /*String[] result =
                {"java",
                 "-jar",jarFitnesse.getAbsolutePath(),
                 //"-d", rootFitnesse.getAbsolutePath() , //TODO Adbs path
                 //"-r", "FitNesseRoot",
                 "-p", getParameter("fitnessePort"),
        };*/

        String  result = String.format("java -jar %s  -p %s",jarFitnesse.getAbsolutePath(), getParameter("fitnessePort"));

        return result;
    }

    private Process runFitnesseInstance()
    {
        try
        {
            String cmdFitnesse = getFitnesseCmd();
            String rootFolder = getFitnesseRoot();
            Logger.progressMessage(String.format("Running fitnesse use cmd '%s' in '%s'", cmdFitnesse, rootFolder));

            return Runtime.getRuntime().exec(cmdFitnesse, null, new File(rootFolder));
        }
        catch (IOException e) {
            Logger.exception(e);
        }
        return null;
    }


    public boolean  getSuiteResults(URL pageCmdTarget)
    {
        InputStream  inputStream =null;
        try
        {
            Logger.progressMessage("Connnecting to " + pageCmdTarget);
            HttpURLConnection connection = (HttpURLConnection) pageCmdTarget.openConnection();
            Logger.progressMessage("Connected: " + connection.getResponseCode() + "/" + connection.getResponseMessage());

            inputStream = connection.getInputStream();
            inputStream.reset();

            XMLEventReader xmlReader  = XMLInputFactory.newInstance().createXMLEventReader(inputStream);

            Integer rightCount = 0;
            Integer wrongCount = 0;
            Integer ignoresCount = 0;
            Integer exceptionsCount = 0;
            Integer runTimeInMillis = 0;
            String relativePageName = "";
            String pageHistoryLink = "";

            Logger.logSuiteStarted("FitNesse");

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

                        Logger.progressMessage("found  "+pageHistoryLink);
                    }
                }
                else
                if (event.isEndElement())
                {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("result"))
                    {
                        //int questMarkIndex = pageHistoryLink.indexOf('?');
                        //if (questMarkIndex > 0)
                        //{
                            //String testName = pageHistoryLink.substring(0,questMarkIndex);
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
                        //}
                }
                }
            }
            xmlReader.close();
        }
        catch (Exception e)
        {
            Logger.exception(e);
        } finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (Exception e)
                {
                }
            }
            Logger.logSuiteFinished("FitNesse");
        }
        return true;

    }

    private void waitWhileUnpacking(Process fitProcess) throws  Exception
    {
        BufferedReader is = new BufferedReader(new InputStreamReader(fitProcess.getInputStream()));

        int timeout = 60;
        int count = 0;
        String line = "";
        do{
            line = is.readLine();
            if (line != null)
                Logger.progressMessage("Fitnesse out:"+line);
            else
            {
                Thread.sleep(1000);
                count++;
            }

        }while (!line.contains("page version expiration set to") && count<timeout);
    }

    @NotNull
    public BuildFinishedStatus call() throws Exception
    {
        try
        {
            Process fitProcess = runFitnesseInstance();
            Logger.progressMessage("Fitnesse runned");
            waitWhileUnpacking(fitProcess);

            getSuiteResults(new URL("http://localhost:"+getParameter("fitnessePort")+"/"+getParameter("fitnesseTest")+"&format=xml"));

            Logger.progressMessage("terminating");

            fitProcess.destroy();

            return BuildFinishedStatus.FINISHED_SUCCESS;
        }
        catch (Exception e)
        {
            return BuildFinishedStatus.FINISHED_FAILED;
        }
    }
}
