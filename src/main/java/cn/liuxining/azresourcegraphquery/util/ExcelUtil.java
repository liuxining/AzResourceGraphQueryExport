package cn.liuxining.azresourcegraphquery.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.io.File;
import java.util.*;

/**
 * <p>Title: ExcelUtil </p >
 * <p>Description: ExcelUtil </p >
 *
 * @author: liuxining
 * @date: 2021/7/4 00:29
 * @version: v1.0
 */
public class ExcelUtil {

    public static boolean writeExcelCustomHead(String fileName, Map<String, List<Map<String, String>>> data) {
        File file = new File(fileName);
        ExcelWriter excelWriter = EasyExcel.write(file).build();

        Set<Map.Entry<String, List<Map<String, String>>>> entrySet = data.entrySet();
        for (Map.Entry<String, List<Map<String, String>>> entry:entrySet) {
            String sheetName = entry.getKey();
            List<Map<String, String>> recordList = entry.getValue();

            // 获取head
            Map<String, String> headMap = recordList.get(0);
            List<List<String>> sheetHead = new ArrayList<>(headMap.size());
            Set<Map.Entry<String, String>> headMapEntrySet = headMap.entrySet();
            for (Map.Entry<String, String> item:headMapEntrySet) {
                List<String> headColumnList = new ArrayList<>(1);
                headColumnList.add(item.getValue());
                sheetHead.add(headColumnList);
            }
            recordList.remove(0);

            // 获取每一行数据
            List<List<String>> sheetData = new ArrayList<>();
            for (Map<String, String> item:recordList) {
                List<String> recordData = new ArrayList<>(headMap.size());
                Set<Map.Entry<String, String>> headMapEntrySet2 = headMap.entrySet();
                for (Map.Entry<String, String> headEntryItem:headMapEntrySet2) {
                    String headValue = item.get(headEntryItem.getValue());
                    recordData.add(headValue);
                }
                sheetData.add(recordData);
            }


            // 写入文件
            WriteSheet writeSheet = EasyExcel.writerSheet().head(sheetHead).build();
            writeSheet.setSheetName(sheetName);

            excelWriter.write(sheetData, writeSheet);

        }
        excelWriter.finish();
        return true;

    }

}
