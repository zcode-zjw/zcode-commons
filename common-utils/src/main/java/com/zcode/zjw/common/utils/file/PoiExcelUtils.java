package com.zcode.zjw.common.utils.file;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class PoiExcelUtils<T> {
    /**
     * 与本次导出相关的POJO类型
     */
    private Class<T> clazz;

    /**
     * POJO字段的Map形式，key是字段上ExcelAttribute注解的sort值
     */
    private Map<Integer, Field> fieldMap = new HashMap<>();

    /**
     * 构造器，明确POJO的Class类型
     *
     * @param clazz
     */
    public PoiExcelUtils(Class<T> clazz) {
        this.clazz = clazz;
        // 得到所有字段，并用Map为字段建立索引
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelAttribute.class)) {
                fieldMap.put(field.getAnnotation(ExcelAttribute.class).sort(), field);
            }
        }
    }

    /**
     * 按模板导出到网络
     * 输入Excel模板+数据，导出Excel表格
     *
     * @param dataList  要导出的数据
     * @param excelName 指定本次导出的Excel表名
     * @param is        Excel模板，InputStream流
     * @param rowIndex  从模板哪一行开始拷贝样式
     * @param cellIndex 从模板哪一列开始拷贝样式
     * @param response  response响应
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void exportExcelWithTemplate(List<T> dataList, String excelName, InputStream is, Integer rowIndex, Integer cellIndex, HttpServletResponse response) throws IOException, IllegalAccessException {
        // 把数据封装到Excel
        XSSFWorkbook workbook = mapData2ExcelWithTemplate(dataList, is, rowIndex, cellIndex);

        //===============response响应导出Excel=================
        String fileName = new String(excelName.getBytes("UTF-8"), "ISO-8859-1");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + fileName);
        response.setHeader("filename", fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     * 按模板导出到本地
     * 输入Excel模板+数据+Excel保存路径，导出Excel表格
     *
     * @param dataList  要导出的数据
     * @param is        Excel模板，InputStream流
     * @param pathName  本地输出路径（比如/users/document/student_info.xlsx）
     * @param rowIndex  从模板哪一行开始拷贝样式
     * @param cellIndex 从模板哪一列开始拷贝样式
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void exportExcelToLocalWithTemplate(List<T> dataList, InputStream is, String pathName, Integer rowIndex, Integer cellIndex) throws IOException, IllegalAccessException {
        // 把数据封装到Excel
        XSSFWorkbook workbook = mapData2ExcelWithTemplate(dataList, is, rowIndex, cellIndex);

        //===============导出Excel到本地=================
        FileOutputStream fileOutputStream = new FileOutputStream(pathName);
        workbook.write(fileOutputStream);
    }

    /**
     * Excel导出到网络
     *
     * @param dataList  要导出的数据
     * @param excelName 指定本次导出的Excel表名
     * @param sheetName sheet名称
     * @param response  response响应
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void exportExcel(List<T> dataList, String excelName, String sheetName, HttpServletResponse response) throws IllegalAccessException, IOException {
        XSSFWorkbook workbook = mapData2Excel(dataList, sheetName);

        //===============response响应导出Excel=================
        String fileName = new String(excelName.getBytes("UTF-8"), "ISO-8859-1");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + fileName);
        response.setHeader("filename", fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }


    /**
     * Excel导出到本地
     *
     * @param dataList  要导出的数据
     * @param pathName  本地输出路径（比如/users/document/student_info.xlsx）
     * @param sheetName sheet名称
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void exportExcelToLocal(List<T> dataList, String pathName, String sheetName) throws IllegalAccessException, IOException {
        XSSFWorkbook workbook = mapData2Excel(dataList, sheetName);

        //===============导出Excel到本地=================
        FileOutputStream fileOutputStream = new FileOutputStream(pathName);
        workbook.write(fileOutputStream);
    }

    /**
     * Excel导入
     *
     * @param inputStream 要导入的Excel表
     * @param rowIndex    从哪一行开始读取
     * @param cellIndex   从哪一列开始读取
     * @return
     * @throws Exception
     */
    public List<T> importExcel(InputStream inputStream, Integer rowIndex, Integer cellIndex) throws Exception {
        //================准备要导入的Excel=================
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        List<T> dataList = new ArrayList<>();

        //================从Excel读取数据，并设置给pojoList================
        for (int i = rowIndex; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            // 遍历单元格（老婆组）
            T pojo = (T) clazz.newInstance();
            for (int j = cellIndex; j < row.getLastCellNum(); j++) {
                // 获取单元格的值
                XSSFCell cell = row.getCell(j);
                // 当前是朱丽叶，已经就位了，罗密欧在哪？
                Object value = getValue(cell);
                // 根据索引快速从老公组找出罗密欧
                Field field = fieldMap.get(j - cellIndex);
                // 把朱丽叶配给罗密欧（把单元格数据赋值给字段）
                field.setAccessible(true);
                field.set(pojo, convertAttrType(field, cell));
            }

            dataList.add(pojo);
        }

        return dataList;
    }


    //======================== private =======================

    /**
     * 把查询得到的数据封装到Excel
     *
     * @param dataList
     * @return
     * @throws IllegalAccessException
     */
    private XSSFWorkbook mapData2Excel(List<T> dataList, String sheetName) throws IllegalAccessException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        //=============创建表头===============
        XSSFRow row = sheet.createRow(0);
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(ExcelAttribute.class)) {
                row.createCell(i).setCellValue(field.getAnnotation(ExcelAttribute.class).value());
            }
        }

        //=========创建单元格，并设置数据（跳过表头）=======
        int size = fieldMap.keySet().size();
        for (int i = 0, length = dataList.size(); i < length; i++) {
            T pojo = dataList.get(i);
            row = sheet.createRow(i + 1);
            for (int k = 0; k < size; k++) {
                XSSFCell cell = row.createCell(k);
                Field field = fieldMap.get(k);
                field.setAccessible(true);
                mappingValue(field, cell, pojo);
            }
        }
        return workbook;
    }

    /**
     * 把查询得到的数据【按指定的模板样式】封装到Excel
     *
     * @param dataList
     * @param is
     * @param rowIndex
     * @param cellIndex
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     */
    private XSSFWorkbook mapData2ExcelWithTemplate(List<T> dataList, InputStream is, Integer rowIndex, Integer cellIndex) throws IOException, IllegalAccessException {
        //================得到模板=================
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);

        //=============从模板抽取样式===============
        CellStyle[] templateStyles = getTemplateStyles(rowIndex, cellIndex, sheet);

        //=========创建单元格，并设置样式和数据=======
        for (int i = 0; i < dataList.size(); i++) {
            T pojo = dataList.get(i);
            XSSFRow row = sheet.createRow(i + rowIndex);
            // 循环创建单元格（创建老婆组）
            for (int k = cellIndex; k < templateStyles.length + cellIndex; k++) {
                // 新建单元格：朱丽叶
                XSSFCell cell = row.createCell(k);
                // 为单元格设置样式：找到朱丽叶的化妆盒，给朱丽叶化妆
                cell.setCellStyle(templateStyles[k - cellIndex]);
                // 根据索引快速从老公组找出罗密欧
                Field field = fieldMap.get(k - cellIndex);
                // 把罗密欧给朱丽叶（把字段的数据赋值给单元格）
                field.setAccessible(true);
                mappingValue(field, cell, pojo);
            }
        }
        return workbook;
    }


    /**
     * 把字段Field的值设置给单元格Cell
     * Filed.get()得到的数据类型是Object，而cell.setCellValue()要求是具体的数据类
     * 因此需要判断字段类型，并把数据转换为原来的真是类型
     *
     * @param field
     * @param cell
     * @param pojo
     * @throws IllegalAccessException
     */
    private void mappingValue(Field field, Cell cell, Object pojo) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        if (Date.class.isAssignableFrom(fieldType)) {
            cell.setCellValue((Date) field.get(pojo));
        } else if (int.class.isAssignableFrom(fieldType) || Integer.class.isAssignableFrom(fieldType)) {
            cell.setCellValue((Integer) field.get(pojo));
        } else if (double.class.isAssignableFrom(fieldType) || Double.class.isAssignableFrom(fieldType)) {
            cell.setCellValue((Double) field.get(pojo));
        } else if (boolean.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType)) {
            cell.setCellValue((Boolean) field.get(pojo));
        } else if (BigDecimal.class.isAssignableFrom(fieldType)) {
            cell.setCellValue(((BigDecimal) field.get(pojo)).doubleValue());
        } else {
            cell.setCellValue((String) field.get(pojo));
        }
    }


    /**
     * 收集Excel模板的样式，方便对新建单元格复用，相当于打造一把格式刷
     *
     * @param rowIndex
     * @param cellIndex
     * @param sheet
     * @return
     */
    private CellStyle[] getTemplateStyles(Integer rowIndex, Integer cellIndex, XSSFSheet sheet) {
        XSSFRow dataTemplateRow = sheet.getRow(rowIndex);
        CellStyle[] cellStyles = new CellStyle[dataTemplateRow.getLastCellNum() - cellIndex];
        for (int i = 0; i < cellStyles.length; i++) {
            cellStyles[i] = dataTemplateRow.getCell(i + cellIndex).getCellStyle();
        }
        return cellStyles;
    }

    /**
     * 类型转换 将 cell单元格数据类型 转为 Java类型
     * <p>
     * 这里其实分两步：
     * 1.通过getValue()方法得到cell对应的Java类型的字符串类型，比如Date，getValue返回的不是Date类型，而是Date的格式化字符串
     * 2.判断Pojo当前字段是什么类型，把getValue()得到的字符串往该类型转
     *
     * @param field
     * @param cell
     * @return
     * @throws Exception
     */
    private Object convertAttrType(Field field, Cell cell) throws Exception {
        Class<?> fieldType = field.getType();
        if (String.class.isAssignableFrom(fieldType)) {
            return getValue(cell);
        } else if (Date.class.isAssignableFrom(fieldType)) {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(getValue(cell));
        } else if (int.class.isAssignableFrom(fieldType) || Integer.class.isAssignableFrom(fieldType)) {
            return Integer.parseInt(getValue(cell));
        } else if (double.class.isAssignableFrom(fieldType) || Double.class.isAssignableFrom(fieldType)) {
            return Double.parseDouble(getValue(cell));
        } else if (boolean.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType)) {
            return Boolean.valueOf(getValue(cell));
        } else if (BigDecimal.class.isAssignableFrom(fieldType)) {
            return new BigDecimal(getValue(cell));
        } else {
            return null;
        }
    }

    /**
     * 提供POI数据类型 --> Java数据类型的转换
     * 由于本方法返回值设为String，所以不管转换后是什么Java类型，都要以String格式返回
     * 所以Date会被格式化为yyyy-MM-dd HH:mm:ss
     * 后面根据需要自己另外转换，详见{@link PoiExcelUtils#convertAttrType(Field, Cell)}
     *
     * @param cell
     * @return
     */
    private String getValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getRichStringCellValue().getString().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // DateUtil是POI内部提供的日期工具类，可以把原本是日期类型的NUMERIC转为Java的Data类型
                    Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
                    String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(javaDate);
                    return dateString;
                } else {
                    // 无论Excel中是58还是58.0，数值类型在POI中最终都被解读为Double。这里的解决办法是通过BigDecimal先把Double先转成字符串，如果是.0结尾，把.0去掉
                    String strCell = "";
                    Double num = cell.getNumericCellValue();
                    BigDecimal bd = new BigDecimal(num.toString());
                    if (bd != null) {
                        strCell = bd.toPlainString();
                    }
                    // 去除 浮点型 自动加的 .0
                    if (strCell.endsWith(".0")) {
                        strCell = strCell.substring(0, strCell.indexOf("."));
                    }
                    return strCell;
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

}