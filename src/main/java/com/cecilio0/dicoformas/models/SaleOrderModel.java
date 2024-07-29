package com.cecilio0.dicoformas.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class SaleOrderModel implements Serializable {
	@Serial
	private static final long serialVersionUID = 3L;
	private Integer code;
	private List<ProductOrder> products; // Each productOrder has an integer corresponding to the amount of the product bought
	private Date orderPlacedDate;
	private boolean isInvoiced;
}
