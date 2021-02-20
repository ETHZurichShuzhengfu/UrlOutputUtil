package XmlParse;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class XmlParser {
    public static void parseXml(String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        File file = new File(xmlPath);
        System.out.println(file.exists());
        SAXHandler handler = new SAXHandler();
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();
        parser.parse(file, handler);
    }
}
