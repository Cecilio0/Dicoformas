package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.persistence.ISaleOrderPersistence;
import com.cecilio0.dicoformas.persistence.SaleOrderPersistence;
import com.cecilio0.dicoformas.services.SaleOrderService;
import com.cecilio0.dicoformas.utils.FileType;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;

public class SaleOrderController {
	
	@Getter
	private final SaleOrderService saleOrderService;
	
	private static SaleOrderController instance;
	
	private SaleOrderController(ISaleOrderPersistence saleOrderPersistence) {
		this.saleOrderService = new SaleOrderService(
				saleOrderPersistence,
				SaleProductController.getInstance().getSaleProductService());
	}
	
	public static SaleOrderController getInstance() {
		if(instance != null)
			return instance;
		
		ISaleOrderPersistence saleOrderPersistence = new SaleOrderPersistence();
		
		return instance = new SaleOrderController(saleOrderPersistence);
	}
	
	public void loadSaleOrder(String fileRoute, FileType fileType) throws IOException, ClassNotFoundException {
		saleOrderService.loadSaleOrders(fileRoute, fileType);
	}
	public void saveSaleOrders(String fileRoute, FileType fileType) throws IOException {
		saleOrderService.saveSaleOrders(Objects.requireNonNullElse(fileRoute, "saleOrders.dat"), fileType);
	}
	public void showSaleOrders() {
		saleOrderService.getSaleOrders().forEach((number,sale) -> {
			System.out.println(sale.getCode() + ": ");
			System.out.println("\t" + sale.getOrderPlacedDate());
			System.out.println("\tProduct orders: ");
			sale.getProductOrders().forEach((productOrder) -> {
				System.out.println("\t\t" + productOrder.getProduct().getName() + ": " + productOrder.getAmount());
			});
			System.out.println();
		});
	}
	
}
