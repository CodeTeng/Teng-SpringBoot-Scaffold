package com.lt.boot.utils;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/20 15:50
 */
public class ExcelUtils {
    /**
     * Excel 导出
     *
     * @param list 数据
     * @param t    导出类型
     */
    public static void doExport(List<?> list, Class t, String fileName, HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        fileName = fileName + System.currentTimeMillis();
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), t).sheet("模板").doWrite(list);
    }
}
