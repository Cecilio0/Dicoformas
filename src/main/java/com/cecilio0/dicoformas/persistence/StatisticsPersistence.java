package com.cecilio0.dicoformas.persistence;

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
	public void writeWeightsByProductByMonthToExcelFile(TimePeriodType timePeriodType, String fileRoute, List<WeightStatsModel> stats) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = workbook.createSheet("Pesos por Fecha");
		String title = "PESOS PT Y MP POR " + (timePeriodType.equals(TimePeriodType.MONTH) ? "MES" : "AÑO");
		
		int columnCount = 5;
		
		ExcelUtil.writeTitles(workbook, sheet, title, new byte[]{(byte) 242, (byte) 206, (byte) 239}, columnCount);
		
		ExcelUtil.writeHeaders(workbook, sheet, "FECHA", "PESO PT", "PESO MP", "PESO INVENTARIO", "PESO PT + PESO INVENTARIO");
	}
}
