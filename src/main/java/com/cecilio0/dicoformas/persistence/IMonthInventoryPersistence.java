package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.MonthInventoryModel;
import com.cecilio0.dicoformas.utils.DatUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public interface IMonthInventoryPersistence {
	MonthInventoryModel loadMonthInventoryFromExcelFile(String fileRoute) throws IOException;
	
	default Map<LocalDate, MonthInventoryModel> loadMonthOrdersFromDatFile(String fileRoute) throws IOException, ClassNotFoundException {
		return (Map<LocalDate, MonthInventoryModel>) DatUtil.readObjectFromFile(fileRoute);
	}
	
	default void saveMonthOrdersToDatFile(Map<LocalDate, MonthInventoryModel> monthOrders, String fileRoute) throws IOException {
		DatUtil.writeObjectToFile(monthOrders, fileRoute);
	};
}
