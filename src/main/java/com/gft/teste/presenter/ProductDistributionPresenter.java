package com.gft.teste.presenter;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ProductDistributionPresenter {
	
	private String storeName;
	private List<ProductPresenter> products;
	private Long totalAmount;
	private BigDecimal totalPrice;
	private BigDecimal avaragePrice;

}
