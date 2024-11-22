package com.cecilio0.dicoformas.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class MonthInventoryModel implements Serializable {
	@Serial
	private static final long serialVersionUID = 5L;
	private LocalDate date;
	private Map<Integer, Integer> productAmounts; // Each productAmount has an integer corresponding to the amount of the product in stock
}
