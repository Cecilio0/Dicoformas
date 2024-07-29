package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.PurchaseOrderModel;

import java.io.IOException;
import java.util.Map;

public interface IPurchaseOrderPersistence {
	Map<Integer, PurchaseOrderModel> loadPurchaseOrdersFromExcelFile(String fileRoute, Map<Integer, PurchaseOrderModel> products) throws IOException;
	
	Map<Integer, PurchaseOrderModel> loadPurchaseOrdersFromDatFile(String fileRoute);
	
	void savePurchaseOrdersToDatFile(Map<Integer, PurchaseOrderModel> purchaseOrders, String fileRoute);
	
	void savePurchaseOrdersToExcelFile(Map<Integer, PurchaseOrderModel> purchaseOrders, String fileRoute);
}
