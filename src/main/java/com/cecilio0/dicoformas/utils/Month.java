package com.cecilio0.dicoformas.utils;

import lombok.Getter;

@Getter
public enum Month {
	JANUARY(1, "enero"),
	FEBRUARY(2, "febrero"),
	MARCH(3, "marzo"),
	APRIL(4, "abril"),
	MAY(5, "mayo"),
	JUNE(6, "junio"),
	JULY(7, "julio"),
	AUGUST(8, "agosto"),
	SEPTEMBER(9, "septiembre"),
	OCTOBER(10, "octubre"),
	NOVEMBER(11, "noviembre"),
	DECEMBER(12, "diciembre");

	private final int monthNumber;
	
	private final String monthName;

	Month(int monthNumber, String monthName) {
		this.monthNumber = monthNumber;
		this.monthName = monthName;
	}
	
	public static int getMonthNumber(String monthName) {
		for (Month month : Month.values()) {
			if (month.getMonthName().equalsIgnoreCase(monthName)) {
				return month.getMonthNumber();
			}
		}
		return -1;
	}
}
