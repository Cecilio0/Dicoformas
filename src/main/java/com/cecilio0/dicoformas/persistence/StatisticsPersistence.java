package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.TimePeriodType;
import com.cecilio0.dicoformas.models.WeightStatsModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class StatisticsPersistence implements IStatisticsPersistence {
	@Override
	public void writeStatisticsToExcelFile(TimePeriodType timePeriodType, String fileRoute, List<WeightStatsModel> stats) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = workbook.createSheet("Pesos por Fecha");
		
		Font boldFont = workbook.createFont();
		boldFont.setBold(true);
		
		// Company
		Cell companyCell = sheet.createRow(0).createCell(0);
		companyCell.setCellValue("Empresa : DICOFORMAS S.A.S.");
		
		CellStyle companyStyle = workbook.createCellStyle();
		companyStyle.setAlignment(HorizontalAlignment.CENTER);
		companyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		companyStyle.setFont(boldFont);
		companyCell.setCellStyle(companyStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
		
		// NIT
		Cell nitCell = sheet.createRow(1).createCell(0);
		nitCell.setCellValue("NIT : 901.668.970-5");
		
		CellStyle nitStyle = workbook.createCellStyle();
		nitStyle.setAlignment(HorizontalAlignment.CENTER);
		nitStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		nitStyle.setFont(boldFont);
		nitCell.setCellStyle(nitStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));
		
		// Title
		Row titleRow = sheet.createRow(3);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("PESOS PT Y MP POR " + (timePeriodType.equals(TimePeriodType.MONTH) ? "MES" : "AÑO"));
		
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		titleStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 242, (byte) 206, (byte) 239}));
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setBorderBottom(BorderStyle.THIN);
		titleStyle.setBorderLeft(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);
		titleStyle.setBorderTop(BorderStyle.THIN);
		
		for (int i = 0; i < 3; i++) {
			Cell mergedCell = titleRow.getCell(i);
			if (mergedCell == null) {
				mergedCell = titleRow.createCell(i);
			}
			mergedCell.setCellStyle(titleStyle);
		}
		
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 2));
		
		// Headers
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setFont(boldFont);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		
		Row headers = sheet.createRow(4);
		Cell dateTitleCell = headers.createCell(0);
		dateTitleCell.setCellValue("FECHA");
		dateTitleCell.setCellStyle(headerStyle);
		
		Cell saleOrderWeightTitleCell = headers.createCell(1);
		saleOrderWeightTitleCell.setCellValue("PESO PT");
		saleOrderWeightTitleCell.setCellStyle(headerStyle);
		
		Cell purchaseOrderWeightTitleCell = headers.createCell(2);
		purchaseOrderWeightTitleCell.setCellValue("PESO MP");
		purchaseOrderWeightTitleCell.setCellStyle(headerStyle);
		
		// Data
		CellStyle dataStyle = workbook.createCellStyle();
		dataStyle.setBorderTop(BorderStyle.THIN);
		dataStyle.setBorderRight(BorderStyle.THIN);
		dataStyle.setBorderBottom(BorderStyle.THIN);
		dataStyle.setBorderLeft(BorderStyle.THIN);
		
		int index = 5;
		for (WeightStatsModel stat : stats) {
			Row row = sheet.createRow(index++);
			
			Cell dateCell = row.createCell(0);
			dateCell.setCellValue(stat.getDate().toString().substring(0, timePeriodType == TimePeriodType.MONTH? 7: 4));
			dateCell.setCellStyle(dataStyle);
			
			Cell saleOrderWeightCell = row.createCell(1);
			saleOrderWeightCell.setCellValue(stat.getSaleOrderWeight());
			saleOrderWeightCell.setCellStyle(dataStyle);
			
			Cell purchaseOrderWeightCell = row.createCell(2);
			purchaseOrderWeightCell.setCellValue(stat.getPurchaseOrderWeight());
			purchaseOrderWeightCell.setCellStyle(dataStyle);
		}
		
//		// Resize columns, may not be necessary
//		for (int i = 0; i < 3; i++) {
//			sheet.autoSizeColumn(i);
//		}
		
		// Write workbook to file
		FileOutputStream fileOut = new FileOutputStream(fileRoute);
		workbook.write(fileOut);
		
		workbook.close();
	}
}
