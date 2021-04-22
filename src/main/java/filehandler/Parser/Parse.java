package filehandler.Parser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:szf
 * desc:解析器接口
 * date:2021/2/23
 */
public interface Parse {

    void startElement(String uri, String localName, String qName, Attributes attributes, ParseResultEntry entry,
                      ArrayList<ParseResultEntry> resultEntries, Map.Entry<String, List<String>> conf_kv);

    void endElement(String uri, String localName, String qName, ParseResultEntry entry,
                    ArrayList<ParseResultEntry> resultEntries, Map.Entry<String, List<String>> conf_kv);

    void characters(char[] ch, int start, int length, ParseResultEntry entry,
                    ArrayList<ParseResultEntry> resultEntries, Map.Entry<String, List<String>> conf_kv);

    void writeSheet(ArrayList<ParseResultEntry> resultEntries, String fileName) throws IOException;

    boolean isFieldValid(List<String> searchField, String content);
}
