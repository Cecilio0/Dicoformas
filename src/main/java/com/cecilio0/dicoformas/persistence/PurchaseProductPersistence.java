package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.ProductModel;
import com.cecilio0.dicoformas.utils.ExcelReader;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.*;

@NoArgsConstructor
public class PurchaseProductPersistence implements IProductPersistence {
	@Override
	public Sheet getSheet(String fileRoute) throws IOException {
		Workbook workbook = ExcelReader.getWorkbookFromExcelFile(fileRoute);
		
		Sheet firstSheet = workbook.getSheet("MATERIA PRIMA");
		if(firstSheet == null) {
			firstSheet = workbook.getSheetAt(0);
		}
		
		return firstSheet;
	}
	
	// todo Complete this implementation
	@Override
	public void saveProductsToExcelFile(Map<Integer, ProductModel> products, String fileRoute) {
	
	}
}
