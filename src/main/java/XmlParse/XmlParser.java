package XmlParse;

import Constants.UtilConfiguration;
import ThreadPool.UrlUtilThreadPool;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * author:szf
 * desc:xml解析器
 * date:2021/2/19
 */
public class XmlParser {
    public static void parseXml(String apkName, Map<String, List<String>> configuration, boolean isDictOn) throws InterruptedException {
        ExecutorService executorService = UrlUtilThreadPool.getThreadPool();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        CountDownLatch countDownLatch = new CountDownLatch(configuration.size());
        for (Map.Entry<String, List<String>> entry : configuration.entrySet()) {
            String xmlPath = UtilConfiguration.XML_PATH + entry.getKey();
            if (new File(xmlPath).exists()) {
                executorService.submit(new ParserTask(countDownLatch, entry, apkName + "_" + entry.getKey(), factory, isDictOn));
            } else {
                System.out.println(UtilConfiguration.xmlNotFoundInfo(xmlPath));
                countDownLatch.countDown();
            }
        }
        countDownLatch.await();
    }

    protected static class ParserTask implements Runnable {
        private final CountDownLatch countDownLatch;
        private final Map.Entry<String, List<String>> entry;
        private final String outputFileName;
        private final SAXParserFactory factory;
        private final boolean isDictOn;

        public ParserTask(CountDownLatch countDownLatch, Map.Entry<String, List<String>> entry,
                          String outputFileName, SAXParserFactory factory, boolean isDictOn) {
            this.countDownLatch = countDownLatch;
            this.entry = entry;
            this.outputFileName = outputFileName;
            this.factory = factory;
            this.isDictOn = isDictOn;
        }

        @Override
        public void run() {
            SAXHandler handler = new SAXHandler(outputFileName, entry, isDictOn);
            javax.xml.parsers.SAXParser parser;
            try {
                parser = factory.newSAXParser();
                parser.parse(new File(UtilConfiguration.XML_PATH + entry.getKey()), handler);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
