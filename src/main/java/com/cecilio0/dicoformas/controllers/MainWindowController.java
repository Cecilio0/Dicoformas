package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.services.IProductService;
import com.cecilio0.dicoformas.services.IPurchaseOrderService;
import com.cecilio0.dicoformas.services.ISaleOrderService;
import com.cecilio0.dicoformas.utils.FileType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;

public class MainWindowController {
	
	private IProductService purchaseProductService;
	
	private IPurchaseOrderService purchaseOrderService;
	
	private IProductService saleProductService;
	
	private ISaleOrderService saleOrderService;
	
	@FXML
	private LineChart<Number, Number> lineChart;
	
	private Stage stage;
	
	////////////////////////////// SERVICES //////////////////////////////
	public void setPurchaseProductService(IProductService service) {
		this.purchaseProductService = service;
	}
	
	public void setPurchaseOrderService(IPurchaseOrderService service) {
		this.purchaseOrderService = service;
	}
	
	public void setSaleProductService(IProductService service) {
		this.saleProductService = service;
	}
	
	public void setSaleOrderService(ISaleOrderService service) {
		this.saleOrderService = service;
	}
	
	////////////////////////////// MISC //////////////////////////////
	
	public void initialize() {
		displayLineChart();
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
		
		stage.setOnCloseRequest(event -> {
			saveAllData();
		});
	}
	
	////////////////////////////// MENU  //////////////////////////////
	
	//////////////// MENU - ARCHIVO //////////////////
	
