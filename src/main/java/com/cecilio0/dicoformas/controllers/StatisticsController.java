package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.services.IStatisticsService;
import com.cecilio0.dicoformas.services.StatisticsService;

public class StatisticsController {
	
	private final IStatisticsService statisticsService;
	
	private static StatisticsController instance;
	
	private StatisticsController() {
		this.statisticsService = new StatisticsService(
				SaleOrderController.getInstance().getSaleOrderService(),
				SaleProductController.getInstance().getSaleProductService(),
				PurchaseOrderController.getInstance().getPurchaseOrderService(),
				PurchaseProductController.getInstance().getPurchaseProductService());
	}
	
	public static StatisticsController getInstance() {
		if(instance != null)
			return instance;
		
		return instance = new StatisticsController();
	}
	
	public IStatisticsService getStatisticsService() {
		return statisticsService;
	}
}
