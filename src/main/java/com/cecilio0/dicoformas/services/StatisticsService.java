package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.TimePeriodType;

// todo Complete this implementation
public class StatisticsService implements IStatisticsService{
	private final IProductService saleProductService;
	private final ISaleOrderService saleOrderService;
	
	
	private final IPurchaseOrderService purchaseOrderService;
	private final IProductService purchaseProductService;

	public StatisticsService(ISaleOrderService saleOrderService,
							 IProductService saleProductService,
							 IPurchaseOrderService purchaseOrderService,
							 IProductService purchaseProductService) {
		this.saleOrderService = saleOrderService;
		this.saleProductService = saleProductService;
		this.purchaseOrderService = purchaseOrderService;
		this.purchaseProductService = purchaseProductService;
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
	
	// TODO: Implement this method
	@Override
	public double getPurchaseOrderWeightByTimePeriod(TimePeriodType timePeriodType, int periodStart, int periodSize) {
		return 0;
	}
	
	// TODO: Implement this method
	@Override
	public double getSaleOrderWeightByTimePeriod(TimePeriodType timePeriodType, int periodStart, int periodSize) {
		return 0;
	}
}
