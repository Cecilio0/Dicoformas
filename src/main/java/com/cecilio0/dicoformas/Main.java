package com.cecilio0.dicoformas;

import com.cecilio0.dicoformas.controllers.*;
import com.cecilio0.dicoformas.models.ProductModel;
import com.cecilio0.dicoformas.persistence.IPurchaseOrderPersistence;
import com.cecilio0.dicoformas.persistence.PurchaseOrderPersistence;
import com.cecilio0.dicoformas.persistence.PurchaseProductPersistence;
import com.cecilio0.dicoformas.utils.FileType;
import com.cecilio0.dicoformas.utils.XMLReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.Element;

import java.util.HashMap;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
		Scene scene = new Scene(loader.load());
		
		// Get controller instance and inject the service
		MainWindowController controller = loader.getController();
		controller.setSaleProductService(SaleProductController.getInstance().getSaleProductService());
		controller.setPurchaseProductService(PurchaseProductController.getInstance().getPurchaseProductService());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("DicoStat");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		// Controllers should be created in the following order:
		// SaleProductController, PurchaseProductController, SaleOrderController, StatisticsController
		
		try {
			SaleProductController saleProductController = SaleProductController.getInstance();
			saleProductController.loadSaleProducts("C:/Users/drone/Documents/Work/Base de datos PT y MP.xlsx", FileType.EXCEL);
//			saleProductController.loadSaleProducts("saleProducts.dat", FileType.DAT);
//			saleProductController.showSaleProducts();

			PurchaseProductController purchaseProductController = PurchaseProductController.getInstance();
			purchaseProductController.loadPurchaseProducts("C:/Users/drone/Documents/Work/Base de datos PT y MP.xlsx", FileType.EXCEL);
//			purchaseProductController.showPurchaseProducts();

			SaleOrderController saleOrderController = SaleOrderController.getInstance();
			saleOrderController.loadSaleOrder("C:/Users/drone/Documents/Work/Detalle de Pedidos.xlsx", FileType.EXCEL);
//			saleOrderController.showSaleOrders();
//			saleOrderController.saveSaleOrders("./../saleOrders.dat", FileType.DAT);
			
			IPurchaseOrderPersistence purchaseOrderPersistence = new PurchaseOrderPersistence();
			purchaseOrderPersistence.loadPurchaseOrdersFromExcelFile("C:/Users/drone/Documents/Work/new data/Detalle de Compras MP.xlsx", new HashMap<Integer, ProductModel>());

			StatisticsController statisticsController = StatisticsController.getInstance();
//			statisticsController.showStatistics();

			Element node = XMLReader.readXML("dialogs/mainWindow.xml");
			System.out.println(node.getElementsByTagName("title").item(0).getTextContent());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		launch(args);
	}
}