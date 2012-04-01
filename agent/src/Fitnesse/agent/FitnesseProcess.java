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
            Logger.progressMessage(String.format("Running fitnesse use cmd '%s' in '%s'",cmdFitnesse, rootFolder));

            return Runtime.getRuntime().exec(cmdFitnesse, null, new File(rootFolder));
        }
        catch (IOException e) {
            Logger.exception(e);
        }
        return null;
    }


    public byte[] getHttpBytes(URL pageCmdTarget) {
        InputStream inputStream = null;
        ByteArrayOutputStream bucket = new ByteArrayOutputStream();

        try {
            Logger.progressMessage("Connnecting to " + pageCmdTarget);
            HttpURLConnection connection = (HttpURLConnection) pageCmdTarget.openConnection();
            Logger.progressMessage("Connected: " + connection.getResponseCode() + "/" + connection.getResponseMessage());

            inputStream = connection.getInputStream();
            long recvd = 0, lastLogged = 0;
            byte[] buf = new byte[4096];
            int lastRead;
            while ((lastRead = inputStream.read(buf)) > 0) {
                bucket.write(buf, 0, lastRead);
                //timeout.reset();
                recvd += lastRead;
                if (recvd - lastLogged > 1024) {
                    Logger.progressMessage(recvd / 1024 + "k...");
                    lastLogged = recvd;
                }
            }
        } catch (IOException e) {
            // this may be a "premature EOF" caused by e.g. incorrect content-length HTTP header
            // so it may be non-fatal -- try to recover
            Logger.exception(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    // swallow
                }
            }
        }
        return bucket.toByteArray();
    }

    private void writeFitnesseResults(File resultsFilePath, byte[] results) {

        try{
            FileOutputStream fos = new FileOutputStream(resultsFilePath);
            fos.write(results);
            fos.close();
        }
        catch (Exception e)
        {
            Logger.exception(e);
        }
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
            //Runtime.getRuntime().exec("");

            Process fitProcess = runFitnesseInstance();
            Logger.progressMessage("Fitnesse runned");
            waitWhileUnpacking(fitProcess);


            byte[] bytes = getHttpBytes(new URL("http://localhost:"+getParameter("fitnessePort")+"/"+getParameter("fitnesseTest")+"&format=xml"));
            writeFitnesseResults(new File(getParameter("fitnesseResult")), bytes);

            //Thread.sleep(5000);
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
