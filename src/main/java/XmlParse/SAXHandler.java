package XmlParse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class SAXHandler extends DefaultHandler {
    private static final String elementType = "string";
    private ArrayList<ParseResultEntry> resultEntries;
    private ParseResultEntry entry;
    private static boolean tmp = false;

    @Override
    public void startDocument() throws SAXException {
        resultEntries = new ArrayList<>();
        System.out.println("开始解析xml");
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        for (ParseResultEntry entry : resultEntries) {
            System.out.println(entry.toString());
        }
        System.out.println("xml解析完成");
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals(elementType)) {
            entry = new ParseResultEntry();
            if (attributes.getValue(0).endsWith("_url")) {
                tmp = true;
                entry.setKey(attributes.getValue(0));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (entry.getKey() != null) {
            resultEntries.add(entry);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String content = new String(ch, start, length);
        if (tmp) {
            entry.setValue(content);
        }
        tmp = false;
    }
}
