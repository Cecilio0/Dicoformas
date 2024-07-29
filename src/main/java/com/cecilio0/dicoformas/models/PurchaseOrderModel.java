package com.cecilio0.dicoformas.models;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

// todo Do this with one order class if possible without issues
@Data
public class PurchaseOrderModel implements Serializable {
	@Serial
	private static final long serialVersionUID = 2L;
	private Integer code;
	private List<ProductModel> products;
	private Date orderPlacedDate;
}
