package Fitnesse.agent.Results;

import Fitnesse.agent.Results.FitnesseResult;
import Fitnesse.agent.Results.ResultReporter;
import Fitnesse.agent.Results.ResultsStreamProcessor;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

public class XmlResultStreamProcessor implements ResultsStreamProcessor {
    private final ResultReporter reporter;

    public XmlResultStreamProcessor(ResultReporter reporter){
        this.reporter = reporter;
    }

    public void ProcessStream(InputStream stream) {
        try {
            XMLEventReader xmlReader  = XMLInputFactory.newInstance().createXMLEventReader(stream);

            int rightCount = 0;
            int wrongCount = 0;
            int ignoresCount = 0;
            int exceptionsCount = 0;
            int runTimeInMillis = 0;
            String relativePageName = "";
            String pageHistoryLink = "";

            while (xmlReader.hasNext()) {
                XMLEvent event = xmlReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    String elName = startElement.getName().getLocalPart();

                    if (elName.equalsIgnoreCase("result")) {
                        rightCount = 0;
                        wrongCount = 0;
                        ignoresCount = 0;
                        exceptionsCount = 0;
                        runTimeInMillis = 0;
                        relativePageName = "";
                        pageHistoryLink = "";
                    }
                    else if (elName.equalsIgnoreCase("right")) {
                        //TODO remove duplication
                        event = xmlReader.nextEvent();
                        String data = event.asCharacters().getData();
                        rightCount = Integer.parseInt(data );
                    }
                    else if (elName.equalsIgnoreCase("wrong")) {
                        event = xmlReader.nextEvent();
                        String data = event.asCharacters().getData();
                        wrongCount = Integer.parseInt(data );
                    }
                    else if (elName.equalsIgnoreCase("ignores")) {
                        event = xmlReader.nextEvent();
                        String data = event.asCharacters().getData();
                        ignoresCount = Integer.parseInt(data );
                    }
                    else if (elName.equalsIgnoreCase("exceptions")) {
                        event = xmlReader.nextEvent();
                        String data = event.asCharacters().getData();
                        exceptionsCount = Integer.parseInt(data );
                    }
                    else if (elName.equalsIgnoreCase("runTimeInMillis")) {
                        event = xmlReader.nextEvent();
                        String data = event.asCharacters().getData();
                        runTimeInMillis = Integer.parseInt(data );
                    }
                    else if (elName.equalsIgnoreCase("relativePageName")) {
                        event = xmlReader.nextEvent();
                        relativePageName= event.asCharacters().getData();
                    }
                    else if (elName.equalsIgnoreCase("pageHistoryLink")) {
                        event = xmlReader.nextEvent();
                        pageHistoryLink= event.asCharacters().getData();
                    }
                }
                else
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("result")) {

                        reporter.Report(new FitnesseResult(rightCount, wrongCount, ignoresCount, exceptionsCount,
                                runTimeInMillis, relativePageName, pageHistoryLink));
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
