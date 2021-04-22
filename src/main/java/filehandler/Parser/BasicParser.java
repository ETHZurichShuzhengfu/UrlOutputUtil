package filehandler.Parser;

import dict.Dict;
import poi.ExcelWriter;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:szf
 * desc:基本类型xml解析器
 * date:2021/2/24
 */
public class BasicParser extends Parser {

    private boolean flag = false;
    private final String BASIC_XML_SUFFIX = "s.xml";

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes, ParseResultEntry entry, ArrayList<ParseResultEntry> resultEntries, Map.Entry<String, List<String>> conf_kv) {
        String fileName = conf_kv.getKey();
        if (qName.equals(fileName.substring(0, fileName.length() - BASIC_XML_SUFFIX.length()))) {
            String paramNameValue = attributes.getValue(0);
            if (isFieldValid(conf_kv.getValue(), paramNameValue)) {
                flag = true;
                entry.setKey(paramNameValue);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName, ParseResultEntry entry, ArrayList<ParseResultEntry> resultEntries, Map.Entry<String, List<String>> conf_kv) {
        if (entry.getKey() != null)
            resultEntries.add(entry);
    }

    @Override
    public void characters(char[] ch, int start, int length, ParseResultEntry entry, ArrayList<ParseResultEntry> resultEntries, Map.Entry<String, List<String>> conf_kv) {
        String content = new String(ch, start, length);
        if (flag) {
            entry.setValue(content);
        }
        flag = false;
    }

    @Override
    public void writeSheet(ArrayList<ParseResultEntry> resultEntries, String fileName) throws IOException {
        ExcelWriter.writeExcelWithXmlTypeNormal(fileName, resultEntries);
    }

    @Override
    public boolean isFieldValid(List<String> searchList, String content) {
        return Dict.dict.containsKey(content);
    }
}
