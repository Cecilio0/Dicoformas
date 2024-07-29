package com.cecilio0.dicoformas.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class ProductModel implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private Integer code;
	private String name;
	private Double weightKG;
	private Double price;
	private ProductType type;
}
