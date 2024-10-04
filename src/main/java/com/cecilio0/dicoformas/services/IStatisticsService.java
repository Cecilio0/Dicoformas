package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.TimePeriodType;

//TODO: Implement this interface
public interface IStatisticsService {

	int getNumberOfSaleOrders();
	
	int getNumberOfPurchaseOrders();
	
	double getTotalSaleOrderWeight();
	
	double getTotalPurchaseOrderWeight();
	
	double getPurchaseOrderWeightByTimePeriod(TimePeriodType timePeriodType, int periodStart, int periodSize);
	
	double getSaleOrderWeightByTimePeriod(TimePeriodType timePeriodType, int periodStart, int periodSize);

}
