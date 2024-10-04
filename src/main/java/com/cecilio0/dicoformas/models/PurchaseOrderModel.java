package com.cecilio0.dicoformas.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PurchaseOrderModel implements Serializable {
	@Serial
	private static final long serialVersionUID = 2L;
	private Integer code;
	private Integer invoice;
	private List<ProductOrder> productOrders;
	private LocalDate orderPlacedDate;
}
