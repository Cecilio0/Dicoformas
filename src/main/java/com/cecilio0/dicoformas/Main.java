package com.cecilio0.dicoformas;

import com.cecilio0.dicoformas.controllers.*;
import com.cecilio0.dicoformas.utils.FileType;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		// Controllers should be created in the following order:
		// SaleProductController, PurchaseProductController, SaleOrderController, StatisticsController
		
		try {
			SaleProductController saleProductController = SaleProductController.getInstance();
			saleProductController.loadSaleProducts("C:/Users/drone/Documents/Work/Base de datos PT y MP.xlsx", FileType.EXCEL);
//			saleProductController.loadSaleProducts("saleProducts.dat", FileType.DAT);
			saleProductController.showSaleProducts();
			
			PurchaseProductController purchaseProductController = PurchaseProductController.getInstance();
			purchaseProductController.loadPurchaseProducts("C:/Users/drone/Documents/Work/Base de datos PT y MP.xlsx", FileType.EXCEL);
			purchaseProductController.showPurchaseProducts();
			
			SaleOrderController saleOrderController = SaleOrderController.getInstance();
			saleOrderController.loadSaleOrder("C:/Users/drone/Documents/Work/Detalle de Pedidos.xlsx", FileType.EXCEL);

			saleOrderController.showSaleOrders();
//			saleOrderController.saveSaleOrders("./../saleOrders.dat", FileType.DAT);
			
			StatisticsController statisticsController = StatisticsController.getInstance();
//			statisticsController.showStatistics();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("MainWindow");
			frame.setContentPane(new MainWindowController().getMainPanel());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		});
	}
}