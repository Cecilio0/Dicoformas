package com.cecilio0.dicoformas.persistence;

import com.cecilio0.dicoformas.models.ProductsSoldByMonthModel;
import com.cecilio0.dicoformas.models.TimePeriodType;
import com.cecilio0.dicoformas.models.WeightStatsModel;

import java.io.IOException;
import java.util.List;

public interface IStatisticsPersistence {
	void writeWeightsByMonthToExcelFile(TimePeriodType timePeriodType, String fileRoute, List<WeightStatsModel> stats) throws IOException;
	void writeProductsSoldByMonthToExcelFile(TimePeriodType timePeriodType, String fileRoute, List<ProductsSoldByMonthModel> stats) throws IOException;
}
