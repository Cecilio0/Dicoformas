package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.ProductsSoldByMonthModel;
import com.cecilio0.dicoformas.models.TimePeriodType;
import com.cecilio0.dicoformas.models.WeightStatsModel;
import com.cecilio0.dicoformas.utils.ExcelUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class StatisticsPersistence implements IStatisticsPersistence {
	@Override
	public void writeWeightsByMonthToExcelFile(TimePeriodType timePeriodType, String fileRoute, List<WeightStatsModel> stats) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = workbook.createSheet("Pesos por Fecha");
		String title = "PESOS PT Y MP POR " + (timePeriodType.equals(TimePeriodType.MONTH) ? "MES" : "AÑO");
		
		int columnCount = 5;
		
		ExcelUtil.writeTitles(workbook, sheet, title, new byte[]{(byte) 242, (byte) 206, (byte) 239}, columnCount);
		
		ExcelUtil.writeHeaders(workbook, sheet, "FECHA", "PESO PT", "PESO MP", "PESO INVENTARIO", "PESO PT + PESO INVENTARIO");
		
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
			
			Cell inventoryWeightCell = row.createCell(3);
			inventoryWeightCell.setCellValue(stat.getMonthInventoryWeight());
			inventoryWeightCell.setCellStyle(dataStyle);
			
			Cell saleOrderPlusInventoryWeightCell = row.createCell(4);
			saleOrderPlusInventoryWeightCell.setCellFormula("B" + index + "+D" + index);
			saleOrderPlusInventoryWeightCell.setCellStyle(dataStyle);
		}
		
		// Resize columns
		for (int i = 0; i < columnCount; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 200);
		}
		
		// Write workbook to file
		FileOutputStream fileOut = new FileOutputStream(fileRoute);
		workbook.write(fileOut);
		workbook.close();
	}
	
	@Override
	public void writeProductsSoldByMonthToExcelFile(TimePeriodType timePeriodType, String fileRoute, List<ProductsSoldByMonthModel> stats) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = workbook.createSheet("Pesos por Fecha");
		String title = "PESOS PT Y MP POR " + (timePeriodType.equals(TimePeriodType.MONTH) ? "MES" : "AÑO");
		
		int columnCount = 8;
		
		ExcelUtil.writeTitles(workbook, sheet, title, new byte[]{(byte) 150, (byte) 206, (byte) 239}, columnCount);
		
		ExcelUtil.writeHeaders(workbook, sheet, "FECHA", "CODIGO", "NOMBRE", "PRECIO1", "CANT.", "TOTAL", "PESO", "PESO TOTAL");
		
		// Data
		CellStyle dataStyle = workbook.createCellStyle();
		dataStyle.setBorderTop(BorderStyle.THIN);
		dataStyle.setBorderRight(BorderStyle.THIN);
		dataStyle.setBorderBottom(BorderStyle.THIN);
		dataStyle.setBorderLeft(BorderStyle.THIN);
		
		CellStyle accountingStyle = workbook.createCellStyle();
		accountingStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
		accountingStyle.setBorderTop(BorderStyle.THIN);
		accountingStyle.setBorderRight(BorderStyle.THIN);
		accountingStyle.setBorderBottom(BorderStyle.THIN);
		accountingStyle.setBorderLeft(BorderStyle.THIN);
		
		int index = 5;
		for (ProductsSoldByMonthModel productsSoldByMonth : stats) {
			String dateString = productsSoldByMonth.getDate().toString().substring(0, 7);
			for (int key : productsSoldByMonth.getProductsSold().keySet()) {
				Row row = sheet.createRow(index++);
				
				Cell dateCell = row.createCell(0);
				dateCell.setCellValue(dateString);
				dateCell.setCellStyle(dataStyle);
				
				Cell codeCell = row.createCell(1);
				codeCell.setCellValue(key);
				codeCell.setCellStyle(dataStyle);
				
				Cell salesCell = row.createCell(2);
				salesCell.setCellValue(productsSoldByMonth.getProductsSold().get(key).getProduct().getName());
				salesCell.setCellStyle(dataStyle);
				
				Cell priceCell = row.createCell(3);
				priceCell.setCellValue(productsSoldByMonth.getProductsSold().get(key).getProduct().getPrice());
				priceCell.setCellStyle(accountingStyle);
				
				Cell salesAmountCell = row.createCell(4);
				salesAmountCell.setCellValue(productsSoldByMonth.getProductsSold().get(key).getAmount());
				salesAmountCell.setCellStyle(dataStyle);
				
				Cell totalCell = row.createCell(5);
				totalCell.setCellFormula("D" + index + "*E" + index);
				totalCell.setCellStyle(accountingStyle);
				
				Cell weightCell = row.createCell(6);
				weightCell.setCellValue(productsSoldByMonth.getProductsSold().get(key).getProduct().getWeightKG());
				weightCell.setCellStyle(dataStyle);
				
				Cell totalWeightCell = row.createCell(7);
				totalWeightCell.setCellFormula("E" + index + "*G" + index);
				totalWeightCell.setCellStyle(dataStyle);
			}
		}
		
		// Resize columns
		for (int i = 0; i < columnCount; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 200);
		}
		
		// Write workbook to file
		FileOutputStream fileOut = new FileOutputStream(fileRoute);
		workbook.write(fileOut);
		workbook.close();
	}
}
