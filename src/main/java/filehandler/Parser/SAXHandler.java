package filehandler.Parser;

import constants.UtilConfiguration;
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
    private Parser parser;  //解析器
    private ParseResultEntry entry;

    public SAXHandler(String apkName, String fileName, Map.Entry<String, List<String>> conf_kv) {
        this.apkName = apkName;
        this.fileName = fileName;
        this.conf_kv = conf_kv;
    }

    /**
     * 继承于sax的DefaultHandler的SAXHandler有五大接口:startElement,endElement,characters,startDocument,endDocument
     * 本工具使用SAX的方式解析XML，以最简单的一种格式举例: <tag>value</tag>
     *
     * startDocument与endDocument在整个xml文档解析的开始与结束时各执行一次
     *
     * startElement为读取到某一开始的tag时要进行的处理，
     * characters为读取到value时要进行的处理,通常使用 String content = new String(ch, start, length)获取value值即可
     * endElement为读取到某一结束的tag时进行的处理
     *
     * Parse接口中的同名方法在对应位置直接调用,startDocument用于判断xml是否可以解析以及初始化结果列表
     * endDocument调用Parse接口中的writeSheet方法，将结果列表写入excel文件的一张表格
     */

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
            parser.writeSheet(resultEntries, fileName);
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
