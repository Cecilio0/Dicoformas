package com.cecilio0.dicoformas.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class ProductsSoldByMonthModel {
	private LocalDate date;
	private Map<Integer, ProductOrder> productsSold; // Each productAmount has an integer corresponding to the amount of that product that was sold
}
