package XmlParse;

import Constants.UtilConfiguration;

/**
 * author:szf
 * desc:解析器抽象基类
 * date:2021/2/23
 */
abstract public class Parser implements Parse {
    public static Parser getInstance(int type) {
        switch (type) {
            case UtilConfiguration.XML_TYPE_NORMAL:
                return new BasicParser();
            default:
                return null;
        }
    }
}
