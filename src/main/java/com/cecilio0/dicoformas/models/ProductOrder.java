package com.cecilio0.dicoformas.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class ProductOrder implements Serializable {
	@Serial
	private static final long serialVersionUID = 4L;
	private ProductModel product;
	private int amount;
}
