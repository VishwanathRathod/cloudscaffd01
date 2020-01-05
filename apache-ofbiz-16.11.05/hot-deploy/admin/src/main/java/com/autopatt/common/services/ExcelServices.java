package com.autopatt.common.services;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.codehaus.plexus.util.FastMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelServices {
    public static final String module = ExcelServices.class.getName();

    /***
     * Service to generate Excel file for the given input data
     * Inputs
     * 1) reportMetaInfo - shown in top of the sheet, this is map of key-value pairs shown in each line
     * 2) reportGroups - this contains list of group. Each group containing 3 sections (header, records, footer)
     * 3) reportSummary - show at bottom of the sheet, summary of the whole report
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> generateExcel (DispatchContext dctx, Map<String, Object> context) {
        List<Object> reportTitleList = (List) context.get("reportTitle");
        List<Object> reportMetaInfoList = (List) context.get("reportMetaInfo");
        List<Map<String, Object>> reportGroups = (List) context.get("reportGroups");
        List<Map<Object, Object>> reportTotalSummaryList = (List) context.get("reportTotalSummary");
        List<Object> reportSummaryList = (List) context.get("reportSummary");
        String sheetName = (String) context.get("sheetName");
        String groupByReport = (String) context.get("groupByReport");
        if(UtilValidate.isEmpty(sheetName)) sheetName = "Sheet1";

        int totalCells = 1;
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
            int maxColumnsCount = 0;
            HSSFSheet sheet = workbook.createSheet(sheetName);
            int rowNum = 0;

            for (Map<String, Object> reportGroup: reportGroups) {
                Map<Object, Object> reportGroupHeader = (Map) reportGroup.get("reportGroupHeader");
                int headerSize = reportGroupHeader.size();
                if (headerSize > totalCells) {
                    totalCells = headerSize;
                }
            }
            totalCells = totalCells - 1;
            // Add a report level title info (to show company name, location, some other letter-data kind of details). Aligned center
            if (UtilValidate.isNotEmpty(reportTitleList)) {
                for (Object reportTitle : reportTitleList) {
                    HSSFRow reportTitleRow = sheet.createRow(rowNum++);
                    HSSFCell titleCell = reportTitleRow.createCell(0);
                    if (reportTitle instanceof String)
                        titleCell.setCellValue((String) reportTitle);
                    else if (reportTitle instanceof Integer) {
                        Integer i = (Integer) reportTitle;
                        BigDecimal b = new BigDecimal(i.intValue());
                        b = b.setScale(2, RoundingMode.HALF_UP);
                        titleCell.setCellValue(b.intValue());
                    } else if (reportTitle instanceof Double) {
                        Double i = (Double) reportTitle;
                        BigDecimal b = new BigDecimal(i.doubleValue());
                        b = b.setScale(2, RoundingMode.HALF_UP);
                        titleCell.setCellValue(b.doubleValue());
                    }
                    CellUtil.setAlignment(titleCell, workbook, CellStyle.ALIGN_CENTER);
                    CellUtil.setCellStyleProperty(titleCell, workbook, CellUtil.FILL_BACKGROUND_COLOR, IndexedColors.BLUE_GREY.index);
                    sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, totalCells));
                }
                // Add empty row between the new row
                HSSFRow row = sheet.createRow(rowNum++);
            }

            // Add Report Level Header (Meta-info about the report)
            if (UtilValidate.isNotEmpty(reportMetaInfoList)) {
                for (Object reportMetaInfo : reportMetaInfoList) {
                    HSSFRow reportMetaInfoRow = sheet.createRow(rowNum++);
                    HSSFCell cell = reportMetaInfoRow.createCell(0);
                    if (reportMetaInfo instanceof String)
                        cell.setCellValue((String) reportMetaInfo);
                    else if (reportMetaInfo instanceof Integer) {
                        Integer i = (Integer) reportMetaInfo;
                        BigDecimal b = new BigDecimal(i.intValue());
                        b = b.setScale(2, RoundingMode.HALF_UP);
                        cell.setCellValue(b.intValue());
                    } else if (reportMetaInfo instanceof Double) {
                        Double i = (Double) reportMetaInfo;
                        BigDecimal b = new BigDecimal(i.doubleValue());
                        b = b.setScale(2, RoundingMode.HALF_UP);
                        cell.setCellValue(b.doubleValue());
                    }
                    sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, totalCells));
                }
                HSSFRow row = sheet.createRow(rowNum++);
            }

            List<Object> headerList = new ArrayList<>();
            Map<Object, Object> headerLevelStyle = new HashMap<>();
            for (Map<String, Object> reportGroup: reportGroups) {
                Map<Object, Object> reportGroupHeader = (Map) reportGroup.get("reportGroupHeader");
                HSSFRow reportGroupHeaderRow = sheet.createRow(rowNum++);
                int headerCellNum = 0;

                for (Map.Entry<Object, Object> reportGroupHeaderValue : reportGroupHeader.entrySet()) {
                    Object key = reportGroupHeaderValue.getKey();
                    Map val = (Map)reportGroupHeaderValue.getValue();
                    headerList.add(key);
                    HSSFCell cell = reportGroupHeaderRow.createCell(headerCellNum++);
                    cell.setCellValue((String) val.get("description"));

                    CellStyle style = workbook.createCellStyle();
                    style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);

                    HSSFFont font = workbook.createFont();
                    font.setColor(HSSFColor.BLACK.index);
                    font.getBoldweight();
                    style.setFont(font);
                    cell.setCellStyle(style);
                }
                if(headerCellNum > maxColumnsCount) maxColumnsCount = headerCellNum;

                List<List<Map<Object, Object>>> reportGroupRecords = (List) reportGroup.get("reportGroupRecords");
                for (List<Map<Object, Object>> customerRecords: reportGroupRecords) {
                    String field = null;
                    for (Map<Object, Object> reportGroupRecord: customerRecords) {
                        field = (String) reportGroupRecord.get(groupByReport);
                        HSSFRow reportGroupRecordRow = sheet.createRow(rowNum++);
                        int reportGroupCellNum = 0;
                        for (Object header: headerList) {
                            Map settings = (Map)reportGroupHeader.get(header);
                            HSSFCell cell = reportGroupRecordRow.createCell(reportGroupCellNum++);
                            Object headerCell = reportGroupRecord.get(header);
                            if (headerCell instanceof String)
                                cell.setCellValue((String)headerCell);
                            else if (headerCell instanceof Integer) {
                                Integer i = (Integer) headerCell;
                                BigDecimal b = new BigDecimal(i.intValue());
                                b = b.setScale(2, RoundingMode.HALF_UP);
                                cell.setCellValue(b.intValue());
                            } else if (headerCell instanceof Double) {
                                Double i = (Double) headerCell;
                                BigDecimal b = new BigDecimal(i.doubleValue());
                                b = b.setScale(2, RoundingMode.HALF_UP);
                                cell.setCellValue(b.doubleValue());
                            } else if (headerCell instanceof Long) {
                                try {
                                    Long i = (Long) headerCell;
                                    Date newDate = new Date(i);
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    cell.setCellValue(formatter.format(newDate).toString());
                                } catch (Exception e) {
                                    Debug.logError(e, module);
                                }
                            }
                            if(UtilValidate.isNotEmpty(settings.get("cellFormat"))) {
                                if(UtilValidate.isEmpty(headerLevelStyle.get(header))) {
                                    HSSFDataFormat df = workbook.createDataFormat();
                                    HSSFCellStyle currencyStyle = workbook.createCellStyle();
                                    currencyStyle.setDataFormat(df.getFormat((String)settings.get("cellFormat")));
                                    headerLevelStyle.put(header, (HSSFCellStyle) currencyStyle);
                                }
                                cell.setCellStyle((HSSFCellStyle) headerLevelStyle.get(header));
                            }
                        }
                    }
                    Map<Object, Object> reportGroupSummary = (Map) reportGroup.get("reportGroupSummary");
                    if(UtilValidate.isNotEmpty(reportGroupSummary)) {
                        HSSFRow reportGroupSummaryRow = sheet.createRow(rowNum++);
                        int reportGroupSummaryCellNum = 0;
                        for (Object header: headerList) {
                            Map settings = (Map)reportGroupHeader.get(header);
                            HSSFCell cell = reportGroupSummaryRow.createCell(reportGroupSummaryCellNum++);
                            Map summaryRecord = (Map) reportGroupSummary.get(field);
                            Object headerCell = summaryRecord.get(header);
                            if (headerCell instanceof String)
                                cell.setCellValue((String)headerCell);
                            else if (headerCell instanceof Integer) {
                                Integer i = (Integer) headerCell;
                                BigDecimal b = new BigDecimal(i.intValue());
                                b = b.setScale(2, RoundingMode.HALF_UP);
                                cell.setCellValue(b.intValue());
                            } else if (headerCell instanceof Double) {
                                Double i = (Double) headerCell;
                                BigDecimal b = new BigDecimal(i.doubleValue());
                                b = b.setScale(2, RoundingMode.HALF_UP);
                                cell.setCellValue(b.doubleValue());
                            }
                            if(UtilValidate.isNotEmpty(settings.get("cellFormat")) && UtilValidate.isNotEmpty(settings.get("backgroundColor"))) {
                                String cellFormat = "cellFormat" + settings.get("cellFormat");
                                String backgroundColorProperty = "backgroundColor" + settings.get("backgroundColor");
                                String cellKey = cellFormat + backgroundColorProperty;
                                if (UtilValidate.isEmpty(headerLevelStyle.get(cellKey))) {
                                    HSSFDataFormat df = workbook.createDataFormat();
                                    HSSFCellStyle style = workbook.createCellStyle();
                                    style.setDataFormat(df.getFormat((String) settings.get("cellFormat")));
                                    style.setFillForegroundColor((Short) settings.get("backgroundColor"));
                                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                                    headerLevelStyle.put(cellKey, (HSSFCellStyle) style);
                                }
                                cell.setCellStyle((HSSFCellStyle) headerLevelStyle.get(cellKey));
                            } else if(UtilValidate.isNotEmpty(settings.get("cellFormat"))) {
                                String cellFormat = "cellFormat" + settings.get("cellFormat");
                                if (UtilValidate.isEmpty(headerLevelStyle.get(cellFormat))) {
                                    HSSFDataFormat df = workbook.createDataFormat();
                                    HSSFCellStyle style = workbook.createCellStyle();
                                    style.setDataFormat(df.getFormat((String) settings.get("cellFormat")));
                                    headerLevelStyle.put(cellFormat, (HSSFCellStyle) style);
                                }
                                cell.setCellStyle((HSSFCellStyle) headerLevelStyle.get(cellFormat));
                            } else if(UtilValidate.isNotEmpty(settings.get("backgroundColor"))) {
                                String backgroundColorProperty = "backgroundColor" + settings.get("backgroundColor");
                                if (UtilValidate.isEmpty(headerLevelStyle.get(backgroundColorProperty))) {
                                    HSSFCellStyle style = workbook.createCellStyle();
                                    style.setFillForegroundColor((Short) settings.get("backgroundColor"));
                                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                                    headerLevelStyle.put(backgroundColorProperty, (HSSFCellStyle) style);
                                }
                                cell.setCellStyle((HSSFCellStyle) headerLevelStyle.get(backgroundColorProperty));
                            }

                            if(UtilValidate.isNotEmpty(settings.get("align"))) {
                                CellUtil.setAlignment(cell, workbook,(Short) settings.get("align"));
                            }
                        }
                    }
                }
            } // End of iterating reportGroups

            if (UtilValidate.isNotEmpty(reportGroups)) {
                HSSFRow row = sheet.createRow(rowNum++);
                sheet.addMergedRegion(new CellRangeAddress(rowNum -1, rowNum-1,0,totalCells));
            }

            // Show Totals Record (if available) at report level
            if (UtilValidate.isNotEmpty(reportTotalSummaryList)) {
                for (Map<Object, Object> reportTotalSummary: reportTotalSummaryList) {
                    HSSFRow reportTotalSummaryRow = sheet.createRow(rowNum++);
                    int reportTotalCellNum = 0;
                    for (Object header : headerList) {
                        Map settings = (Map)reportTotalSummary.get(header);
                        HSSFCell cell = reportTotalSummaryRow.createCell(reportTotalCellNum++);
                        if (UtilValidate.isNotEmpty(settings)) {
                            Object headerCell = settings.get("description");
                            if (UtilValidate.isNotEmpty(headerCell)) {
                                if (headerCell instanceof String)
                                    cell.setCellValue((String) headerCell);
                                else if (headerCell instanceof Integer) {
                                    Integer i = (Integer) headerCell;
                                    BigDecimal b = new BigDecimal(i.intValue());
                                    b = b.setScale(2, RoundingMode.HALF_UP);
                                    cell.setCellValue(b.intValue());
                                } else if (headerCell instanceof Double) {
                                    Double i = (Double) headerCell;
                                    BigDecimal b = new BigDecimal(i.doubleValue());
                                    b = b.setScale(2, RoundingMode.HALF_UP);
                                    cell.setCellValue(b.doubleValue());
                                }
                            }

                            if(UtilValidate.isNotEmpty(settings.get("cellFormat")) && UtilValidate.isNotEmpty(settings.get("backgroundColor"))) {
                                String cellFormat = "cellFormat" + settings.get("cellFormat");
                                String backgroundColorProperty = "backgroundColor" + settings.get("backgroundColor");
                                String cellKey = cellFormat + backgroundColorProperty;
                                if (UtilValidate.isEmpty(headerLevelStyle.get(cellKey))) {
                                    HSSFDataFormat df = workbook.createDataFormat();
                                    HSSFCellStyle style = workbook.createCellStyle();
                                    style.setDataFormat(df.getFormat((String) settings.get("cellFormat")));
                                    style.setFillForegroundColor((Short) settings.get("backgroundColor"));
                                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                                    headerLevelStyle.put(cellKey, (HSSFCellStyle) style);
                                }
                                cell.setCellStyle((HSSFCellStyle) headerLevelStyle.get(cellKey));
                            } else if(UtilValidate.isNotEmpty(settings.get("cellFormat"))) {
                                String cellFormat = "cellFormat" + settings.get("cellFormat");
                                if (UtilValidate.isEmpty(headerLevelStyle.get(cellFormat))) {
                                    HSSFDataFormat df = workbook.createDataFormat();
                                    HSSFCellStyle style = workbook.createCellStyle();
                                    style.setDataFormat(df.getFormat((String) settings.get("cellFormat")));
                                    headerLevelStyle.put(cellFormat, (HSSFCellStyle) style);
                                }
                                cell.setCellStyle((HSSFCellStyle) headerLevelStyle.get(cellFormat));
                            } else if(UtilValidate.isNotEmpty(settings.get("backgroundColor"))) {
                                String backgroundColorProperty = "backgroundColor" + settings.get("backgroundColor");
                                if (UtilValidate.isEmpty(headerLevelStyle.get(backgroundColorProperty))) {
                                    HSSFCellStyle style = workbook.createCellStyle();
                                    style.setFillForegroundColor((Short) settings.get("backgroundColor"));
                                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                                    headerLevelStyle.put(backgroundColorProperty, (HSSFCellStyle) style);
                                }
                                cell.setCellStyle((HSSFCellStyle) headerLevelStyle.get(backgroundColorProperty));
                            }

                            if(UtilValidate.isNotEmpty(settings.get("align"))) {
                                CellUtil.setAlignment(cell, workbook,(Short) settings.get("align"));
                            }
                        }
                    }
                }
                HSSFRow row = sheet.createRow(rowNum++);
                sheet.addMergedRegion(new CellRangeAddress(rowNum -1, rowNum-1,0,totalCells));
            }

            if(UtilValidate.isNotEmpty(reportSummaryList)) {
                // Render Report level summary
                for (Object reportSummary : reportSummaryList) {
                    HSSFRow reportSummaryRow = sheet.createRow(rowNum++);
                    HSSFCell cell = reportSummaryRow.createCell(0);
                    if (reportSummary instanceof String)
                        cell.setCellValue((String) reportSummary);
                    else if (reportSummary instanceof Integer) {
                        Integer i = (Integer) reportSummary;
                        BigDecimal b = new BigDecimal(i.intValue());
                        b = b.setScale(2, RoundingMode.HALF_UP);
                        cell.setCellValue(b.intValue());
                    } else if (reportSummary instanceof Double) {
                        Double i = (Double) reportSummary;
                        BigDecimal b = new BigDecimal(i.doubleValue());
                        b = b.setScale(2, RoundingMode.HALF_UP);
                        cell.setCellValue(b.doubleValue());
                    }
                    sheet.addMergedRegion(new CellRangeAddress(rowNum -1, rowNum-1,0,totalCells));
                }
            }

            // Autosize all columns to fit to content
            for(int i=0;i<maxColumnsCount;i++) {
                sheet.autoSizeColumn(i);
            }

        } catch (Exception e) {
            Debug.logError(e, module);
        }
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("hssWorkbook", workbook);
        return result;
    }
}
