package Constants;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * author:szf
 * desc:常量
 * date:2021/2/23
 */
public class UtilConfiguration {

    /**
     * 控制台提示信息
     */
    public static final String DICT_PATH_NOT_EXISTS_INFO = "Directory Dict Not Found!Skip Load Dict";
    public static final String DICT_LIST_EMPTY_INFO = "Could Not Found Any Dict!Skip Load Dict";
    public static final String PROGRAM_END_INFO = "中间目录删除完成,程序执行完毕";
    public static final String INPUT_ERROR = "INPUT APK IS NULL OR APK NOT FOUND!";
    public static final String CONF_EMPTY_INFO = "读取到的配置文件内容为空,请根据规范检查配置文件格式是否有问题";
    public static final String XML_PARSE_NOT_SUPPORT_INFO = "Sorry,current xml type is Not supported yet";
    /**
     * xml类型,根据xml不同的结构适配不同的解析器
     */
    public static final int XML_TYPE_NORMAL = 1;
    public static final int XML_TYPE_NOT_SUPPORT = -1;
    public static final List<String> xmlType_1 = Arrays.asList("integers.xml", "strings.xml", "bools.xml",
            "colors.xml", "dimens.xml");

    /**
     * 文件目录
     */
    public static String DICT_PATH = "dict/";
    public static final String XML_PATH = "out/res/values/";
    public static final String CONFIGURATION_PATH = "configuration/";
    public static final String APK_FILEPATH = "apk/";
    public static final String MiddleOutDirectory = "out/";
    public static final String RESULT_PATH = "result/";
    public static final String ADDRESS_XML_PATH = MiddleOutDirectory + "assets/address.xml";

    public static String dictErrorHint(File file, int row, int col) {
        return "Dict " + file.getName() + " has error in row" + row + " item" + col;
    }

    public static String xmlNotFoundInfo(String xmlPath) {
        return "xml file " + xmlPath + " not found,please check";
    }

    /**
     * 获取解析的xml的类型
     */
    public static int getParseType(String xmlName) {
        if (xmlType_1.contains(xmlName))
            return XML_TYPE_NORMAL;
        return XML_TYPE_NOT_SUPPORT;
    }
}
