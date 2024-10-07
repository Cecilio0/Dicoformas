package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.models.TimePeriodType;
import com.cecilio0.dicoformas.services.IProductService;
import com.cecilio0.dicoformas.services.IPurchaseOrderService;
import com.cecilio0.dicoformas.services.ISaleOrderService;
import com.cecilio0.dicoformas.services.IStatisticsService;
import com.cecilio0.dicoformas.utils.FileType;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;

public class MainWindowController {
	
	private IProductService purchaseProductService;
	
	private IPurchaseOrderService purchaseOrderService;
	
	private IProductService saleProductService;
	
	private ISaleOrderService saleOrderService;
	
	private IStatisticsService statisticsService;
	
	@FXML
	private LineChart<String, Number> lineChart;
	
	@FXML
	private ChoiceBox<String> timePeriodTypeChoiceBox;
	
	@FXML
	private DatePicker periodStartChoiceBox;
	
	@FXML
	private DatePicker periodEndChoiceBox;
	
	public void setStageOnClose(Stage stage) {
		stage.setOnCloseRequest(event -> saveAllData());
	}
	
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
	
	public void setStatisticsService(IStatisticsService service) {
		this.statisticsService = service;
		displayLineChart();
	}
	
	////////////////////////////// MISC //////////////////////////////
	
	public void initialize() {
		timePeriodTypeChoiceBox.getItems().setAll("Meses", "Años");
		timePeriodTypeChoiceBox.setValue("Meses");
		timePeriodTypeChoiceBox.valueProperty().addListener(this::onTimePeriodTypeChoiceBoxSelectionChanged);
		
		periodStartChoiceBox.setValue(LocalDate.now().minusYears(1).withDayOfMonth(1));
		periodStartChoiceBox.valueProperty().addListener(this::updateLineChart);
		
		periodEndChoiceBox.setValue(LocalDate.now().withDayOfMonth(1));
		periodEndChoiceBox.valueProperty().addListener(this::updateLineChart);
		
		lineChart.getXAxis().setTickLabelRotation(90);
	}
	
	private void updateLineChart(ObservableValue<?> observableValue, Object oldValue, Object newValue) {
		if(Objects.equals(oldValue, newValue))
			return;
		
		displayLineChart();
	}
	
