package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.TimePeriodType;
import com.cecilio0.dicoformas.models.WeightStatsModel;
import com.cecilio0.dicoformas.persistence.IStatisticsPersistence;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService implements IStatisticsService{
	private final ISaleOrderService saleOrderService;
	private final IPurchaseOrderService purchaseOrderService;
	private final IStatisticsPersistence statisticsPersistence;

	public StatisticsService(IStatisticsPersistence statisticsPersistence,
							 ISaleOrderService saleOrderService,
							 IPurchaseOrderService purchaseOrderService) {
		this.statisticsPersistence = statisticsPersistence;
		this.saleOrderService = saleOrderService;
		this.purchaseOrderService = purchaseOrderService;
	}
	
	@Override
	public int getNumberOfSaleOrders() {
		return saleOrderService.getOrders().size();
	}
	
	@Override
	public int getNumberOfPurchaseOrders() {
		return purchaseOrderService.getOrders().size();
	}
	
	@Override
	public double getTotalSaleOrderWeight() {
		double result = 0;
		for (Integer orderCode : saleOrderService.getOrders().keySet()) {
			result += saleOrderService.getOrders().get(orderCode).getProductOrders().stream().reduce(
					0.0,
					(subtotal, productOrder) -> subtotal + productOrder.getAmount() * productOrder.getProduct().getWeightKG(),
					Double::sum);
		}
		return result;
	}
	
	@Override
	public double getTotalPurchaseOrderWeight() {
		double result = 0;
		for (Integer orderCode : purchaseOrderService.getOrders().keySet()) {
			result += purchaseOrderService.getOrders().get(orderCode).getProductOrders().stream().reduce(
					0.0,
					(subtotal, productOrder) -> subtotal + productOrder.getAmount() * productOrder.getProduct().getWeightKG(),
					Double::sum);
		}
		return result;
	}
	
	@Override
	public Map<LocalDate, Double> getPurchaseOrderWeightByTimePeriod(TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd) {
		Map<LocalDate, Double> result = new HashMap<>();
		
		List<LocalDate> keyDates = new ArrayList<>();
		LocalDate date;
		if(timePeriodType.equals(TimePeriodType.MONTH)){
			date = LocalDate.of(periodStart.getYear(), periodStart.getMonth(), 1);
			while(date.isBefore(periodEnd)){
				keyDates.add(date);
				date = date.plusMonths(1);
			}
			
		} else {
			date = LocalDate.of(periodStart.getYear(), 1, 1);
			while(date.isBefore(periodEnd)){
				keyDates.add(date);
				date = date.plusYears(1);
			}
		}
		keyDates.add(date);
		
		for (LocalDate keyDate : keyDates) {
			double weight = purchaseOrderService.getOrders().values().stream().filter(
					purchaseOrder -> (timePeriodType.equals(TimePeriodType.YEAR) || purchaseOrder.getOrderPlacedDate().getMonth() == keyDate.getMonth())
							&& purchaseOrder.getOrderPlacedDate().getYear() == keyDate.getYear()
			).reduce(
					0.0,
					(totalWeight, purchaseOrder) -> totalWeight + purchaseOrder.getProductOrders().stream().reduce(
							0.0,
							(orderWeight, productOrder) -> orderWeight + productOrder.getAmount() * productOrder.getProduct().getWeightKG(),
							Double::sum
					),
					Double::sum);
			result.put(keyDate, weight);
		}
		
		return result;
	}
	
	@Override
	public Map<LocalDate, Double> getSaleOrderWeightByTimePeriod(TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd) {
		Map<LocalDate, Double> result = new HashMap<>();
		
		List<LocalDate> keyDates = new ArrayList<>();
		LocalDate date;
		if(timePeriodType == TimePeriodType.MONTH){
			date = LocalDate.of(periodStart.getYear(), periodStart.getMonth(), 1);
			while(date.isBefore(periodEnd)){
				keyDates.add(date);
				date = date.plusMonths(1);
			}
		} else {
			date = LocalDate.of(periodStart.getYear(), 1, 1);
			while(date.isBefore(periodEnd)){
				keyDates.add(date);
				date = date.plusYears(1);
			}
		}
		keyDates.add(date);
		
		for (LocalDate keyDate : keyDates) {
			double weight = saleOrderService.getOrders().values().stream().filter(
					saleOrder -> (timePeriodType.equals(TimePeriodType.YEAR) || saleOrder.getOrderPlacedDate().getMonth() == keyDate.getMonth())
							&& saleOrder.getOrderPlacedDate().getYear() == keyDate.getYear()
			).reduce(
					0.0,
					(totalWeight, saleOrder) -> totalWeight + saleOrder.getProductOrders().stream().reduce(
							0.0,
							(orderWeight, productOrder) -> orderWeight + productOrder.getAmount() * productOrder.getProduct().getWeightKG(),
							Double::sum
					),
					Double::sum);
			result.put(keyDate, weight);
		}
		
		return result;
	}
	
	@Override
	public void exportToExcelFile(String fileRoute, TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd) throws IOException {
		Map<LocalDate, Double> saleOrderWeightByTimePeriod = getSaleOrderWeightByTimePeriod(timePeriodType, periodStart, periodEnd);
		Map<LocalDate, Double> purchaseOrderWeightByTimePeriod = getPurchaseOrderWeightByTimePeriod(timePeriodType, periodStart, periodEnd);
		List<LocalDate> keyDates = saleOrderWeightByTimePeriod.keySet().stream().sorted().toList();
		
		List<WeightStatsModel> stats = new ArrayList<>();
		for (LocalDate keyDate : keyDates) {
			stats.add(WeightStatsModel.builder()
					.date(keyDate)
					.saleOrderWeight(saleOrderWeightByTimePeriod.get(keyDate))
					.purchaseOrderWeight(purchaseOrderWeightByTimePeriod.get(keyDate))
					.build());
		}
		
		statisticsPersistence.writeStatisticsToExcelFile(timePeriodType, fileRoute, stats);
	}
}
