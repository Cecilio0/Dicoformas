package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.SaleOrderModel;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;
import java.util.Map;

public interface ISaleOrderService {
	
	void saveOrders(String fileRoute, FileType fileType) throws IOException;
	
	void updateOrders(String fileRoute) throws IOException;
	
	void loadOrders(String fileRoute, FileType fileType) throws IOException, ClassNotFoundException;
	
	Map<Integer, SaleOrderModel> getOrders();
}