	private void onTimePeriodTypeChoiceBoxSelectionChanged(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
		if(Objects.equals(oldValue, newValue))
			return;
		
		if(newValue.equals("Meses")){
			periodStartChoiceBox.setValue(LocalDate.now().minusYears(1).withDayOfMonth(1));
			periodEndChoiceBox.setValue(LocalDate.now().withDayOfMonth(1));
		} else {
			periodStartChoiceBox.setValue(LocalDate.now().minusYears(5).withMonth(1).withDayOfMonth(1));
			periodEndChoiceBox.setValue(LocalDate.now().withMonth(1).withDayOfMonth(1));
		}
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
				try {
					purchaseProductService.loadProducts(file.getAbsolutePath(), FileType.EXCEL);
					
					purchaseProductService.saveProducts("./purchaseProducts.dat", FileType.DAT);
					
					alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Carga de MP");
					alert.setHeaderText(null);
					alert.setContentText("MP cargadas correctamente. Se cargaron " + purchaseProductService.getProducts().size() + " MP.");
				} catch (Exception e) {
					alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error al cargar MP");
					alert.setHeaderText(null);
					alert.setContentText("Error al cargar MP. Por favor, revise que haya seleccionado el archivo correcto.");
				}
			} else {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al cargar MP");
				alert.setHeaderText(null);
				alert.setContentText("No se ha seleccionado ningún archivo.");
			}
			alert.showAndWait();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar MP");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
		}
	}
	
	@FXML
	private void loadPurchaseOrders(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Abrir archivo de Compras MP");
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
				try {
					purchaseOrderService.loadOrders(file.getAbsolutePath(), FileType.EXCEL);
					purchaseOrderService.saveOrders("./purchaseOrders.dat", FileType.DAT);
					
					alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Carga de compras MP");
					alert.setHeaderText(null);
					alert.setContentText("Compras cargadas correctamente. Se cargaron " + purchaseOrderService.getOrders().size() + " compras.");
				} catch (Exception e) {
					alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error al cargar compras MP.");
					alert.setHeaderText(null);
					alert.setContentText("Error al cargar compras de MP. Por favor, revise que haya seleccionado el archivo correcto.");
				}
			} else {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al cargar compras MP");
				alert.setHeaderText(null);
				alert.setContentText("No se ha seleccionado ningún archivo.");
			}
			alert.showAndWait();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar compras MP");
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
				try {
					saleProductService.loadProducts(file.getAbsolutePath(), FileType.EXCEL);
					
					saleProductService.saveProducts("./saleProducts.dat", FileType.DAT);
					
					alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Carga de PT");
					alert.setHeaderText(null);
					alert.setContentText("PT cargados correctamente. Se cargaron " + saleProductService.getProducts().size() + " PT.");
				} catch (Exception e) {
					alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error al cargar PT");
					alert.setHeaderText(null);
					alert.setContentText("Error al cargar PT. Por favor, revise que haya seleccionado el archivo correcto.");
				}
			} else {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al cargar PT");
				alert.setHeaderText(null);
				alert.setContentText("No se ha seleccionado ningún archivo.");
			}
			alert.showAndWait();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar PT");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
		}
	}
	
	@FXML
	private void loadSaleOrders(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Abrir archivo de Pedidos PT");
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
				try {
					saleOrderService.loadOrders(file.getAbsolutePath(), FileType.EXCEL);
					
					saleOrderService.saveOrders("./saleOrders.dat", FileType.DAT);
					
					alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Carga de pedidos PT");
					alert.setHeaderText(null);
					alert.setContentText("Pedidos PT cargados correctamente. Se cargaron " + saleOrderService.getOrders().size() + " pedidos.");
				} catch (Exception e) {
					alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error al cargar pedidos PT");
					alert.setHeaderText(null);
					alert.setContentText("Error al cargar pedidos PT. Por favor, revise que haya seleccionado el archivo correcto.");
				}
			} else {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al cargar pedidos PT");
				alert.setHeaderText(null);
				alert.setContentText("No se ha seleccionado ningún archivo.");
			}
			alert.showAndWait();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar pedidos PT");
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
		Map<LocalDate, Double> saleOrderData, purchaseOrderData;
		TimePeriodType timePeriodType = timePeriodTypeChoiceBox.getValue().equals("Meses") ? TimePeriodType.MONTH : TimePeriodType.YEAR;
		saleOrderData = statisticsService.getSaleOrderWeightByTimePeriod(timePeriodType, periodStartChoiceBox.getValue(), periodEndChoiceBox.getValue());
		purchaseOrderData = statisticsService.getPurchaseOrderWeightByTimePeriod(timePeriodType, periodStartChoiceBox.getValue(), periodEndChoiceBox.getValue());
		
		// Clear any existing data in the chart
		lineChart.getData().clear();
		
		lineChart.getXAxis().setLabel(timePeriodTypeChoiceBox.getValue());
		lineChart.getYAxis().setLabel("Peso (Kg)");
		
		// Create the first dataset for Sale Orders
		XYChart.Series<String, Number> saleOrderSeries = new XYChart.Series<>();
		saleOrderSeries.setName("Ventas PT");
		
		List<LocalDate> keys = saleOrderData.keySet().stream().sorted().toList();
		List<String> keyStrings = keys.stream().map( key -> key.toString().substring(0, timePeriodType == TimePeriodType.MONTH? 7: 5)).toList();
		
		for (int i = 0; i < keys.size(); i++) {
			saleOrderSeries.getData().add(new XYChart.Data<>(keyStrings.get(i), saleOrderData.get(keys.get(i))));
		}
		
		// Create the first dataset for Purchase Orders
		XYChart.Series<String, Number> purchaseOrderSeries = new XYChart.Series<>();
		purchaseOrderSeries.setName("Compras MP");
		
		for (int i = 0; i < keys.size(); i++) {
			purchaseOrderSeries.getData().add(new XYChart.Data<>(keyStrings.get(i), purchaseOrderData.get(keys.get(i))));
		}
		
		// Update the X Axis with the new keys
		CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
		xAxis.getCategories().clear();
		xAxis.getCategories().addAll(keyStrings);
		
		// Add both datasets to the existing LineChart
		lineChart.getData().addAll(saleOrderSeries, purchaseOrderSeries);
		
		// Add tooltips to the data points
		for (XYChart.Data<String, Number> data : saleOrderSeries.getData()) {
			Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nPeso: " + String.format("%.1f", data.getYValue().floatValue()) + " Kg");
			Tooltip.install(data.getNode(), tooltip);
		}
		
		for (XYChart.Data<String, Number> data : purchaseOrderSeries.getData()) {
			Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nPeso: " + String.format("%.1f", data.getYValue().floatValue()) + " Kg");
			Tooltip.install(data.getNode(), tooltip);
		}
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
