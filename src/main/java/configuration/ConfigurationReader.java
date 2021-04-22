package configuration;

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
            if (index == -1) {
                System.out.println(configurationFile + "的第" + row + "行出现不规范输入，该行自动跳过，请检查配置文件是否有误");
                continue;
            }
            String fileName = line.substring(0, index).trim();
            String content = line.substring(index + 1).trim();
            configuration.put(fileName, Arrays.asList(content.split(",")));
            row++;
        }
        return configuration;
    }
}
