package com.wenwo.evaluate.util;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.wenwo.evaluate.listener.NoModelDataListener;
import com.wenwo.evaluate.listener.ReadExcelListener;
import org.apache.poi.ss.formula.functions.T;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author doctor
 * @title: EasyExcelReadUtil
 * @projectName wenwo-cloud-core-domain-contract-manager
 * @description: TODO
 * @date 2021/12/314:29
 */
public class EasyExcelReadUtil {



    /**
     * 简单的读 只读单sheet默认第一个sheet
     *
     * @param inputStream 文件流
     * @param clazz       实体类
     * @return 数据源list
     * @author wyy
     */
    public static <T> List<T> simpleReadFirstSheet(InputStream inputStream, Class<T> clazz,int headRowNum) {
        return repeatedReadBySheetNos(inputStream, clazz, headRowNum);

    }


    /**
     * 读全部sheet,这里注意一个sheet不能读取多次，多次读取需要重新读取文件
     * 指定sheet读取 传入0、1、2分别读取的sheet是Excel从左到右
     *
     * @param sheetNos 输入需要读取的sheet数量 想要读取多少个就输入多少
     * @author wyy
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Class}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link ReadExcelListener}
     * <p>
     * 3. 直接读即可
     */
    public static <T> List<T> repeatedReadBySheetNos(InputStream inputStream, Class<T> clazz, int headRowNumber, Integer... sheetNos) {
        if (headRowNumber <= 0) {
            throw new RuntimeException("请输入大于零的数字");
        }
        List<T> res = new ArrayList<>();
        if(sheetNos.length == 0){
            ExcelReader excelReader = EasyExcel.read(inputStream).build();
            List<T> list = readSheet(excelReader, clazz, headRowNumber, 0);
            res.addAll(list);
        }else {
            for (Integer sheet : sheetNos) {
                ExcelReader excelReader = EasyExcel.read(inputStream).build();
                List<T> list = readSheet(excelReader, clazz, headRowNumber, sheet);
                res.addAll(list);
            }
        }
        return res;

    }


    /**
     * 简单的读 不需要对象模板读取出来是map类型
     *
     * @param inputStream 文件流
     * @return 数据源list
     * @author wyy
     */
    public static List<Map<String, Object>> noModelRead(InputStream inputStream) {
        NoModelDataListener noModelDataListener = new NoModelDataListener();
        EasyExcel.read(inputStream, noModelDataListener).headRowNumber(4).sheet().doRead();
        return noModelDataListener.getDataList();

    }


    /**
     * @param excelReader   excel读取reader
     * @param clazz         读取模板
     * @param headRowNumber 读取行数
     * @param sheet         读取的sheetNo
     * @param <T> 泛型
     * @return 返回数据集合
     * @author wyy
     */
    private static <T> List<T> readSheet(ExcelReader excelReader, Class<T> clazz, int headRowNumber, Integer sheet) {
        ReadExcelListener<T> dataListener = new ReadExcelListener<>();
        // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
        ReadSheet build = EasyExcel.readSheet(sheet).head(clazz).registerReadListener(dataListener).headRowNumber(headRowNumber).build();
        excelReader.read(build);
        return dataListener.getDataList();
    }

}
