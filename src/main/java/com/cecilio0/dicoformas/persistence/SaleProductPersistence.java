package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.ProductModel;
import com.cecilio0.dicoformas.utils.ExcelReader;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.*;


public class SaleProductPersistence implements IProductPersistence {
	@Override
	public Sheet getSheet(String fileRoute) throws IOException {
		Workbook workbook = ExcelReader.getWorkbookFromExcelFile(fileRoute);
		
		Sheet firstSheet = workbook.getSheet("PRODUCTO TERMINADO");
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
