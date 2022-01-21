package com.wenwo.evaluate.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.wenwo.evaluate.constant.ReadConstant;
import com.wenwo.evaluate.listener.base.BaseDataProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wyy
 * @version 1.0
 * @date 2020/12/8 10:23
 * 无模板监听器
 **/
@Slf4j
public class NoModelDataListener extends AnalysisEventListener<Map<String, Object>>  {

    /**
     * 自定义实现监听数据处理方法
     */
    private BaseDataProcessor listener;


    public NoModelDataListener(){

    }

    /**
     * 设置自定义数据加工强化器
     * @param listener
     */
    public NoModelDataListener(BaseDataProcessor listener){
        this.listener = listener;
    }

    /**
     * 最大一次处理条数，实际使用中可以3000条（推荐），然后清理list ，方便内存回收
     * 不推荐一次性读取所有并处理 这样很费内存并有很大可能性造成OOM
     */
    private static int BATCH_COUNT = ReadConstant.MAX_READ_COUNTS;

    /**
     * 解析数据集合
     */
    private List<Map<String, Object>> dataList = new ArrayList<>();



    /**
     * 自定义读取行数一次性 读完后会对list进行清空操作
     * 注意需求大小
     *
     * @param maxCount 一次性处理的最大行数
     */
    public static void setBatchCount(int maxCount) {
        if (maxCount < 1) {
            throw new ArithmeticException("请输入大于0的行数");
        }
        BATCH_COUNT = maxCount;
    }


    public List<Map<String, Object>> getDataList() {
        return dataList;
    }


    /**
     * 解析数据
     * @param integerStringMap integerStringMap
     * @param analysisContext analysisContext解析上下文
     */
    @Override
    public void invoke(Map<String, Object> integerStringMap, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(integerStringMap));
        dataList.add(integerStringMap);
        //达到一次读取上限就进行数据保存操作推荐一次保存3000条
        if (dataList.size() >= BATCH_COUNT) {
            listener.saveNoModelData(dataList);
            dataList.clear();
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据解析完成！");
    }

}
