package com.wenwo.evaluate.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author doctor
 * @title: EasyExcelWriteUtil
 * @projectName wenwo-cloud-core-domain-contract-manager
 * @description: TODO
 * @date 2021/12/314:29
 */
public class EasyExcelWriteUtil {



    /**
     * 不需要对象model的导出  动态表头数据导出
     *
     * @param response  流
     * @param head      头数据 一个list表示一列
     * @param data      数据集合 一个list标识一行数据
     * @param fileName  文件名
     * @param sheetName sheet名称
     * @throws Exception
     */
    public static void dynamicNoModelWrite(HttpServletResponse response, List<List<String>> head, List<List<Object>> data,
                                           String fileName, String sheetName) throws Exception {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        EasyExcel.write(getOutputStream(fileName, response))
                .excelType(ExcelTypeEnum.XLSX).head(head).sheet(sheetName)
                .registerWriteHandler(horizontalCellStyleStrategy)
                //最大长度自适应 目前没有对应算法优化 建议注释不用 会出bug
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(data);
    }


    /**
     * 不需要对象model的导出  动态表头数据导出
     *
     * @param outputStream  流
     * @param head      头数据 一个list表示一列
     * @param data      数据集合 一个list标识一行数据
     * @param fileName  文件名
     * @param sheetName sheet名称
     * @throws Exception
     */
    public static void dynamicNoModelWrite(OutputStream outputStream, List<List<String>> head, List<List<Object>> data,
                                           String fileName, String sheetName) throws Exception {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        EasyExcel.write(outputStream)
                .excelType(ExcelTypeEnum.XLSX).head(head).sheet(sheetName)
                .registerWriteHandler(horizontalCellStyleStrategy)
                //最大长度自适应 目前没有对应算法优化 建议注释不用 会出bug
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(data);
    }

    /**
     * 导出 Excel ：一个 sheet，带表头.只有一个sheet 并以response流输出
     *
     * @param response  HttpServletResponse
     * @param data      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param model     映射实体类，Excel 模型
     * @throws Exception 异常
     */
    public static <T> void writeExcelByResponse(HttpServletResponse response, List<T> data,
                                                String fileName, String sheetName, Class model) throws Exception {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(getOutputStream(fileName, response), model)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet(sheetName)
                .registerWriteHandler(horizontalCellStyleStrategy)
                //最大长度自适应 目前没有对应算法优化 建议注释不用 会出bug
                //.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(data);

    }


    /**
     * 导出 Excel ：一个 sheet，带表头.只有一个sheet 并以response流输出
     *
     * @param data      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param model     映射实体类，Excel 模型
     * @throws Exception 异常
     */
    public static <T> void writeExcel(OutputStream outputStream ,List<T> data,String fileName, String sheetName, Class model) throws Exception {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(outputStream, model)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet(sheetName)
                .registerWriteHandler(horizontalCellStyleStrategy)
                //最大长度自适应 目前没有对应算法优化 建议注释不用 会出bug
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(data);

    }

    /**
     * 导出 Excel ：一个 sheet，带表头.指定两个sheet名称 直接作为response流输出
     *
     * @param response HttpServletResponse
     * @param data2    数据 list，每个元素为一个 BaseRowModel
     * @param fileName 导出的文件名
     * @param sheetName1 导入文件的 sheet 名
     * @param model    映射实体类，Excel 模型
     * @throws Exception 异常
     */
    public static <T> void writeExcelComplexSheetByResponse(HttpServletResponse response, List<T> data1, List<T> data2,
                                                            String fileName, String sheetName1, String sheetName2, Class model) throws Exception {
        if (sheetName1.trim().equals(sheetName2)) {
            throw new RuntimeException("请不要输入相同的sheet名称");
        }
        ExcelWriter excelWriter = EasyExcel.write(getOutputStream(fileName, response)).build();
        //这里 需要指定写用哪个class去写
        WriteSheet writeSheet = EasyExcel.writerSheet(0, sheetName1).head(model).build();
        excelWriter.write(data1, writeSheet);
        writeSheet = EasyExcel.writerSheet(1, sheetName2).head(model).build();
        excelWriter.write(data2, writeSheet);
        //千万别忘记finish 会帮忙关闭流
        excelWriter.finish();
    }


