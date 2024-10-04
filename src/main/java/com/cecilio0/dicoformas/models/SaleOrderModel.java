package com.cecilio0.dicoformas.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SaleOrderModel implements Serializable {
	@Serial
	private static final long serialVersionUID = 3L;
	private Integer code;
	private List<ProductOrder> productOrders; // Each productOrder has an integer corresponding to the amount of the product bought
	private LocalDate orderPlacedDate;
	private Integer invoice;
}
