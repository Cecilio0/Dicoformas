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
import java.text.DecimalFormat;
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
			periodStartChoiceBox.setValue(LocalDate.now().minusYears(1).plusMonths(1).withDayOfMonth(1));
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
			
			if (file == null)
				return;
			
			Alert alert;
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
			alert.showAndWait();
			displayLineChart();
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
			if (file == null)
				return;
			
			Alert alert;
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
			alert.showAndWait();
			displayLineChart();
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
			
			if (file == null)
				return;
			
			Alert alert;
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
			alert.showAndWait();
			displayLineChart();
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
			
			if (file == null)
				return;
			
			Alert alert;
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
			alert.showAndWait();
			displayLineChart();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar pedidos PT");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
		}
	}
	
	@FXML
	private void exportToExcel(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Exportar a Excel");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx", "*.xls")
			);
			
			String currentDir = System.getProperty("user.home");
			File initialDir = new File(currentDir, "Documents");
			
			if (initialDir.exists()) {
				fileChooser.setInitialDirectory(initialDir);
			}
			
			File file = fileChooser.showSaveDialog(null);
			
			if (file == null)
				return;
			
			if (!file.getPath().endsWith(".xlsx"))
				file = new File(file.getPath() + ".xlsx");
			
			Alert alert;
			try {
				TimePeriodType timePeriodType = timePeriodTypeChoiceBox.getValue().equals("Meses") ? TimePeriodType.MONTH : TimePeriodType.YEAR;
				statisticsService.exportToExcelFile(file.getAbsolutePath(), timePeriodType, periodStartChoiceBox.getValue(), periodEndChoiceBox.getValue());
				
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Exportar a Excel");
				alert.setHeaderText(null);
				alert.setContentText("Datos exportados correctamente.");
				
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().open(file);
					} catch (Exception e) {
						e.printStackTrace();
						alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Error al abrir Excel");
						alert.setHeaderText(null);
						alert.setContentText("Error al intentar abrir el archivo en Excel.");
						alert.showAndWait();
					}
				}
			} catch (Exception e) {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al exportar a Excel");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al exportar a Excel");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			
			alert.showAndWait();
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
		List<String> keyStrings = keys.stream().map( key -> key.toString().substring(0, timePeriodType == TimePeriodType.MONTH? 7: 4)).toList();
		
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
		DecimalFormat df = new DecimalFormat("#,###.00");
		for (XYChart.Data<String, Number> data : saleOrderSeries.getData()) {
			String formattedValue = df.format(data.getYValue().doubleValue());
			Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nPeso: " + formattedValue + " Kg");
			Tooltip.install(data.getNode(), tooltip);
		}
		
		for (XYChart.Data<String, Number> data : purchaseOrderSeries.getData()) {
			String formattedValue = df.format(data.getYValue().doubleValue());
			Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nPeso: " + formattedValue + " Kg");
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
