package com.cecilio0.dicoformas.services;

import com.cecilio0.dicoformas.models.MonthInventoryModel;
import com.cecilio0.dicoformas.persistence.IMonthInventoryPersistence;
import com.cecilio0.dicoformas.utils.FileType;
import lombok.Getter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MonthInventoryService implements IMonthInventoryService {
	
	private final IMonthInventoryPersistence monthInventoryPersistence;
	@Getter
	private final Map<LocalDate, MonthInventoryModel> monthInventories;
	
	public MonthInventoryService(IMonthInventoryPersistence monthInventoryPersistence) {
		this.monthInventoryPersistence = monthInventoryPersistence;
		this.monthInventories = new HashMap<>();
	}
	
	@Override
	public void loadMonthInventory(String fileRoute, FileType fileType) throws IOException, ClassNotFoundException {
		if (fileType == FileType.EXCEL) {
			MonthInventoryModel monthInventory = monthInventoryPersistence.loadMonthInventoryFromExcelFile(fileRoute);
			monthInventories.put(monthInventory.getDate(), monthInventory);
		} else {
			Map<LocalDate, MonthInventoryModel> toUpdate = monthInventoryPersistence.loadMonthOrdersFromDatFile(fileRoute);
			
			if (toUpdate == null)
				return;
			
			for (LocalDate key : toUpdate.keySet()) {
				monthInventories.put(key, toUpdate.get(key));
			}
		}
	}
	
	@Override
	public void saveMonthInventory(String fileRoute) throws IOException {
		monthInventoryPersistence.saveMonthOrdersToDatFile(monthInventories, fileRoute);
	}
}
