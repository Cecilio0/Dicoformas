package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.ProductModel;
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
	private final IProductService saleProductService;
	private final ISaleOrderService saleOrderService;
	private final IProductService purchaseProductService;
	private final IPurchaseOrderService purchaseOrderService;
	private final IMonthInventoryService monthInventoryService;
	private final IStatisticsPersistence statisticsPersistence;

	public StatisticsService(IStatisticsPersistence statisticsPersistence,
							 IProductService saleProductService,
							 ISaleOrderService saleOrderService,
							 IProductService purchaseProductService,
							 IPurchaseOrderService purchaseOrderService,
							 IMonthInventoryService monthInventoryService) {
		this.statisticsPersistence = statisticsPersistence;
		this.saleProductService = saleProductService;
		this.saleOrderService = saleOrderService;
		this.purchaseProductService = purchaseProductService;
		this.purchaseOrderService = purchaseOrderService;
		this.monthInventoryService = monthInventoryService;
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
		
		List<LocalDate> keyDates = getKeyDates(timePeriodType, periodStart, periodEnd);
		
		Map<Integer, ProductModel> products = purchaseProductService.getProducts();
		for (LocalDate keyDate : keyDates) {
			double weight = purchaseOrderService.getOrders().values().stream().filter(
					purchaseOrder -> (timePeriodType.equals(TimePeriodType.YEAR) || purchaseOrder.getOrderPlacedDate().getMonth() == keyDate.getMonth())
							&& purchaseOrder.getOrderPlacedDate().getYear() == keyDate.getYear()
			).reduce(
					0.0,
					(totalWeight, purchaseOrder) -> totalWeight + purchaseOrder.getProductOrders().stream().reduce(
							0.0,
							(orderWeight, productOrder) -> {
								if(productOrder.getProduct().getCode() != 1200)
									return orderWeight + productOrder.getAmount() * products.get(productOrder.getProduct().getCode()).getWeightKG();
								else
									return orderWeight + productOrder.getAmount() * productOrder.getProduct().getWeightKG();
							},
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
		
		List<LocalDate> keyDates = getKeyDates(timePeriodType, periodStart, periodEnd);
		
		Map<Integer, ProductModel> products = saleProductService.getProducts();
		for (LocalDate keyDate : keyDates) {
			double weight = saleOrderService.getOrders().values().stream().filter(
					saleOrder -> (timePeriodType.equals(TimePeriodType.YEAR) || saleOrder.getOrderPlacedDate().getMonth() == keyDate.getMonth())
							&& saleOrder.getOrderPlacedDate().getYear() == keyDate.getYear()
			).reduce(
					0.0,
					(totalWeight, saleOrder) -> totalWeight + saleOrder.getProductOrders().stream().reduce(
							0.0,
							(orderWeight, productOrder) -> {
								if(productOrder.getProduct().getCode() != 1200)
									return orderWeight + productOrder.getAmount() * products.get(productOrder.getProduct().getCode()).getWeightKG();
								else
									return orderWeight + productOrder.getAmount() * productOrder.getProduct().getWeightKG();
							},
							Double::sum
					),
					Double::sum);
			result.put(keyDate, weight);
		}
		
		return result;
	}
	
	@Override
	public Map<LocalDate, Double> getMonthlyInventoryWeightByTimePeriod(TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd) {
		Map<LocalDate, Double> result = new HashMap<>();
		
		List<LocalDate> keyDates = getKeyDates(timePeriodType, periodStart, periodEnd);
		
		for(LocalDate keyDate : keyDates){
			double weight = monthInventoryService.getMonthInventories().values().stream().filter(
					monthInventory -> (timePeriodType.equals(TimePeriodType.YEAR) || monthInventory.getDate().getMonth() == keyDate.getMonth())
							&& monthInventory.getDate().getYear() == keyDate.getYear()
			).reduce(
					0.0,
					(totalWeight, monthInventory) -> totalWeight + monthInventory.getProductAmounts().entrySet().stream().reduce(
							0.0,
							(subtotal, productAmount) -> {
								double productWeight = 0;
								if(saleProductService.getProducts().containsKey(productAmount.getKey()))
									productWeight = saleProductService.getProducts().get(productAmount.getKey()).getWeightKG();
								else if(purchaseProductService.getProducts().containsKey(productAmount.getKey()))
									productWeight = purchaseProductService.getProducts().get(productAmount.getKey()).getWeightKG();
								return subtotal + productAmount.getValue() * productWeight;
							},
							Double::sum),
					Double::sum);
			result.put(keyDate, weight);
		}
		
		return result;
	}
	
	@Override
	public void exportWeightsByMonthToExcelFile(String fileRoute, TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd) throws IOException {
		Map<LocalDate, Double> saleOrderWeightByTimePeriod = getSaleOrderWeightByTimePeriod(timePeriodType, periodStart, periodEnd);
		Map<LocalDate, Double> purchaseOrderWeightByTimePeriod = getPurchaseOrderWeightByTimePeriod(timePeriodType, periodStart, periodEnd);
		Map<LocalDate, Double> monthlyInventoryWeightByTimePeriod = getMonthlyInventoryWeightByTimePeriod(timePeriodType, periodStart, periodEnd);
		List<LocalDate> keyDates = saleOrderWeightByTimePeriod.keySet().stream().sorted().toList();
		
		List<WeightStatsModel> stats = new ArrayList<>();
		for (LocalDate keyDate : keyDates) {
			stats.add(WeightStatsModel.builder()
					.date(keyDate)
					.saleOrderWeight(saleOrderWeightByTimePeriod.get(keyDate))
					.purchaseOrderWeight(purchaseOrderWeightByTimePeriod.get(keyDate))
					.monthInventoryWeight(monthlyInventoryWeightByTimePeriod.get(keyDate))
					.build());
		}
		
		statisticsPersistence.writeWeightsByMonthToExcelFile(timePeriodType, fileRoute, stats);
	}
	
	// todo: implement this
	@Override
	public void exportWeightsByProductByMonthToExcelFile(String fileRoute, TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd) throws IOException {
	
	}
	
	private List<LocalDate> getKeyDates(TimePeriodType timePeriodType, LocalDate periodStart, LocalDate periodEnd){
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
		return keyDates;
	}
}
