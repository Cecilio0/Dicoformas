package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.persistence.*;
import com.cecilio0.dicoformas.services.*;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;
import java.util.Objects;

public class Manager {
	
	// todo Implement all dependencies
	private static Manager instance;
	
	// Product related
	private final IProductService saleProductService;
	private final IProductService purchaseProductService;
	
	// Order related
	private final ISaleOrderService saleOrderService;
	
	// Stats related
	private final IStatisticsService statisticsService;
	
	private Manager(IProductPersistence saleProductPersistence,
					IProductPersistence purchaseProductPersistence,
					ISaleOrderPersistence saleOrderPersistence) {
		
		saleProductService = new ProductService(saleProductPersistence);
		purchaseProductService = new ProductService(purchaseProductPersistence);
		
		saleOrderService = new SaleOrderService(saleOrderPersistence, saleProductService);
		
		statisticsService = new StatisticsService(saleOrderService);
	}
	
	public static Manager getInstance() {
		if(instance != null)
			return instance;
		
		IProductPersistence saleProductPersistence = new SaleProductPersistence();
		IProductPersistence purchaseProductPersistence = new PurchaseProductPersistence();
		
		ISaleOrderPersistence saleOrderPersistence = new SaleOrderPersistence();
		
		return instance = new Manager(saleProductPersistence, purchaseProductPersistence, saleOrderPersistence);
	}
	
	// Sale Product related
	public void loadSaleProducts(String fileRoute, FileType fileType) throws IOException {
		saleProductService.loadProducts(fileRoute, fileType);
	}
	public void saveSaleProducts(String fileRoute, FileType fileType) {
		saleProductService.saveProducts(Objects.requireNonNullElse(fileRoute, "saleProducts.dat"), fileType);
	}
	public void showSaleProducts() {
		saleProductService.getProducts().forEach((number,product) -> {
			System.out.println(product.getCode() + ": " + product.getName());
		});
	}
	
	// Purchase Product related
	public void loadPurchaseProducts(String fileRoute, FileType fileType) throws IOException {
		purchaseProductService.loadProducts(fileRoute, fileType);
	}
	public void savePurchaseProducts(String fileRoute, FileType fileType) {
		purchaseProductService.saveProducts(Objects.requireNonNullElse(fileRoute, "purchaseProducts.dat"), fileType);
	}
	public void showPurchaseProducts(String excelRoute) throws IOException {
		purchaseProductService.updateProducts(excelRoute);
		purchaseProductService.getProducts().forEach((number,product) -> {
			System.out.println(product.getCode() + ": " + product.getName());
		});
	}
	
	// Sale Order related
	public void loadSaleOrder(String fileRoute, FileType fileType) throws IOException {
		saleOrderService.loadSaleOrders(fileRoute, fileType);
	}
	public void saveSaleOrders(String fileRoute, FileType fileType) {
		saleOrderService.saveSaleOrders(Objects.requireNonNullElse(fileRoute, "saleOrders.dat"), fileType);
	}
	public void showSaleOrders() {
		saleOrderService.getSaleOrders().forEach((number,sale) -> {
			System.out.println(sale.getCode() + ": " + sale.getOrderPlacedDate());
		});
	}
	
	
	
	
}
