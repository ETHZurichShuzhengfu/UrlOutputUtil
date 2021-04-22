package poi;

import filehandler.Parser.ParseResultEntry;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.util.List;

import dict.Dict;

/**
 * author:szf
 * desc:Excel写入
 * date:2021/2/19
 */
public class ExcelWriter {
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    /**
     * 简单类型的xml的解析方式
     * @param fileName 文件名
     * @param list 数据列表
     */
    public static void writeExcelWithXmlTypeNormal(String fileName, List<ParseResultEntry> list) {
        HSSFWorkbook hssfWorkBook = OutputExcel.getExcel();
        HSSFSheet sheet = hssfWorkBook.createSheet(fileName);
        for (int i = 0; i < list.size(); i++) {
            HSSFRow item_row = sheet.createRow(i);
            HSSFCell key_cell = item_row.createCell(KEY_INDEX);
            HSSFCell value_cell = item_row.createCell(VALUE_INDEX);
            String key = (String) list.get(i).getKey();
            String value = (String) list.get(i).getValue();
            String translate_key = Dict.translate(key);
            key_cell.setCellValue(translate_key);
            value_cell.setCellValue(value);
        }
    }
}
