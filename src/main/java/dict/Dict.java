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

    static {
        dict = new ConcurrentHashMap<>();
    }

    /**
     * 若字典中存在key且对应的value值不为""，则将其替换为字典中该key对应的value
     * @param key 翻译内容
     * @return
     */
    public static String translate(String key) {
        if (dict.containsKey(key)) {
            if (dict.get(key).equals("")) {
                return key;
            }
            return dict.get(key);
        }
        return null;
    }
}
