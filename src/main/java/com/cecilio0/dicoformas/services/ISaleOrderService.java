package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.SaleOrderModel;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;
import java.util.Map;

public interface ISaleOrderService {
	
	void saveSaleOrders(String fileRoute, FileType fileType);
	
	void updateSaleOrders(String fileRoute) throws IOException;
	
	void loadSaleOrders(String fileRoute, FileType fileType) throws IOException;
	
	Map<Integer, SaleOrderModel> getSaleOrders();
}
