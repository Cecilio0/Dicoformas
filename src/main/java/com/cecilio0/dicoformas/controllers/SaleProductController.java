package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.persistence.IProductPersistence;
import com.cecilio0.dicoformas.persistence.SaleProductPersistence;
import com.cecilio0.dicoformas.services.IProductService;
import com.cecilio0.dicoformas.services.ProductService;
import com.cecilio0.dicoformas.utils.FileType;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;


public class SaleProductController {
	
	@Getter
	private final IProductService saleProductService;
	
	private static SaleProductController instance;
	
	private SaleProductController(IProductPersistence saleProductPersistence) {
		this.saleProductService = new ProductService(saleProductPersistence);
	}
	
	public static SaleProductController getInstance() {
		if(instance != null)
			return instance;
		
		IProductPersistence saleProductPersistence = new SaleProductPersistence();
		
		return instance = new SaleProductController(saleProductPersistence);
	}
	
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
}
