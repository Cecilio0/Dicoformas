package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.*;
import com.cecilio0.dicoformas.utils.DatUtil;
import com.cecilio0.dicoformas.utils.ExcelReader;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class PurchaseOrderPersistence implements IPurchaseOrderPersistence {
	@Override
	public Map<Integer, PurchaseOrderModel> loadPurchaseOrdersFromExcelFile(String fileRoute, Map<Integer, ProductModel> products) throws IOException {
		Workbook workbook = ExcelReader.getWorkbookFromExcelFile(fileRoute);
		
		Sheet firstSheet = workbook.getSheetAt(0);
		
		Iterator<Row> rowIterator = firstSheet.iterator();
		
		List<String> keys = new ArrayList<>();
		keys.add("NUMERO"); // Order code
		keys.add("FACTURA"); // Invoice of the order
		keys.add("FECHA"); // Order date
		keys.add("CODIGO"); // Order product code
		keys.add("NOMBRE"); // In case the product code is not registered
		keys.add("DETALLE"); // In case the product has code 1200
		keys.add("CANTIDAD"); // How many of the product were bought
		keys.add("VALOR"); // Price of the product being detailed
		
		// Get the position for each key inside the workbook
		Map<String, Integer> keyPositions = new HashMap<>();
		while (rowIterator.hasNext()) {
			Row currentRow = rowIterator.next();
			Cell orderCodeCell = currentRow.getCell(4);
			if (orderCodeCell.getCellType().equals(CellType.STRING) && orderCodeCell.getStringCellValue().equalsIgnoreCase(keys.get(0))) {
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
		
		Map<Integer, PurchaseOrderModel> purchaseOrders = new HashMap<>();
		Row currentRow;
		while (rowIterator.hasNext() &&
				!Objects.equals((currentRow = rowIterator.next()).getCell(keyPositions.get("NUMERO")).getStringCellValue(), "")){
			
			Integer orderCode = Integer.parseInt(currentRow.getCell(keyPositions.get("NUMERO")).getStringCellValue().trim());
			PurchaseOrderModel purchaseOrder;
			if((purchaseOrder = purchaseOrders.get(orderCode)) == null){
				long date = (long) currentRow.getCell(keyPositions.get("FECHA")).getNumericCellValue();
				purchaseOrder = PurchaseOrderModel.builder()
						.code(orderCode)
						.orderPlacedDate(LocalDate.ofEpochDay(date-25569))
						.invoice(Integer.parseInt(currentRow.getCell(keyPositions.get("FACTURA")).getStringCellValue().trim()))
						.productOrders(new ArrayList<>())
						.build();
			}
			
			// "FLETES" are products built by a different company, so they are not registered in the system
			// "ANTICIPO" is a payment made in advance, so it is not a product
			String tempCode = currentRow.getCell(keyPositions.get("CODIGO")).getStringCellValue().trim();
			if(tempCode.equals("FLETES") || tempCode.equals("ANTICIPO"))
				continue;
			
			Integer productCode = Integer.parseInt(tempCode);
			
			// If the product found is a custom product then register it as such
			if (productCode != 1200 && productCode != 3086){
				if(products.get(productCode) == null){
					products.put(productCode, ProductModel.builder()
							.type(ProductType.PURCHASE)
							.code(productCode)
							.weightKG(0.0)
							.name(currentRow.getCell(keyPositions.get("NOMBRE")).getStringCellValue().trim())
							.price(currentRow.getCell(keyPositions.get("VALOR")).getNumericCellValue())
							.build());
				}
				
				purchaseOrder.getProductOrders()
						.add(new ProductOrder(
								products.get(productCode),
								(int) currentRow.getCell(keyPositions.get("CANTIDAD")).getNumericCellValue()));
			} else {
				purchaseOrder.getProductOrders()
						.add(new ProductOrder(
								ProductModel.builder()
										.type(ProductType.SALE)
										.code(productCode)
										.weightKG(0.0)
										.name(currentRow.getCell(keyPositions.get("NOMBRE")).getStringCellValue().trim())
										.price(currentRow.getCell(keyPositions.get("VALOR")).getNumericCellValue())
										.build(),
								(int) currentRow.getCell(keyPositions.get("CANTIDAD")).getNumericCellValue()));
			}
			
			purchaseOrders.put(orderCode, purchaseOrder);
		}
		
		return purchaseOrders;
	}
	
	@Override
	public Map<Integer, PurchaseOrderModel> loadPurchaseOrdersFromDatFile(String fileRoute) throws IOException, ClassNotFoundException {
		return (Map<Integer, PurchaseOrderModel>) DatUtil.readObjectFromFile(fileRoute);
	}
	
	@Override
	public void savePurchaseOrdersToDatFile(Map<Integer, PurchaseOrderModel> purchaseOrders, String fileRoute) throws IOException {
		DatUtil.writeObjectToFile(purchaseOrders, fileRoute);
	}
	
	@Override
	public void savePurchaseOrdersToExcelFile(Map<Integer, PurchaseOrderModel> purchaseOrders, String fileRoute) {
	
	}
}
