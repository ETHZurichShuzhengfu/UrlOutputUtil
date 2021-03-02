package XmlParse;

import Constants.UtilConfiguration;
import POI.OutputExcel;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:szf
 * desc:SAX方式解析xml的handler
 * date:2021/2/19
 */
public class SAXHandler extends DefaultHandler {
    private Map.Entry<String, List<String>> conf_kv;  //检索参数,key为xml文件名,value为需要检索的内容列表
    private String apkName; //apk名
    private String fileName; //xml文件名
    private ArrayList<ParseResultEntry> resultEntries;  //检索结果列表
    private boolean isDictOn;
    private Parser parser;
    private ParseResultEntry entry;

    public SAXHandler(String apkName, String fileName, Map.Entry<String, List<String>> conf_kv, boolean isDictOn) {
        this.apkName = apkName;
        this.fileName = fileName;
        this.conf_kv = conf_kv;
        this.isDictOn = isDictOn;
    }

    @Override
    public void startDocument() {
        resultEntries = new ArrayList<>();
        parser = Parser.getInstance(UtilConfiguration.getParseType(conf_kv.getKey()));
        if (parser == null) {
            System.out.println(conf_kv + ":" + UtilConfiguration.XML_PARSE_NOT_SUPPORT_INFO);
            return;
        }
    }

    @Override
    public void endDocument() {
        try {
            parser.writeExcel(resultEntries, fileName, isDictOn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        entry = new ParseResultEntry();
        parser.startElement(uri, localName, qName, attributes, entry, resultEntries, conf_kv);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        parser.endElement(uri, localName, qName, entry, resultEntries, conf_kv);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        parser.characters(ch, start, length, entry, resultEntries, conf_kv);
    }
}
