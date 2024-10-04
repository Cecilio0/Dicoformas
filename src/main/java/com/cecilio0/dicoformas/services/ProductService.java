package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.ProductModel;
import com.cecilio0.dicoformas.persistence.IProductPersistence;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductService implements IProductService {

	private final IProductPersistence productPersistence;
	private final Map<Integer, ProductModel> products;
	
	public ProductService(IProductPersistence productPersistence) {
		this.productPersistence = productPersistence;
		products = new HashMap<>();
	}
	
	@Override
	public void saveProducts(String fileRoute, FileType fileType) throws IOException {
		if (fileType == FileType.EXCEL)
			productPersistence.saveProductsToExcelFile(products, fileRoute);
		else
			productPersistence.saveProductsToDatFile(products, fileRoute);
	}
	
	// Only from Excel files
	@Override
	public void updateProducts(String fileRoute) throws IOException {
		Map<Integer, ProductModel> toUpdate = productPersistence.loadProductsFromExcelFile(fileRoute);
		
		if (toUpdate == null)
			return;
		
		for (Integer key : toUpdate.keySet()) {
			products.put(key, toUpdate.get(key));
		}
	}
	
	@Override
	public void loadProducts(String fileRoute, FileType fileType) throws IOException, ClassNotFoundException {
		Map<Integer, ProductModel> toUpdate;
		
		if(fileType == FileType.EXCEL)
			toUpdate = productPersistence.loadProductsFromExcelFile(fileRoute);
		else
			toUpdate = productPersistence.loadProductsFromDatFile(fileRoute);
		
		if (toUpdate == null)
			return;
		
		for (Integer key : toUpdate.keySet()) {
			products.put(key, toUpdate.get(key));
		}
	}
	
	@Override
	public Map<Integer, ProductModel> getProducts() {
		return products;
	}
}
