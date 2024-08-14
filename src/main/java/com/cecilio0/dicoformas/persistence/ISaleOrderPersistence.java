package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.ProductModel;
import com.cecilio0.dicoformas.models.SaleOrderModel;

import java.io.IOException;
import java.util.Map;

public interface ISaleOrderPersistence {
	
	Map<Integer, SaleOrderModel> loadSaleOrdersFromExcelFile(String fileRoute, Map<Integer, ProductModel> products) throws IOException;
	
	Map<Integer, SaleOrderModel> loadSaleOrdersFromDatFile(String fileRoute) throws IOException, ClassNotFoundException;
	
	void saveSaleOrdersToDatFile(Map<Integer, SaleOrderModel> saleOrders, String fileRoute) throws IOException;
	
	void saveSaleOrdersToExcelFile(Map<Integer, SaleOrderModel> saleOrders, String fileRoute);
}
