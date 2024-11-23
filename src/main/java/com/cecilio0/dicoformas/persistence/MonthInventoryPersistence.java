package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.MonthInventoryModel;
import com.cecilio0.dicoformas.utils.ExcelUtil;
import com.cecilio0.dicoformas.utils.Month;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class MonthInventoryPersistence implements IMonthInventoryPersistence {
	@Override
	public MonthInventoryModel loadMonthInventoryFromExcelFile(String fileRoute) throws IOException {
		Workbook workbook = ExcelUtil.getWorkbookFromExcelFile(fileRoute);
		
		Sheet firstSheet = workbook.getSheetAt(0);
		
		Iterator<Row> rowIterator = firstSheet.iterator();
		rowIterator.next();
		
		LocalDate date;
		if(!rowIterator.hasNext())
			throw new RuntimeException("No se pudo leer el archivo correctamente");
		
		Row currentRow = rowIterator.next();
		String[] dateStringParts = currentRow.getCell(3).getStringCellValue().split(" ");
		
		if(dateStringParts.length < 3)
			throw new RuntimeException("El formato de la fecha no es el esperado");
		
		int month = Month.getMonthNumber(dateStringParts[0]);
		if(month == -1)
			throw new RuntimeException("No fue posible extraer el mes de la fecha");
		
		int year = Integer.parseInt(dateStringParts[dateStringParts.length-1]);
		
		date = LocalDate.of(year, month, 1);
		
		rowIterator.next();
		if(!rowIterator.next().getCell(0).getStringCellValue().equals("MATERIA PRIMA"))
			throw new RuntimeException("No se pudo leer el archivo correctamente");
		
		
		Map<Integer, Integer> productAmounts = new HashMap<>();
		
		List<String> keys = new ArrayList<>();
		keys.add("Código");
		keys.add("Cant.");
		
		Map<String, Integer> keyPositions = new HashMap<>();
		
		currentRow = rowIterator.next();
		
		int numberOfCells = currentRow.getPhysicalNumberOfCells();
		for (int i = 0; i < numberOfCells; i++) {
			Cell currentCell = currentRow.getCell(i);
			if (keys.contains(currentCell.getStringCellValue())) {
				keyPositions.put(currentCell.getStringCellValue(), i);
			}
		}
		
		if (keyPositions.size() != keys.size())
			throw new IOException("The keys were not found in the excel file");
		
		while(rowIterator.hasNext()){
			currentRow = rowIterator.next();
			if(currentRow.getCell(keyPositions.get(keys.getFirst())) == null || currentRow.getCell(keyPositions.get(keys.getFirst())).getCellType() != CellType.NUMERIC)
				continue;
			
			Integer code = (int) currentRow.getCell(keyPositions.get(keys.get(0))).getNumericCellValue();
			Integer amount = (int) currentRow.getCell(keyPositions.get(keys.get(1))).getNumericCellValue();
			productAmounts.put(code, amount);
		}
		
		return MonthInventoryModel.builder()
				.date(date)
				.productAmounts(productAmounts)
				.build();
	}
}
