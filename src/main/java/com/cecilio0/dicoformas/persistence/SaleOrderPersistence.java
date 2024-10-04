package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.ProductModel;
import com.cecilio0.dicoformas.models.ProductOrder;
import com.cecilio0.dicoformas.models.ProductType;
import com.cecilio0.dicoformas.models.SaleOrderModel;
import com.cecilio0.dicoformas.utils.DatUtil;
import com.cecilio0.dicoformas.utils.ExcelReader;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@NoArgsConstructor
public class SaleOrderPersistence implements ISaleOrderPersistence {
	
	@Override
	public Map<Integer, SaleOrderModel> loadSaleOrdersFromExcelFile(String fileRoute, Map<Integer, ProductModel> products) throws IOException {
		Workbook workbook = ExcelReader.getWorkbookFromExcelFile(fileRoute);
		
		Sheet firstSheet = workbook.getSheetAt(0);
		
		Iterator<Row> rowIterator = firstSheet.iterator();
		
		List<String> keys = new ArrayList<>();
		keys.add("PEDIDO"); // Order code
		keys.add("FECHA"); // Order date
		keys.add("CODIGO"); // Order product code
		keys.add("NOMBRE"); // In case the product code is not registered
		keys.add("DETALLE"); // In case the product has code 1200
		keys.add("CANTIDAD"); // How many of the product were bought
		keys.add("VALOR"); // Price of the product being detailed
		keys.add("FACTURA"); // Invoice of the order
		
		// Get the position for each key inside the workbook
		Map<String, Integer> keyPositions = new HashMap<>();
		while (rowIterator.hasNext()) {
			Row currentRow = rowIterator.next();
			Cell firstCell = currentRow.getCell(0);
			if (firstCell.getCellType().equals(CellType.STRING) && firstCell.getStringCellValue().equalsIgnoreCase(keys.get(0))) {
				int numberOfCells = currentRow.getPhysicalNumberOfCells();
				for (int i = 0; i < numberOfCells; i++) {
					Cell currentCell = currentRow.getCell(i);
					if (keys.contains(currentCell.getStringCellValue())) {
						keyPositions.put(currentCell.getStringCellValue(), i);
					}
				}
				break;
			}
		}
		
		Map<Integer, SaleOrderModel> saleOrders = new HashMap<>();
		Row currentRow;
		while (rowIterator.hasNext() &&
				!Objects.equals((currentRow = rowIterator.next()).getCell(keyPositions.get("PEDIDO")).getStringCellValue(), "")){
			
			Integer orderCode = Integer.parseInt(currentRow.getCell(keyPositions.get("PEDIDO")).getStringCellValue().trim());
			SaleOrderModel saleOrder;
			if((saleOrder = saleOrders.get(orderCode)) == null){
				long date = (long) currentRow.getCell(keyPositions.get("FECHA")).getNumericCellValue();
				saleOrder = SaleOrderModel.builder()
						.code(orderCode)
						.orderPlacedDate(LocalDate.ofEpochDay(date-25569))
						.invoice((int) currentRow.getCell(keyPositions.get("FACTURA")).getNumericCellValue())
						.productOrders(new ArrayList<>())
						.build();
			}
			
			// "FLETES" are products built by a different company, so they are not registered in the system
			if(currentRow.getCell(keyPositions.get("CODIGO")).getStringCellValue().trim().equals("FLETES"))
				continue;
			
			Integer productCode = Integer.parseInt(currentRow.getCell(keyPositions.get("CODIGO")).getStringCellValue().trim());
			
			String detail = currentRow.getCell(keyPositions.get("DETALLE")).getStringCellValue().trim();
			double weightKG = 0;
			// todo Implement exception for no parenthesis found, not sure if this actually has to be done
			if(detail.indexOf('(') != -1 && detail.indexOf(')') != -1){
				String substring = detail.substring(detail.lastIndexOf('('), detail.lastIndexOf(')'));
				if(NumberUtils.isCreatable(substring))
					weightKG = Double.parseDouble(substring);
			}
			
			// If the product found is a custom product then register it as such
			if (productCode != 1200){
				if(products.get(productCode) == null){
					products.put(productCode, ProductModel.builder()
							.type(ProductType.SALE)
							.code(productCode)
							.weightKG(weightKG)
							.name(currentRow.getCell(keyPositions.get("NOMBRE")).getStringCellValue().trim())
							.price(currentRow.getCell(keyPositions.get("VALOR")).getNumericCellValue())
							.build());
				}
				
				saleOrder.getProductOrders()
						.add(new ProductOrder(
								products.get(productCode),
								(int) currentRow.getCell(keyPositions.get("CANTIDAD")).getNumericCellValue()));
			} else {
				saleOrder.getProductOrders()
						.add(new ProductOrder(
								ProductModel.builder()
										.type(ProductType.SALE)
										.code(1200)
										.weightKG(weightKG)
										.name(currentRow.getCell(keyPositions.get("NOMBRE")).getStringCellValue().trim())
										.price(currentRow.getCell(keyPositions.get("VALOR")).getNumericCellValue())
										.build(),
								(int) currentRow.getCell(keyPositions.get("CANTIDAD")).getNumericCellValue()));
			}
			
			saleOrders.put(orderCode, saleOrder);
		}
		
		return saleOrders;
	}
	
	@Override
	public Map<Integer, SaleOrderModel> loadSaleOrdersFromDatFile(String fileRoute) throws IOException, ClassNotFoundException {
		return (Map<Integer, SaleOrderModel>) DatUtil.readObjectFromFile(fileRoute);
	}
	
	@Override
	public void saveSaleOrdersToDatFile(Map<Integer, SaleOrderModel> saleOrders, String fileRoute) throws IOException {
		DatUtil.writeObjectToFile(saleOrders, fileRoute);
	}
	
	// todo Complete this implementation
	@Override
	public void saveSaleOrdersToExcelFile(Map<Integer, SaleOrderModel> saleOrders, String fileRoute) {
	
	}
}
