package com.gft.teste.service;

import java.util.List;

import com.gft.teste.presenter.ProductDistributionPresenter;

public interface InventoryService {
	
	public List<ProductDistributionPresenter> calculateDistribution(String product, Long storeAmmount);

}
