package com.cecilio0.dicoformas.utils;

import com.cecilio0.dicoformas.exceptions.FileFormatNotSupportedException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class ExcelUtil {
	
	public static Workbook getWorkbookFromExcelFile(String fileRoute) throws IOException {
		String[] arr = fileRoute.split("\\.");
		
		File file = new File(fileRoute);
		FileInputStream fis = new FileInputStream(file);
		
		Workbook workbook;
		if(arr[arr.length-1].equalsIgnoreCase("xls")) {
			workbook = new HSSFWorkbook(fis);
		} else if (arr[arr.length-1].equalsIgnoreCase("xlsx")) {
			workbook = new XSSFWorkbook(fis);
		} else {
			throw new FileFormatNotSupportedException("The provided file is neither xls nor xlsx");
		}
		
		return workbook;
	}
	
	public static void writeTitles(Workbook workbook, Sheet sheet, String title, byte[] titleColor, int columnCount) {
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
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnCount-1));
		
		// NIT
		Cell nitCell = sheet.createRow(1).createCell(0);
		nitCell.setCellValue("NIT : 901.668.970-5");
		
		CellStyle nitStyle = workbook.createCellStyle();
		nitStyle.setAlignment(HorizontalAlignment.CENTER);
		nitStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		nitStyle.setFont(boldFont);
		nitCell.setCellStyle(nitStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, columnCount-1));
		
		// Intermediate Cell
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, columnCount-1));
		
		// Title
		Row titleRow = sheet.createRow(3);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(title);
		
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		titleStyle.setFillForegroundColor(new XSSFColor(titleColor));
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setBorderBottom(BorderStyle.THIN);
		titleStyle.setBorderLeft(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);
		titleStyle.setBorderTop(BorderStyle.THIN);
		
		for (int i = 0; i < columnCount; i++) {
			Cell mergedCell = titleRow.getCell(i);
			if (mergedCell == null) {
				mergedCell = titleRow.createCell(i);
			}
			mergedCell.setCellStyle(titleStyle);
		}
		
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, columnCount-1));
	}
	
	public static void writeHeaders(Workbook workbook, Sheet sheet, String... titles) {
		Font boldFont = workbook.createFont();
		boldFont.setBold(true);
		
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setFont(boldFont);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		
		Row headers = sheet.createRow(4);
		
		for (int i = 0; i < titles.length; i++) {
			Cell headerCell = headers.createCell(i);
			headerCell.setCellValue(titles[i]);
			headerCell.setCellStyle(headerStyle);
		}
	}
}
