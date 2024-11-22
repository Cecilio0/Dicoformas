package com.cecilio0.dicoformas.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class WeightStatsModel {
	private LocalDate date;
	private double saleOrderWeight;
	private double purchaseOrderWeight;
	private double monthInventoryWeight;
}
