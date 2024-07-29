package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.ProductModel;
import com.cecilio0.dicoformas.models.ProductType;
import com.cecilio0.dicoformas.utils.DatUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.*;

public interface IProductPersistence {
	Sheet getSheet(String fileRoute) throws IOException;
	
	default Map<Integer, ProductModel> loadProductsFromExcelFile(String fileRoute) throws IOException{
		Sheet firstSheet = getSheet(fileRoute);
		Iterator<Row> rowIterator = firstSheet.iterator();
		
		List<String> keys = new ArrayList<>();
		keys.add("CODIGO");
		keys.add("NOMBRE");
		keys.add("PRECIO1");
		keys.add("PESO");
		
		Map<String, Integer> keyPositions = new HashMap<>();
		while (rowIterator.hasNext()){
			Row currentRow = rowIterator.next();
			Cell firstCell = currentRow.getCell(0);
			if(firstCell.getCellType().equals(CellType.STRING) && firstCell.getStringCellValue().equalsIgnoreCase(keys.get(0))){
				int numberOfCells = currentRow.getPhysicalNumberOfCells();
				for (int i = 1; i < numberOfCells; i++) {
					Cell currentCell = currentRow.getCell(i);
					if (keys.contains(currentCell.getStringCellValue())){
						keyPositions.put(currentCell.getStringCellValue(), i);
					}
				}
				break;
			}
		}
		
		Map<Integer, ProductModel> products = new HashMap<>();
		
		while (rowIterator.hasNext()){
			Row currentRow = rowIterator.next();
			Integer code = Integer.parseInt(currentRow.getCell(0).getStringCellValue().trim());
			
			products
					.put(code, ProductModel.builder()
							.code(code)
							.name(currentRow.getCell(keyPositions.get(keys.get(1))).getStringCellValue())
							.price(currentRow.getCell(keyPositions.get(keys.get(2))).getNumericCellValue())
//							.weightKG(currentRow.getCell(keyPositions.get(keys.get(3))).getNumericCellValue()) // todo add this, eventually
							.type(ProductType.SALE)
							.build());
		}
		
		return products;
	}
	
	default Map<Integer, ProductModel> loadProductsFromDatFile(String fileRoute){
		return (Map<Integer, ProductModel>) DatUtil.readObjectFromFile(fileRoute);
	}
	
	default void saveProductsToDatFile(Map<Integer, ProductModel> products, String fileRoute){
		DatUtil.writeObjectToFile(products, fileRoute);
	}
	
	void saveProductsToExcelFile(Map<Integer, ProductModel> products, String fileRoute);
}
