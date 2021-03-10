package com.gft.teste.presenter;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InventoryPresenter {
	
	private BigDecimal price;
	private Long amount;

}
