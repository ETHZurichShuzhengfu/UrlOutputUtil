package poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class OutputExcel {
    public static HSSFWorkbook workbook;
    public static final String EXCEL_SUFFIX = ".xls";
    private OutputExcel() {
    }

    /**
     * 返回一个单例excel
     * @return
     */
    public static HSSFWorkbook getExcel() {
        if (workbook == null) {
            synchronized (OutputExcel.class) {
                if (workbook == null) {
                    workbook = new HSSFWorkbook();
                }
            }
        }
        return workbook;
    }
}
