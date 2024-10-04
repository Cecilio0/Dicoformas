package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.SaleOrderModel;
import com.cecilio0.dicoformas.persistence.ISaleOrderPersistence;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// todo Do this
public class SaleOrderService implements ISaleOrderService {
	
	private final ISaleOrderPersistence saleOrderPersistence;
	private final IProductService saleProductService;
	private final Map<Integer, SaleOrderModel> saleOrders;
	
	
	public SaleOrderService(ISaleOrderPersistence saleOrderPersistence, IProductService saleProductService){
		this.saleOrderPersistence = saleOrderPersistence;
		this.saleProductService = saleProductService;
		saleOrders = new HashMap<>();
	}
	
	@Override
	public Map<Integer, SaleOrderModel> getOrders() {
		return saleOrders;
	}
	
	@Override
	public void updateOrders(String fileRoute) throws IOException {
		Map<Integer, SaleOrderModel> toUpdate = saleOrderPersistence.loadSaleOrdersFromExcelFile(fileRoute, saleProductService.getProducts());
		
		if (toUpdate == null)
			return;
		
		for (Integer key : toUpdate.keySet()) {
			saleOrders.put(key, toUpdate.get(key));
		}
	}
	
	@Override
	public void saveOrders(String fileRoute, FileType fileType) throws IOException {
		if(fileType == FileType.EXCEL)
			saleOrderPersistence.saveSaleOrdersToExcelFile(saleOrders, fileRoute);
		else
			saleOrderPersistence.saveSaleOrdersToDatFile(saleOrders, fileRoute);
	}
	
	@Override
	public void loadOrders(String fileRoute, FileType fileType) throws IOException, ClassNotFoundException {
		Map<Integer, SaleOrderModel> toUpdate;
		
		if(fileType == FileType.EXCEL)
			toUpdate = saleOrderPersistence.loadSaleOrdersFromExcelFile(fileRoute, saleProductService.getProducts());
		else
			toUpdate = saleOrderPersistence.loadSaleOrdersFromDatFile(fileRoute);
		
		if (toUpdate == null)
			return;
		
		for (Integer key : toUpdate.keySet()) {
			saleOrders.put(key, toUpdate.get(key));
		}
	}
}
