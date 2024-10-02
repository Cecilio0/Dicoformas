package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.services.IProductService;
import com.cecilio0.dicoformas.utils.FileType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;

public class MainWindowController {
	
	private IProductService purchaseProductService;
	
	private IProductService saleProductService;
	
	@FXML
	private Label label;
	
	////////////////////////////// SERVICES //////////////////////////////
	public void setPurchaseProductService(IProductService service) {
		this.purchaseProductService = service;
	}
	
	public void setSaleProductService(IProductService service) {
		this.saleProductService = service;
	}
	
	
	////////////////////////////// MENU  //////////////////////////////
	
	//////////////// MENU - ARCHIVO //////////////////\
	
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
			if (file != null) {
				purchaseProductService.loadProducts(file.getAbsolutePath(), FileType.EXCEL);
			}
			
			label.setText("Productos de MP cargados correctamente. Se cargaron " + purchaseProductService.getProducts().size() + " productos.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void closeApp(ActionEvent event) {
		// TODO: save data before closing
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
}
