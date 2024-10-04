package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.PurchaseOrderModel;
import com.cecilio0.dicoformas.persistence.IPurchaseOrderPersistence;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PurchaseOrderService implements IPurchaseOrderService {
	
	private final IPurchaseOrderPersistence purchaseOrderPersistence;
	private final IProductService purchaseProductService;
	private final Map<Integer, PurchaseOrderModel> purchaseOrders;
	
	public PurchaseOrderService(IPurchaseOrderPersistence purchaseOrderPersistence, IProductService purchaseProductService){
		this.purchaseOrderPersistence = purchaseOrderPersistence;
		this.purchaseProductService = purchaseProductService;
		purchaseOrders = new HashMap<>();
	}
	
	@Override
	public void saveOrders(String fileRoute, FileType fileType) throws IOException {
		if(fileType == FileType.EXCEL)
			purchaseOrderPersistence.savePurchaseOrdersToExcelFile(purchaseOrders, fileRoute);
		else
			purchaseOrderPersistence.savePurchaseOrdersToDatFile(purchaseOrders, fileRoute);
	}
	
	@Override
	public void updateOrders(String fileRoute) throws IOException {
		Map<Integer, PurchaseOrderModel> toUpdate = purchaseOrderPersistence.loadPurchaseOrdersFromExcelFile(fileRoute, purchaseProductService.getProducts());
		
		for (Integer key : toUpdate.keySet()) {
			purchaseOrders.put(key, toUpdate.get(key));
		}
	}
	
	@Override
	public void loadOrders(String fileRoute, FileType fileType) throws IOException, ClassNotFoundException {
		Map<Integer, PurchaseOrderModel> toUpdate;
		
		if(fileType == FileType.EXCEL)
			toUpdate = purchaseOrderPersistence.loadPurchaseOrdersFromExcelFile(fileRoute, purchaseProductService.getProducts());
		else
			toUpdate = purchaseOrderPersistence.loadPurchaseOrdersFromDatFile(fileRoute);
		
		for (Integer key : toUpdate.keySet()) {
			purchaseOrders.put(key, toUpdate.get(key));
		}
	}
	
	@Override
	public Map<Integer, PurchaseOrderModel> getOrders() {
		return purchaseOrders;
	}
}
