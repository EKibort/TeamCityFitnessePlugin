package Fitnesse.agent.Results;
import jetbrains.buildServer.agent.BuildProgressLogger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.*;

public class XmlResultStreamProcessor implements ResultsStreamProcessor {

    private static final String[] RESULT_KEYS = new String[] { "right", "wrong", "ignores", "exceptions", "runTimeInMillis", "relativePageName", "pageHistoryLink", "content" };
    private static final Set<String> RESULT_KEYS_SET = new HashSet<String>(Arrays.asList(RESULT_KEYS));

    private final ResultReporter reporter;
    private BuildProgressLogger logger;

    public XmlResultStreamProcessor(ResultReporter reporter, BuildProgressLogger logger){
        this.reporter = reporter;
        this.logger = logger;
    }

    public void ProcessStream(InputStream stream) {
        try {
            XMLInputFactory xMLInputFactory = XMLInputFactory.newInstance();
            xMLInputFactory.setProperty("javax.xml.stream.isCoalescing", true);

            XMLEventReader xmlReader  = xMLInputFactory.createXMLEventReader(stream);

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
    }
}
