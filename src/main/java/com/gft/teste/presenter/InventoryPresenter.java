package com.gft.teste.presenter;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class InventoryPresenter {
	
	private BigDecimal price;
	private Long amount;

}
