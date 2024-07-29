package com.cecilio0.dicoformas;

import com.cecilio0.dicoformas.controllers.Manager;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		Manager manager = Manager.getInstance();

//		manager.loadSaleProducts("C:/Users/drone/Documents/Work/Base de datos PT y MP.xlsx", FileType.EXCEL);
//		manager.loadSaleProducts("saleProducts.dat", FileType.DAT);
		
		manager.loadSaleOrder("C:/Users/drone/Documents/Work/Detalle de Pedidos.xlsx", FileType.EXCEL);
		
		manager.showSaleOrders();
		manager.saveSaleOrders("saleOrders.dat", FileType.DAT);
	}
}