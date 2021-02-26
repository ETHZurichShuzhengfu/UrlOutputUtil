package XmlParse;

import Constants.UtilConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
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
    private String outputFileName;    //导出excel的文件名
    private ArrayList<ParseResultEntry> resultEntries;  //检索结果列表
    private boolean isDictOn;
    private Parser parser;
    private ParseResultEntry entry;

    public SAXHandler(String outputFileName, Map.Entry<String, List<String>> conf_kv, boolean isDictOn) {
        this.outputFileName = outputFileName;
        this.conf_kv = conf_kv;
        this.isDictOn = isDictOn;
    }

    public String getFileName() {
        return outputFileName;
    }

    public void setFileName(String fileName) {
        this.outputFileName = fileName;
    }

    @Override
    public void startDocument() throws SAXException {
        resultEntries = new ArrayList<>();
        parser = Parser.getInstance(UtilConfiguration.getParseType(conf_kv.getKey()));
        if (parser == null) {
            System.out.println(conf_kv + ":" + UtilConfiguration.XML_PARSE_NOT_SUPPORT_INFO);
            return;
        }
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            parser.writeExcel(resultEntries, outputFileName, isDictOn);
            System.out.println(outputFileName + "写入完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.endDocument();
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
