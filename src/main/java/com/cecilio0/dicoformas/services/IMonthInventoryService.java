package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.MonthInventoryModel;
import com.cecilio0.dicoformas.utils.FileType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public interface IMonthInventoryService {
	void loadMonthInventory(String fileRoute, FileType fileType) throws IOException, ClassNotFoundException;
	
	void saveMonthInventory(String fileRoute) throws IOException;
	
	Map<LocalDate, MonthInventoryModel>	getMonthInventories();
}