	@FXML
	private void loadPurchaseProducts(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Abrir archivo de MP");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx", "*.xls"),
					new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
			);
			
			String currentDir = System.getProperty("user.dir");
			File initialDir = new File(currentDir);
			
			if (initialDir.exists()) {
				fileChooser.setInitialDirectory(initialDir);
			}
			
			File file = fileChooser.showOpenDialog(null);
			
			Alert alert;
			if (file != null) {
				purchaseProductService.loadProducts(file.getAbsolutePath(), FileType.EXCEL);
				
				purchaseProductService.saveProducts("./purchaseProducts.dat", FileType.DAT);
				
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Carga de productos de MP");
				alert.setHeaderText(null);
				alert.setContentText("Productos de MP cargados correctamente. Se cargaron " + purchaseProductService.getProducts().size() + " productos.");
				
				alert.showAndWait();
			} else {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al cargar productos de MP");
				alert.setHeaderText(null);
				alert.setContentText("No se ha seleccionado ningún archivo.");
				
				alert.showAndWait();
			}
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar productos de MP");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
		}
	}
	
	@FXML
	private void loadPurchaseOrders(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Abrir archivo de Compras");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx", "*.xls"),
					new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
			);
			
			String currentDir = System.getProperty("user.dir");
			File initialDir = new File(currentDir);
			
			if (initialDir.exists()) {
				fileChooser.setInitialDirectory(initialDir);
			}
			
			File file = fileChooser.showOpenDialog(null);
			Alert alert;
			if (file != null) {
				 purchaseOrderService.loadOrders(file.getAbsolutePath(), FileType.EXCEL);
				 
				 purchaseOrderService.saveOrders("./purchaseOrders.dat", FileType.DAT);
				
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Carga de compras");
				alert.setHeaderText(null);
				alert.setContentText("Compras cargadas correctamente. Se cargaron " + purchaseOrderService.getOrders().size() + " compras.");
				
			} else {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al cargar compras");
				alert.setHeaderText(null);
				alert.setContentText("No se ha seleccionado ningún archivo.");
				
			}
			alert.showAndWait();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar compras");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
		}
	}
	
	@FXML
	private void loadSaleProducts(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Abrir archivo de PT");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx", "*.xls"),
					new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
			);
			
			String currentDir = System.getProperty("user.dir");
			File initialDir = new File(currentDir);
			
			if (initialDir.exists()) {
				fileChooser.setInitialDirectory(initialDir);
			}
			
			File file = fileChooser.showOpenDialog(null);
			
			Alert alert;
			if (file != null) {
				saleProductService.loadProducts(file.getAbsolutePath(), FileType.EXCEL);
				
				saleProductService.saveProducts("./saleProducts.dat", FileType.DAT);
				
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Carga de productos de PT");
				alert.setHeaderText(null);
				alert.setContentText("Productos de PT cargados correctamente. Se cargaron " + saleProductService.getProducts().size() + " productos.");
				
				alert.showAndWait();
			} else {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al cargar productos de PT");
				alert.setHeaderText(null);
				alert.setContentText("No se ha seleccionado ningún archivo.");
				
				alert.showAndWait();
			}
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar productos de PT");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
		}
	}
	
	@FXML
	private void loadSaleOrders(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Abrir archivo de Pedidos");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx", "*.xls"),
					new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
			);
			
			String currentDir = System.getProperty("user.dir");
			File initialDir = new File(currentDir);
			
			if (initialDir.exists()) {
				fileChooser.setInitialDirectory(initialDir);
			}
			
			File file = fileChooser.showOpenDialog(null);
			
			Alert alert;
			if (file != null) {
				saleOrderService.loadOrders(file.getAbsolutePath(), FileType.EXCEL);
				
				saleOrderService.saveOrders("./saleOrders.dat", FileType.DAT);
				
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Carga de pedidos");
				alert.setHeaderText(null);
				alert.setContentText("Pedidos cargados correctamente. Se cargaron " + saleOrderService.getOrders().size() + " pedidos.");
				
				alert.showAndWait();
			} else {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al cargar pedidos");
				alert.setHeaderText(null);
				alert.setContentText("No se ha seleccionado ningún archivo.");
				
				alert.showAndWait();
			}
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar pedidos");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
		}
	}
	
	@FXML
	private void closeApp(ActionEvent event) {
		saveAllData();
		System.exit(0);
	}
	
	//////////////// MENU - AYUDA //////////////////
	@FXML
	private void openRepository(ActionEvent event) {
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI("https://github.com/Cecilio0/Dicoformas"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	////////////////////////////// CONTENT //////////////////////////////
	
	// Method to create and display the LineChart
	private void displayLineChart() {
		// Clear any existing data in the chart
		lineChart.getData().clear();
		
		lineChart.getXAxis().setLabel("Months");
		lineChart.getYAxis().setLabel("Orders");
		
		// Create the first dataset for Sales Orders
		XYChart.Series<Number, Number> salesOrdersSeries = new XYChart.Series<>();
		salesOrdersSeries.setName("Sales Orders");
		
		// Mock data for Sales Orders
		salesOrdersSeries.getData().add(new XYChart.Data<>(1, 50));
		salesOrdersSeries.getData().add(new XYChart.Data<>(2, 80));
		salesOrdersSeries.getData().add(new XYChart.Data<>(3, 45));
		salesOrdersSeries.getData().add(new XYChart.Data<>(4, 90));
		salesOrdersSeries.getData().add(new XYChart.Data<>(5, 60));
		
		// Create the second dataset for Purchase Orders
		XYChart.Series<Number, Number> purchaseOrdersSeries = new XYChart.Series<>();
		purchaseOrdersSeries.setName("Purchase Orders");
		
		// Mock data for Purchase Orders
		purchaseOrdersSeries.getData().add(new XYChart.Data<>(1, 30));
		purchaseOrdersSeries.getData().add(new XYChart.Data<>(2, 60));
		purchaseOrdersSeries.getData().add(new XYChart.Data<>(3, 35));
		purchaseOrdersSeries.getData().add(new XYChart.Data<>(4, 70));
		purchaseOrdersSeries.getData().add(new XYChart.Data<>(5, 55));
		
		// Add both datasets to the existing LineChart
		lineChart.getData().addAll(salesOrdersSeries, purchaseOrdersSeries);
	}
	
	
	@FXML
	private void showChart(ActionEvent event) {
		displayLineChart();  // This will show the chart when the button is clicked
	}
	
	////////////////////////////// UTILS //////////////////////////////
	
	private void saveAllData() {
		try {
			purchaseProductService.saveProducts("./purchaseProducts.dat", FileType.DAT);
			purchaseOrderService.saveOrders("./purchaseOrders.dat", FileType.DAT);
			saleProductService.saveProducts("./saleProducts.dat", FileType.DAT);
			saleOrderService.saveOrders("./saleOrders.dat", FileType.DAT);
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al guardar los datos");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			
			alert.showAndWait();
		}
	}
}
