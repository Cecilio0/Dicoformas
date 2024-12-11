package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.models.TimePeriodType;
import com.cecilio0.dicoformas.services.*;
import com.cecilio0.dicoformas.utils.FileType;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Setter;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainWindowController {
	
	@Setter
	private IProductService purchaseProductService;
	
	@Setter
	private IPurchaseOrderService purchaseOrderService;
	
	@Setter
	private IProductService saleProductService;
	
	@Setter
	private ISaleOrderService saleOrderService;
	
	@Setter
	private IMonthInventoryService monthInventoryService;
	
	@Setter
	private IStatisticsService statisticsService;
	
	@FXML
	private BarChart<String, Number> barChart;
	
	@FXML
	private ChoiceBox<String> timePeriodTypeChoiceBox;
	
	@FXML
	private DatePicker periodStartChoiceBox;
	
	@FXML
	private DatePicker periodEndChoiceBox;
	
	@FXML
	private CheckBox saleOrderCheckBox;
	
	@FXML
	private CheckBox purchaseOrderCheckBox;
	
	@FXML
	private CheckBox inventoryCheckBox;
	
	@FXML
	private CheckBox purchaseOrderMinusSaleOrderCheckBox;
	
	@FXML
	private CheckBox calculatedInventoryCheckBox;
	
	public void setStageOnClose(Stage stage) {
		stage.setOnCloseRequest(event -> saveAllData());
	}
	
	////////////////////////////// MISC //////////////////////////////
	
	public void initialize() {
		// ChoiceBox
		timePeriodTypeChoiceBox.getItems().setAll("Meses", "Años");
		timePeriodTypeChoiceBox.setValue("Meses");
		timePeriodTypeChoiceBox.valueProperty().addListener(this::onTimePeriodTypeChoiceBoxSelectionChanged);

		// todo: somehow add a check to not allow periodStart to be later than periodEnd
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		StringConverter<LocalDate> localDateConverter = new StringConverter<>() {
			@Override
			public String toString(LocalDate localDate) {
				// Convert LocalDate to String with custom format
				return (localDate != null) ? localDate.format(dateFormatter) : "";
			}
			
			@Override
			public LocalDate fromString(String string) {
				// Convert String to LocalDate, handling parsing
				try {
					return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
				} catch (RuntimeException e) {
					// todo: make this return a default date
					return null; // Return null if parsing fails
				}
			}
		};
		
		periodStartChoiceBox.setConverter(localDateConverter);
		periodStartChoiceBox.setValue(LocalDate.now().minusYears(1).plusMonths(1).withDayOfMonth(1));
		periodStartChoiceBox.valueProperty().addListener(this::updateLineChart);
		
		periodEndChoiceBox.setConverter(localDateConverter);
		periodEndChoiceBox.setValue(LocalDate.now().withDayOfMonth(1));
		periodEndChoiceBox.valueProperty().addListener(this::updateLineChart);
		
		// Checkboxes
		saleOrderCheckBox.setSelected(true);
		purchaseOrderCheckBox.setSelected(true);
		inventoryCheckBox.setSelected(true);
		purchaseOrderMinusSaleOrderCheckBox.setSelected(true);
		calculatedInventoryCheckBox.setSelected(true);
		
		saleOrderCheckBox.selectedProperty().addListener(this::updateLineChart);
		purchaseOrderCheckBox.selectedProperty().addListener(this::updateLineChart);
		inventoryCheckBox.selectedProperty().addListener(this::updateLineChart);
		purchaseOrderMinusSaleOrderCheckBox.selectedProperty().addListener(this::updateLineChart);
		calculatedInventoryCheckBox.selectedProperty().addListener(this::updateLineChart);
		
		barChart.getXAxis().setTickLabelRotation(90);
	}
	
	private void updateLineChart(ObservableValue<?> observableValue, Object oldValue, Object newValue) {
		if(Objects.equals(oldValue, newValue))
			return;
		
		displayBarChart();
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
			File file = chooseFile("Abrir archivo de MP", true, true);
			
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
			displayBarChart();
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
			File file = chooseFile("Abrir archivo de Compras MP", true, true);
			
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
			displayBarChart();
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
			File file = chooseFile("Abrir archivo de PT", true, true);
			
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
			displayBarChart();
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
			File file = chooseFile("Abrir archivo de Pedidos PT", true, true);
			
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
			displayBarChart();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar pedidos PT");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
		}
	}
	
	@FXML
	private void loadMonthInventory(ActionEvent event) {
		try {
			File file = chooseFile("Abrir archivo de Inventario", true, true);
			
			if (file == null)
				return;
			
			Alert alert;
			try {
				monthInventoryService.loadMonthInventory(file.getAbsolutePath(), FileType.EXCEL);
				
				monthInventoryService.saveMonthInventory("./monthInventories.dat");
				
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Carga de Inventarios");
				alert.setHeaderText(null);
				alert.setContentText("Inventario cargado correctamente.");
			} catch (Exception e) {
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al cargar Inventarios");
				alert.setHeaderText(null);
				alert.setContentText("Error al cargar Inventarios. Por favor, revise que haya seleccionado el archivo correcto.");
			}
			alert.showAndWait();
			displayBarChart();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al cargar Inventarios");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
		}
	}
	
	@FXML
	private void exportWeightsByMonthToExcel(ActionEvent event) {
		try {
			TimePeriodType timePeriodType = timePeriodTypeChoiceBox.getValue().equals("Meses") ? TimePeriodType.MONTH : TimePeriodType.YEAR;
			
			String title;
			if(timePeriodType == TimePeriodType.MONTH)
				title = "Exportar Pesos Por Mes a Excel";
			else
				title = "Exportar Pesos Por Año a Excel";
			
			File file = chooseFile(title, false, false);
			
			if (file == null)
				return;
			
			Alert alert;
			try {
				statisticsService.exportWeightsByMonthToExcelFile(file.getAbsolutePath(), timePeriodType, periodStartChoiceBox.getValue(), periodEndChoiceBox.getValue());
				
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Exportar a Excel");
				alert.setHeaderText(null);
				alert.setContentText("Datos exportados correctamente.");
				
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().open(file);
					} catch (Exception e) {
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
	private void exportProductsSoldByMonthToExcel(ActionEvent event) {
		try {
			TimePeriodType timePeriodType = timePeriodTypeChoiceBox.getValue().equals("Meses") ? TimePeriodType.MONTH : TimePeriodType.YEAR;
			
			String title;
			if(timePeriodType == TimePeriodType.MONTH)
				title = "Exportar Ventas Por Mes Por Producto a Excel";
			else
				title = "Exportar Ventas Por Año Por Producto a Excel";
			
			File file = chooseFile(title, false, false);
			
			if (file == null)
				return;
			
			Alert alert;
			try {
				statisticsService.exportProductsSoldByMonthToExcel(file.getAbsolutePath(), timePeriodType, periodStartChoiceBox.getValue(), periodEndChoiceBox.getValue());
				
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Exportar a Excel");
				alert.setHeaderText(null);
				alert.setContentText("Datos exportados correctamente.");
				
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().open(file);
					} catch (Exception e) {
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
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al abrir el repositorio");
			alert.setHeaderText(null);
			alert.setContentText("Error al intentar abrir el repositorio en GitHub.");
			alert.showAndWait();
		}
	}
	
	////////////////////////////// CONTENT //////////////////////////////
	
	// Method to create and display the barChart
	public void displayBarChart() {
		Map<LocalDate, Double> saleOrderData, purchaseOrderData, inventoryData;
		TimePeriodType timePeriodType = timePeriodTypeChoiceBox.getValue().equals("Meses") ? TimePeriodType.MONTH : TimePeriodType.YEAR;
		saleOrderData = statisticsService.getSaleOrderWeightByTimePeriod(timePeriodType, periodStartChoiceBox.getValue(), periodEndChoiceBox.getValue());
		purchaseOrderData = statisticsService.getPurchaseOrderWeightByTimePeriod(timePeriodType, periodStartChoiceBox.getValue(), periodEndChoiceBox.getValue());
		inventoryData = statisticsService.getMonthlyInventoryWeightByTimePeriod(timePeriodType, periodStartChoiceBox.getValue(), periodEndChoiceBox.getValue());
		
		List<XYChart.Series<String, Number>> datasets = new ArrayList<>();
		// Clear any existing data in the chart
		barChart.getData().clear();
		
		barChart.getXAxis().setLabel(timePeriodTypeChoiceBox.getValue());
		barChart.getYAxis().setLabel("Peso (Kg)");
		
		List<LocalDate> keys = saleOrderData.keySet().stream().sorted().toList();
		List<String> keyStrings = keys.stream().map( key -> key.toString().substring(0, timePeriodType == TimePeriodType.MONTH? 7: 4)).toList();
		
		XYChart.Series<String, Number> saleOrderSeries = new XYChart.Series<>();
		if(saleOrderCheckBox.selectedProperty().get()){
			// Create the first dataset for Sale Orders
			saleOrderSeries.setName("Ventas PT");
			
			for (int i = 0; i < keys.size(); i++) {
				saleOrderSeries.getData().add(new XYChart.Data<>(keyStrings.get(i), saleOrderData.get(keys.get(i))));
			}
			datasets.add(saleOrderSeries);
		}
		
		XYChart.Series<String, Number> purchaseOrderSeries = new XYChart.Series<>();
		if(purchaseOrderCheckBox.selectedProperty().get()){
			// Create the first dataset for Purchase Orders
			purchaseOrderSeries.setName("Compras MP");
			
			for (int i = 0; i < keys.size(); i++) {
				purchaseOrderSeries.getData().add(new XYChart.Data<>(keyStrings.get(i), purchaseOrderData.get(keys.get(i))));
			}
			datasets.add(purchaseOrderSeries);
		}
		
		XYChart.Series<String, Number> purchaseOrderMinusSaleOrderSeries = new XYChart.Series<>();
		if(purchaseOrderMinusSaleOrderCheckBox.selectedProperty().get()){
			// Create the first dataset for saleOrderPlusInventory
			purchaseOrderMinusSaleOrderSeries.setName("Compras-Ventas");
			
			for (int i = 0; i < keys.size(); i++) {
				purchaseOrderMinusSaleOrderSeries.getData().add(new XYChart.Data<>(keyStrings.get(i), purchaseOrderData.get(keys.get(i)) - saleOrderData.get(keys.get(i))));
			}
			datasets.add(purchaseOrderMinusSaleOrderSeries);
		}
		
		XYChart.Series<String, Number> inventorySeries = new XYChart.Series<>();
		if(inventoryCheckBox.selectedProperty().get()){
			// Create the first dataset for Inventory
			inventorySeries.setName("Inventario");
			
			for (int i = 0; i < keys.size(); i++) {
				inventorySeries.getData().add(new XYChart.Data<>(keyStrings.get(i), inventoryData.get(keys.get(i))));
			}
			datasets.add(inventorySeries);
		}
		
		XYChart.Series<String, Number> calculatedInventorySeries = new XYChart.Series<>();
		if(calculatedInventoryCheckBox.selectedProperty().get()){
			// Create the first dataset for calculatedInventory
			calculatedInventorySeries.setName("Inv. Calculado");
			LocalDate inventoryKey;
			for (int i = 0; i < keys.size(); i++) {
				inventoryKey = keys.get(i).minusMonths(1);
				calculatedInventorySeries.getData().add(
						new XYChart.Data<>(
								keyStrings.get(i),
								purchaseOrderData.get(keys.get(i)) - saleOrderData.get(keys.get(i)) + (keys.contains(inventoryKey)? inventoryData.get(inventoryKey): 0)));
			}
			datasets.add(calculatedInventorySeries);
		}
		
		barChart.setAnimated(true);
		// Update the X Axis with the new keys
		CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
		xAxis.getCategories().clear();
		xAxis.getCategories().addAll(keyStrings);
		barChart.categoryGapProperty().bind(
				barChart.widthProperty().divide(keyStrings.size()).divide(7)
		);
		
		// Add all existing datasets to the existing barChart
		barChart.getData().addAll(datasets);
		
		// Add tooltips to the data points
		DecimalFormat df = new DecimalFormat("#,###");
		
		if(saleOrderCheckBox.selectedProperty().get()){
			for (XYChart.Data<String, Number> data : saleOrderSeries.getData()) {
				String formattedValue = df.format(data.getYValue().doubleValue());
				Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nPeso: " + formattedValue + " Kg");
				Tooltip.install(data.getNode(), tooltip);
			}
		}
		
		if(purchaseOrderCheckBox.selectedProperty().get()){
			for (XYChart.Data<String, Number> data : purchaseOrderSeries.getData()) {
				String formattedValue = df.format(data.getYValue().doubleValue());
				Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nPeso: " + formattedValue + " Kg");
				Tooltip.install(data.getNode(), tooltip);
			}
		}
		
		if(purchaseOrderMinusSaleOrderCheckBox.selectedProperty().get()){
			for (XYChart.Data<String, Number> data : purchaseOrderMinusSaleOrderSeries.getData()) {
				String formattedValue = df.format(data.getYValue().doubleValue());
				Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nPeso: " + formattedValue + " Kg");
				Tooltip.install(data.getNode(), tooltip);
			}
		}
		
		if(inventoryCheckBox.selectedProperty().get()){
			for (XYChart.Data<String, Number> data : inventorySeries.getData()) {
				String formattedValue = df.format(data.getYValue().doubleValue());
				Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nPeso: " + formattedValue + " Kg");
				Tooltip.install(data.getNode(), tooltip);
			}
		}
		
		if(calculatedInventoryCheckBox.selectedProperty().get()){
			for (XYChart.Data<String, Number> data : calculatedInventorySeries.getData()) {
				String formattedValue = df.format(data.getYValue().doubleValue());
				Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nPeso: " + formattedValue + " Kg");
				Tooltip.install(data.getNode(), tooltip);
			}
		}
		
		barChart.setAnimated(false);
	}
	
	////////////////////////////// UTILS //////////////////////////////
	
	private void saveAllData() {
		try {
			purchaseProductService.saveProducts("./purchaseProducts.dat", FileType.DAT);
			purchaseOrderService.saveOrders("./purchaseOrders.dat", FileType.DAT);
			saleProductService.saveProducts("./saleProducts.dat", FileType.DAT);
			saleOrderService.saveOrders("./saleOrders.dat", FileType.DAT);
			monthInventoryService.saveMonthInventory("./monthInventories.dat");
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error al guardar los datos");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			
			alert.showAndWait();
		}
	}
	
	private File chooseFile(String popupTitle, boolean includeAll, boolean open){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(popupTitle);
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx", "*.xls")
		);
		
		if(includeAll)
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
		
		String currentDir = System.getProperty("user.home");
		File initialDir = new File(currentDir, "Documents");
		
		if (initialDir.exists()) {
			fileChooser.setInitialDirectory(initialDir);
		}
		
		File file;
		
		// Check if we want to open or save a file
		if(open)
			file = fileChooser.showOpenDialog(null);
		else
			file = fileChooser.showSaveDialog(null);
		
		if (file == null)
			return null;
		
		if (!file.getPath().endsWith(".xlsx") && !file.getPath().endsWith(".xls"))
			file = new File(file.getPath() + ".xlsx");
		
		return file;
	}
}
