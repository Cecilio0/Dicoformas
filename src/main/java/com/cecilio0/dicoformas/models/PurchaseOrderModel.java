package com.cecilio0.dicoformas.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

// todo Do this with one order class if possible without issues
@Data
@Builder
public class PurchaseOrderModel implements Serializable {
	@Serial
	private static final long serialVersionUID = 2L;
	private Integer code;
	private List<ProductOrder> products;
	private Date orderPlacedDate;
}
