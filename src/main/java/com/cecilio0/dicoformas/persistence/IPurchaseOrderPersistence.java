package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.ProductModel;
import com.cecilio0.dicoformas.models.PurchaseOrderModel;

import java.io.IOException;
import java.util.Map;

public interface IPurchaseOrderPersistence {
	Map<Integer, PurchaseOrderModel> loadPurchaseOrdersFromExcelFile(String fileRoute, Map<Integer, ProductModel> products) throws IOException;
	
	Map<Integer, PurchaseOrderModel> loadPurchaseOrdersFromDatFile(String fileRoute) throws IOException, ClassNotFoundException;
	
	void savePurchaseOrdersToDatFile(Map<Integer, PurchaseOrderModel> purchaseOrders, String fileRoute) throws IOException;
	
	void savePurchaseOrdersToExcelFile(Map<Integer, PurchaseOrderModel> purchaseOrders, String fileRoute);
}
