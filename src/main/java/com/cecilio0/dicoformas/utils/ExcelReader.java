package com.cecilio0.dicoformas.utils;

import com.cecilio0.dicoformas.exceptions.FileFormatNotSupportedException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class ExcelReader {
	
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
}
