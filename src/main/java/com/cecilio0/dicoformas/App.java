package com.cecilio0.dicoformas;

import com.cecilio0.dicoformas.controllers.*;
import com.cecilio0.dicoformas.persistence.*;
import com.cecilio0.dicoformas.services.*;
import com.cecilio0.dicoformas.utils.FileType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
		Scene scene = new Scene(loader.load());
		
		// Get controller instance and inject the services
		MainWindowController controller = loader.getController();
		
		// PurchaseProductService
		IProductService purchaseProductService = new ProductService(new PurchaseProductPersistence());
		purchaseProductService.loadProducts("./purchaseProducts.dat", FileType.DAT);
		controller.setPurchaseProductService(purchaseProductService);
		
		// PurchaseOrderService
		IPurchaseOrderService purchaseOrderService = new PurchaseOrderService(new PurchaseOrderPersistence(), purchaseProductService);
		purchaseOrderService.loadOrders("./purchaseOrders.dat", FileType.DAT);
		controller.setPurchaseOrderService(purchaseOrderService);
		
		// SaleProductService
		IProductService saleProductService = new ProductService(new SaleProductPersistence());
		saleProductService.loadProducts("./saleProducts.dat", FileType.DAT);
		controller.setSaleProductService(saleProductService);
		
		// SaleOrderService
		ISaleOrderService saleOrderService = new SaleOrderService(new SaleOrderPersistence(), saleProductService);
		saleOrderService.loadOrders("./saleOrders.dat", FileType.DAT);
		controller.setSaleOrderService(saleOrderService);
		
		// StatisticsService
		IStatisticsService statisticsService = new StatisticsService(
				new StatisticsPersistence(),
				saleOrderService,
				purchaseOrderService
		);
		controller.setStatisticsService(statisticsService);
		
		controller.setStageOnClose(primaryStage);
		
		// Set the scene and show the stage
		Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/stats-icon.jpg")));
		primaryStage.getIcons().add(icon);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("DicoStat");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}