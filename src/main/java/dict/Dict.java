package dict;

import constants.UtilConfiguration;
import threadpool.UrlUtilThreadPool;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * author:szf
 * desc:字典
 * date:2021/2/22
 */
public class Dict {

    public static ConcurrentMap<String, String> dict;
    private static final String DICT_SUFFIX = ".dict";
    private static final String DICT_ENTRY_SPLIT_SYMBOL = ":";

    static {
        dict = new ConcurrentHashMap<>();
    }

    public static void loadDict(String filePath) throws InterruptedException {
        ExecutorService executorService = UrlUtilThreadPool.getThreadPool();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println(UtilConfiguration.DICT_PATH_NOT_EXISTS_INFO);
            return;
        }
        if (file.listFiles() == null || file.listFiles().length == 0) {
            System.out.println(UtilConfiguration.DICT_LIST_EMPTY_INFO);
        }
        /**
         * 当file为文件且以.dict结尾，读取文件.
         */
        Stream<File> fileList = Arrays.stream(file.listFiles()).filter(f -> f.isFile()
                && f.getName().endsWith(DICT_SUFFIX));
        Stream<File> fileList2 = Arrays.stream(file.listFiles()).filter(f -> f.isFile()
                && f.getName().endsWith(DICT_SUFFIX));
        CountDownLatch countDownLatch = new CountDownLatch((int) fileList.count());
        fileList2.forEach(f -> executorService.submit(new DictLoader(countDownLatch, f)));
        countDownLatch.await();
    }

    /**
     * 若字典中存在key，则将其替换为字典中该key对应的value
     * @param key 翻译内容
     * @return
     */
    public static String translate(String key) {
        if (dict.containsKey(key)) {
            return dict.get(key);
        }
        return null;
    }

    protected static class DictLoader implements Runnable {
        private final CountDownLatch countDownLatch;
        private final File file;

        public DictLoader(CountDownLatch countDownLatch, File file) {
            this.countDownLatch = countDownLatch;
            this.file = file;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                int row = 1;
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.replaceAll("\\s+", " ");
                    AtomicInteger col = new AtomicInteger(1);
                    String[] entries = line.split(" ");
                    int finalRow = row;
                    Arrays.stream(entries).forEach(entry -> {
                        int splitIndex = entry.indexOf(DICT_ENTRY_SPLIT_SYMBOL);
                        if (splitIndex == -1) {
                            /**
                             * 字典需要以key:value的形式存在，冒号前后不能有空格，若冒号不存在，则跳过该entry并提示
                             */
                            System.out.println(UtilConfiguration.dictErrorHint(file, finalRow, col.get()));
                            col.getAndIncrement();
                            return;
                        }
                        String key = entry.substring(0, splitIndex);
                        String value = entry.substring(splitIndex + 1);
                        dict.put(key, value);
                        col.getAndIncrement();
                    });
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
