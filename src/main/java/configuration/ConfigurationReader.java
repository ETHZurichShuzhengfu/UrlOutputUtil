package configuration;

import constants.UtilConfiguration;
import dict.Dict;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:szf
 * desc:配置文件读取
 * date:2021/2/22
 */
public class ConfigurationReader {
    private static final String SPLIT_SYMBOL = ":";

    public static Map<String, List<String>> readConfiguration(String configurationFile) throws IOException {
        Map<String, List<String>> configuration = new HashMap<>();
        File file = new File(configurationFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        int row = 1;
        while ((line = reader.readLine()) != null) {
            int index = line.indexOf(SPLIT_SYMBOL);

            /**
             * 配置文件以 文件名:配置项的形式存在，如果冒号不存在，则跳过该项配置
             */
            if (index == -1) {
                System.out.println(configurationFile + "的第" + row + "行出现不规范输入，该行自动跳过，请检查配置文件是否有误");
                continue;
            }

            String fileName = line.substring(0, index).trim();
            String content = line.substring(index + 1).trim();

            /**
             * 如果是待解析的xml文件，那么对于content需要再进行读取，content为多个key-value键值对,以逗号分隔
             */
            if (UtilConfiguration.getFileHandlerType(fileName) == UtilConfiguration.TYPE_XML) {
                String[] pairs = content.split(",");
                for (String pair : pairs) {

                    /**
                     * 每个key-value键值对同样以冒号分隔，如果冒号不存在，则加入key-value中value置为""
                     */
                    int index1 = pair.indexOf(SPLIT_SYMBOL);
                    if (index1 == -1) {
                        Dict.dict.put(pair, "");
                        continue;
                    }
                    String dict_key = pair.substring(0, index1);
                    String dict_value = pair.substring(index1 + 1);
                    Dict.dict.put(dict_key, dict_value);
                }
            }
            configuration.put(fileName, Arrays.asList(content.split(",")));
            row++;
        }
        return configuration;
    }
}
