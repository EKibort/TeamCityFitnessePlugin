package Fitnesse.agent;

import jetbrains.buildServer.agent.BuildProgressLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//thanks! - http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
public class StreamLogger extends Thread
{
    InputStream inputStream;
    String type;
    private BuildProgressLogger logger;

    StreamLogger(InputStream inputStream, String type, BuildProgressLogger logger)
    {
        this.inputStream = inputStream;
        this.type = type;
        this.logger = logger;
    }

    public void run()
    {
        try
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ( (line = bufferedReader.readLine()) != null)
            {
                logger.progressMessage(type + ": " + line);
            }
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
}
