package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.ProductModel;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;
import java.util.Map;

public interface IProductService {
	
	void saveProducts(String fileRoute, FileType fileType);
	
	void updateProducts(String fileRoute) throws IOException;
	
	void loadProducts(String fileRoute, FileType fileType) throws IOException;
	
	Map<Integer, ProductModel> getProducts();
	
}
