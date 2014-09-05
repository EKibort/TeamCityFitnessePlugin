package Fitnesse.agent.Results;

import java.io.InputStream;
import java.net.URL;

public interface ResultsStreamProcessor {
    public void ProcessStream(InputStream stream, URL url);
}
