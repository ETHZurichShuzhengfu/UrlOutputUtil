package filehandler;

import constants.UtilConfiguration;
import filehandler.FileCopyManager.FileCopyManager;
import filehandler.Parser.SAXHandler;
import org.xml.sax.SAXException;
import poi.OutputExcel;
import threadpool.UrlUtilThreadPool;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * author:szf
 * desc:对读取完成的配置文件进行处理
 * date:2021/2/19
 */
public class ConfigurationHandler {
    public static void execute(String apkName, Map<String, List<String>> configuration) throws InterruptedException, IOException {
        ExecutorService executorService = UrlUtilThreadPool.getThreadPool();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        CountDownLatch countDownLatch = new CountDownLatch(configuration.size());
        for (Map.Entry<String, List<String>> entry : configuration.entrySet()) {
            String filePath = entry.getKey();
            switch (UtilConfiguration.getFileHandlerType(filePath)) {

                /**
                 * 如果是xml类型,则需要初始化对应的parser并进行解析
                 */
                case UtilConfiguration.TYPE_XML: {
                    if (new File(UtilConfiguration.XML_PATH + filePath).exists()) {
                        executorService.submit(new ParserTask(countDownLatch, entry, apkName, entry.getKey(), factory));
                    } else {
                        System.out.println(UtilConfiguration.xmlNotFoundInfo(filePath));
                        countDownLatch.countDown();
                    }
                    break;
                }

                /**
                 * 如果是需要复制的文件，则创建一个FileCopyManager来进行文件复制操作
                 */
                case UtilConfiguration.TYPE_FILE_COPY: {
                    FileCopyManager manager = new FileCopyManager(entry.getKey(), entry.getValue());
                    executorService.submit(new CopyTask(countDownLatch, manager));
                    break;
                }

                default: {
                    countDownLatch.countDown();
                    break;
                }
            }
        }
        countDownLatch.await();
        FileOutputStream fos = new FileOutputStream(UtilConfiguration.RESULT_PATH + apkName + OutputExcel.EXCEL_SUFFIX);
        OutputExcel.getExcel().write(fos);
    }

    protected static class ParserTask implements Runnable {
        private final CountDownLatch countDownLatch;
        private final Map.Entry<String, List<String>> entry;
        private final String apkName;
        private final SAXParserFactory factory;
        private final String fileName;

        public ParserTask(CountDownLatch countDownLatch, Map.Entry<String, List<String>> entry,
                          String apkName, String fileName, SAXParserFactory factory) {
            this.countDownLatch = countDownLatch;
            this.entry = entry;
            this.apkName = apkName;
            this.factory = factory;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            SAXHandler handler = new SAXHandler(apkName, fileName, entry);
            javax.xml.parsers.SAXParser parser;
            try {
                parser = factory.newSAXParser();
                File file = new File(UtilConfiguration.XML_PATH + entry.getKey());
                parser.parse(file, handler);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    protected static class CopyTask implements Runnable {
        private final CountDownLatch countDownLatch;
        private final FileCopyManager manager;

        public CopyTask(CountDownLatch countDownLatch, FileCopyManager manager) {
            this.countDownLatch = countDownLatch;
            this.manager = manager;
        }

        @Override
        public void run() {
            try {
                manager.copyFiles();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
