package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.persistence.IProductPersistence;
import com.cecilio0.dicoformas.persistence.PurchaseProductPersistence;
import com.cecilio0.dicoformas.services.IProductService;
import com.cecilio0.dicoformas.services.ProductService;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;
import java.util.Objects;

public class PurchaseProductController {
	
	private final IProductService purchaseProductService;
	
	private static PurchaseProductController instance;
	
	private PurchaseProductController(IProductPersistence purchaseProductPersistence) {
		this.purchaseProductService = new ProductService(purchaseProductPersistence);
	}
	
	public static PurchaseProductController getInstance() {
		if(instance != null)
			return instance;
		
		IProductPersistence purchaseProductPersistence = new PurchaseProductPersistence();
		
		return instance = new PurchaseProductController(purchaseProductPersistence);
	}
	
	public void loadPurchaseProducts(String fileRoute, FileType fileType) throws IOException, ClassNotFoundException {
		purchaseProductService.loadProducts(fileRoute, fileType);
	}
	public void savePurchaseProducts(String fileRoute, FileType fileType) throws IOException {
		purchaseProductService.saveProducts(Objects.requireNonNullElse(fileRoute, "purchaseProducts.dat"), fileType);
	}
	public void showPurchaseProducts() throws IOException {
		purchaseProductService.getProducts().forEach((number,product) -> {
			System.out.println(product.getCode() + ": " + product.getName());
		});
	}
}
