package com.cecilio0.dicoformas;

import com.cecilio0.dicoformas.controllers.PurchaseProductController;
import com.cecilio0.dicoformas.controllers.SaleOrderController;
import com.cecilio0.dicoformas.controllers.SaleProductController;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		SaleProductController saleProductController = SaleProductController.getInstance();
		saleProductController.loadSaleProducts("C:/Users/drone/Documents/Work/Base de datos PT y MP.xlsx", FileType.EXCEL);
//		saleProductController.loadSaleProducts("saleProducts.dat", FileType.DAT);
		
		PurchaseProductController purchaseProductController = PurchaseProductController.getInstance();
		purchaseProductController.loadPurchaseProducts("C:/Users/drone/Documents/Work/Base de datos PT y MP.xlsx", FileType.EXCEL);
		
		SaleOrderController saleOrderController = SaleOrderController.getInstance();
		saleOrderController.loadSaleOrder("C:/Users/drone/Documents/Work/Detalle de Pedidos.xlsx", FileType.EXCEL);
		
		saleOrderController.showSaleOrders();
//		saleOrderController.saveSaleOrders("./../saleOrders.dat", FileType.DAT);
	}
}