package com.cecilio0.dicoformas.controllers;

import com.cecilio0.dicoformas.services.IProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainPageController {
	
	private IProductService service;
	
	@FXML
	private Label label;
	
	// Setter method for dependency injection
	public void setService(IProductService service) {
		this.service = service;
	}
	
	@FXML
	public void handleButtonClick() {
		if (service != null) {
			label.setText(service.getProducts().toString());
		}
	}
}
