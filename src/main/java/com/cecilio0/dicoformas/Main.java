package com.cecilio0.dicoformas;

import com.cecilio0.dicoformas.controllers.*;
import com.cecilio0.dicoformas.utils.FileType;
import com.cecilio0.dicoformas.utils.XMLReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.w3c.dom.Element;

import java.util.Objects;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
		Scene scene = new Scene(loader.load());
		
		// Get controller instance and inject the services
		MainWindowController controller = loader.getController();
		controller.setSaleProductService(SaleProductController.getInstance().getSaleProductService());
		controller.setSaleOrderService(SaleOrderController.getInstance().getSaleOrderService());
		controller.setPurchaseProductService(PurchaseProductController.getInstance().getPurchaseProductService());
		controller.setPurchaseOrderService(PurchaseOrderController.getInstance().getPurchaseOrderService());
		controller.setStatisticsService(StatisticsController.getInstance().getStatisticsService());
		controller.setStageOnClose(primaryStage);
		
		// Set the scene and show the stage
		Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/stats-icon.jpg")));
		primaryStage.getIcons().add(icon);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("DicoStat");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		// Controllers should be created in the following order:
		// SaleProductController, PurchaseProductController, SaleOrderController, StatisticsController
		
		try {
			SaleProductController saleProductController = SaleProductController.getInstance();
//			saleProductController.loadSaleProducts("C:/Users/drone/Documents/Work/new data/Base de datos PT y MP.xlsx", FileType.EXCEL);
			saleProductController.loadSaleProducts("./saleProducts.dat", FileType.DAT);
//			saleProductController.loadSaleProducts("saleProducts.dat", FileType.DAT);
//			saleProductController.showSaleProducts();

			PurchaseProductController purchaseProductController = PurchaseProductController.getInstance();
//			purchaseProductController.loadPurchaseProducts("C:/Users/drone/Documents/Work/new data/Base de datos PT y MP.xlsx", FileType.EXCEL);
			purchaseProductController.loadPurchaseProducts("./purchaseProducts.dat", FileType.DAT);
//			purchaseProductController.showPurchaseProducts();

			SaleOrderController saleOrderController = SaleOrderController.getInstance();
//			saleOrderController.loadSaleOrder("C:/Users/drone/Documents/Work/new data/Detalle Pedidos.xlsx", FileType.EXCEL);
			saleOrderController.loadSaleOrder("./saleOrders.dat", FileType.DAT);
//			saleOrderController.showSaleOrders();
//			saleOrderController.saveSaleOrders("./../saleOrders.dat", FileType.DAT);
			
			PurchaseOrderController purchaseOrderController = PurchaseOrderController.getInstance();
//			purchaseOrderController.loadPurchaseOrder("C:/Users/drone/Documents/Work/new data/Detalle de Compras MP.xlsx", FileType.EXCEL);
			purchaseOrderController.loadPurchaseOrder("./purchaseOrders.dat", FileType.DAT);
//			purchaseOrderController.showSaleOrders();
//			purchaseOrderController.saveSaleOrders("./../purchaseOrders.dat", FileType.DAT);
			
			StatisticsController statisticsController = StatisticsController.getInstance();

//			Element node = XMLReader.readXML("dialogs/mainWindow.xml");
//			System.out.println(node.getElementsByTagName("title").item(0).getTextContent());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		launch(args);
	}
}