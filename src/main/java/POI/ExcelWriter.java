package POI;

import XmlParse.ParseResultEntry;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import Dict.Dict;

/**
 * author:szf
 * desc:Excel写入
 * date:2021/2/19
 */
public class ExcelWriter {
    private static final String EXCEL_PATH = "excel/";
    private static final String EXCEL_SUFFIX = ".xls";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    public static void writeExcelWithXmlTypeNormal(String fileName, List<ParseResultEntry> list, boolean isDictOn) throws IOException {
        FileOutputStream fos = new FileOutputStream(EXCEL_PATH + fileName + EXCEL_SUFFIX);
        HSSFWorkbook hssfWorkBook = new HSSFWorkbook();
        HSSFSheet sheet = hssfWorkBook.createSheet(fileName);
        HSSFRow header_row = sheet.createRow(0);
        HSSFCell header_key_cell = header_row.createCell(KEY_INDEX);
        HSSFCell header_value_cell = header_row.createCell(VALUE_INDEX);
        header_key_cell.setCellValue("Key");
        header_value_cell.setCellValue("Value");
        for (int i = 0; i < list.size(); i++) {
            HSSFRow item_row = sheet.createRow(i + 1);
            HSSFCell key_cell = item_row.createCell(KEY_INDEX);
            HSSFCell value_cell = item_row.createCell(VALUE_INDEX);
            String key = (String) list.get(i).getKey();
            String value = (String) list.get(i).getValue();
            if (isDictOn) {
                String translate_key = Dict.translate(key);
                if (translate_key != null) {
                    key_cell.setCellValue(translate_key);
                } else {
                    key_cell.setCellValue(key);
                }
            } else {
                key_cell.setCellValue(key);
            }
            value_cell.setCellValue(value);
        }
        hssfWorkBook.write(fos);
        fos.close();
    }
}
