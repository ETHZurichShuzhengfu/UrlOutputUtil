package filehandler.Parser;

import constants.UtilConfiguration;
/**
 * author:szf
 * desc:解析器抽象基类
 * date:2021/2/23
 */
abstract public class Parser implements Parse {
    public static Parser getInstance(int type) {
        switch (type) {
            case UtilConfiguration.TYPE_XML_NORMAL:
                return new BasicParser();
            default:
                return null;
        }
    }
}
