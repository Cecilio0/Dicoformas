package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.TimePeriodType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

//TODO: Implement this interface
public interface IStatisticsService {

	int getNumberOfSaleOrders();
	
	int getNumberOfPurchaseOrders();
	
	double getTotalSaleOrderWeight();
	
	double getTotalPurchaseOrderWeight();
	
	Map<LocalDate, Double> getPurchaseOrderWeightByTimePeriod(TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd);
	
	Map<LocalDate, Double> getSaleOrderWeightByTimePeriod(TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd);
	
	void exportWeightsByMonthToExcelFile(String fileRoute, TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd) throws IOException;
	
	void exportWeightsByProductByMonthToExcelFile(String fileRoute, TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd) throws IOException;
}
