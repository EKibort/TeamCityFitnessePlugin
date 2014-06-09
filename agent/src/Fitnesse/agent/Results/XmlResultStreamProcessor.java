package Fitnesse.agent.Results;

import Fitnesse.agent.Results.FitnesseResult;
import Fitnesse.agent.Results.ResultReporter;
import Fitnesse.agent.Results.ResultsStreamProcessor;
import jetbrains.buildServer.agent.BuildProgressLogger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlResultStreamProcessor implements ResultsStreamProcessor {

    private static final String[] RESULT_KEYS = new String[] { "right", "wrong", "ignores", "exceptions", "runTimeInMillis", "relativePageName", "pageHistoryLink" };
    private static final Set<String> RESULT_KEYS_SET = new HashSet<String>(Arrays.asList(RESULT_KEYS));

    private final ResultReporter reporter;
    private final BuildProgressLogger Logger;

    public XmlResultStreamProcessor(ResultReporter reporter, BuildProgressLogger logger){
        this.reporter = reporter;
        this.Logger = logger;
    }

    public void ProcessStream(InputStream stream) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            Pattern testNamePattern = Pattern.compile("<a href=\\\"(.*?)\\\"\\sid=\\\".*?\\\"\\sclass=\\\"test_name\\\">");
            Pattern testResultsPattern = Pattern.compile(">(\\d*)\\sright,\\s(\\d*)\\swrong,\\s(\\d*)\\signored,\\s(\\d*)\\sexceptions<");


            String line = null;
            String testName = null;
            while ((line = in.readLine()) != null) {
                Logger.message("Read line: "+line);
                Matcher matcherName = testNamePattern.matcher(line);
                if (matcherName.matches())
                {
                    testName = matcherName.group(1);
                }

                Matcher matcherResults = testResultsPattern.matcher(line);
                if (matcherResults.matches())
                {
                    Map<String, String> resultsMap = new HashMap<String, String>();
                    resultsMap.put("right",matcherResults.group(1));
                    resultsMap.put("wrong",matcherResults.group(2));
                    resultsMap.put("ignores",matcherResults.group(3));
                    resultsMap.put("exceptions",matcherResults.group(4));
                    resultsMap.put("relativePageName",testName);

                    resultsMap.put("pageHistoryLink","");
                    resultsMap.put("runTimeInMillis","0");

                    reporter.Report(new FitnesseResult(resultsMap));
                }
            }
            Logger.message("Results finished");
        }
        catch (Exception ex)
        {
            reporter.Exception(ex);
        }
    }

    /*public void ProcessStream(InputStream stream) {
        try {
            XMLEventReader xmlReader  = XMLInputFactory.newInstance().createXMLEventReader(stream);

            Map<String, String> resultsMap = new HashMap<String, String>();

            while (xmlReader.hasNext()) {
                XMLEvent event = xmlReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    String elName = startElement.getName().getLocalPart();

                    if (RESULT_KEYS_SET.contains(elName))
                    {
                        event = xmlReader.nextEvent();
                        resultsMap.put(elName, event.asCharacters().getData());
                    }
                }
                else
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("result")) {

                        reporter.Report(new FitnesseResult(resultsMap));
                        resultsMap.clear();
                    }
                }
            }
            xmlReader.close();
        }
        catch (Exception ex)
        {
            reporter.Exception(ex);
        }
    } */
}