    /**
     * 导出 Excel ：一个 sheet，带表头.指定两个sheet名称 直接作为response流输出
     *
     * @param outputStream HttpServletResponse
     * @param data1    数据 list，每个元素为一个 BaseRowModel
     * @param sheetName2 导出的文件名
     * @param sheetName1 导入文件的 sheet 名
     * @param model    映射实体类，Excel 模型
     *
     */
    public static <T> void writeExcelComplexSheet(OutputStream outputStream, List<T> data1, List<T> data2,
                                                  String sheetName1, String sheetName2, Class model) {
        if (sheetName1.trim().equals(sheetName2)) {
            throw new RuntimeException("请不要输入相同的sheet名称");
        }
        ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
        //这里 需要指定写用哪个class去写
        WriteSheet writeSheet = EasyExcel.writerSheet(0, sheetName1).head(model).build();
        excelWriter.write(data1, writeSheet);
        writeSheet = EasyExcel.writerSheet(1, sheetName2).head(model).build();
        excelWriter.write(data2, writeSheet);
        //千万别忘记finish 会帮忙关闭流
        excelWriter.finish();
    }


    /**
     * 导出 Excel ：一个 sheet，带表头. 输出流 简单的写入流
     *
     * @param outputStream OutputStream
     * @param data         数据 list，每个元素为一个 BaseRowModel
     * @param sheetName    导入文件的 sheet 名
     * @param model        映射实体类，Excel 模型
     */
    public static <T> void writeExcelIn(OutputStream outputStream, List<T> data,
                                        String sheetName, Class model) {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(outputStream, model)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet(sheetName)
                .registerWriteHandler(horizontalCellStyleStrategy)
                //最大长度自适应 目前没有对应算法优化 建议注释不用 会出bug
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(data);

    }


    /**
     * 重复写入多个sheetNo 并导出
     *
     * @param outputStream 输出流
     * @param data         数据源list
     * @param sheetName    sheet名
     * @param model        导出模板类
     * @param sheetNo      想要导出多少个sheet
     * @param <T> 入参泛型
     *
     */
    public static <T> void repeatedWrite(OutputStream outputStream, List<T> data,
                                         String sheetName, Class model, Integer sheetNo) {
        ExcelWriter excelWriter = null;
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        try {
            // 这里 指定文件 写入输出流
            excelWriter = EasyExcel.write(outputStream, model).
                    registerWriteHandler(horizontalCellStyleStrategy).build();
            // 去调用写入,这里传入sheetNo 表示循环多少次写多少个sheet
            for (int i = 0; i < sheetNo; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class 实际上可以一直变
                WriteSheet writeSheet = EasyExcel.writerSheet(i, sheetName + i).head(model).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }






    /**
     * 导出 Excel ：一个 sheet，带表头.模板写入指定sheet并导出 不用浏览器的outputStream
     * 指定写入到哪个sheet中
     *
     * @param outputStream OutputStream 输出流
     * @param data         数据 list，每个元素为一个 BaseRowModel
     * @param in           输入流  模板
     * @param sheetNo      导入文件的 sheet  指定输出在哪一个sheet中，角标从0开始
     * @param model        映射实体类，Excel 模型
     */
    public static <T> void writeExcelInSheetNo(OutputStream outputStream, List<T> data,
                                               InputStream in, String sheetName, Class model, Integer sheetNo) {
        ExcelWriter excelWriter;
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo).sheetName(sheetName).build();
        //填写要写入的流文件  Excel文件
        excelWriter = EasyExcel.write(outputStream, model).withTemplate(in)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .build();
        // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
        excelWriter.write(data, writeSheet);
        excelWriter.finish();
    }


    /**
     * 重复写入多个sheetNo 并导出
     *
     * @param outputStream 输出流
     * @param datas        数据源list
     * @param model        导出模板类
     * @param <T>  泛型
     *
     */
    public static <T> void writeSheetByData(OutputStream outputStream, Class<T> model, List<T>... datas) {
        if(datas.length < 1){
            throw new RuntimeException("至少提供一个数据源集合");
        }
        ExcelWriter excelWriter = null;
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        try {
            // 这里 指定文件 写入输出流
            excelWriter = EasyExcel.write(outputStream, model).
                    registerWriteHandler(horizontalCellStyleStrategy).build();
            // 去调用写入,这里传入sheetNo 表示循环多少次写多少个sheet
            int i = 0;
            for (List<T> data : datas) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class 实际上可以一直变
                WriteSheet writeSheet = EasyExcel.writerSheet(i, String.valueOf(i)).head(model).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                excelWriter.write(data, writeSheet);
                i++;
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }


    /**
     * 导出文件时为Writer生成OutputStream.
     *
     * @param fileName 文件名
     * @param response response
     * @return 响应流输出
     * @throws Exception exception
     */
    private static OutputStream getOutputStream(String fileName,
                                                HttpServletResponse response) throws Exception {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            throw new Exception("导出excel表格失败!", e);
        }
    }
}
