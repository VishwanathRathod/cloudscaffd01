package com.autopatt.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class ExcelUtils {

    public static void flushExcelToResponse(String fileName, HSSFWorkbook wb, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/vnd.ms-excel");
        String contentDispositionValue = "attachment;filename=" + fileName + ".xls";
        contentDispositionValue = contentDispositionValue.replaceAll(" ", "_");
        response.setHeader("Content-Disposition", contentDispositionValue);
        // Write the output
        OutputStream out = response.getOutputStream();
        wb.write(out);
        out.close();
    }

}
