package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.persistence.IPurchaseOrderPersistence;
import com.cecilio0.dicoformas.persistence.PurchaseOrderPersistence;
import com.cecilio0.dicoformas.services.PurchaseOrderService;
import com.cecilio0.dicoformas.utils.FileType;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;

public class PurchaseOrderController {
	
	@Getter
	private final PurchaseOrderService purchaseOrderService;
	
	private static PurchaseOrderController instance;
	
	private PurchaseOrderController(IPurchaseOrderPersistence purchaseOrderPersistence) {
		this.purchaseOrderService = new PurchaseOrderService(
				purchaseOrderPersistence,
				SaleProductController.getInstance().getSaleProductService());
	}
	
	public static PurchaseOrderController getInstance() {
		if(instance != null)
			return instance;
		
		IPurchaseOrderPersistence purchaseOrderPersistence = new PurchaseOrderPersistence();
		
		return instance = new PurchaseOrderController(purchaseOrderPersistence);
	}
	
	public void loadSaleOrder(String fileRoute, FileType fileType) throws IOException, ClassNotFoundException {
		purchaseOrderService.loadOrders(fileRoute, fileType);
	}
	public void saveSaleOrders(String fileRoute, FileType fileType) throws IOException {
		purchaseOrderService.saveOrders(Objects.requireNonNullElse(fileRoute, "saleOrders.dat"), fileType);
	}
	public void showSaleOrders() {
		purchaseOrderService.getOrders().forEach((number,sale) -> {
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
