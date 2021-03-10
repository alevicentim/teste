package com.gft.teste.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.gft.teste.controller.InventoryController;
import com.gft.teste.exception.NotFoundException;
import com.gft.teste.presenter.ProductDistributionPresenter;
import com.gft.teste.service.InventoryService;

@RestController
public class InventoryControllerImpl implements InventoryController {
	
	@Autowired
	private InventoryService inventoryService;

	@Override
	public List<ProductDistributionPresenter> calculateDistribution(String product, Long storeAmmount) {
		List<ProductDistributionPresenter> distribution = inventoryService.calculateDistribution(product, storeAmmount);
		if (distribution.isEmpty()) {
			throw new NotFoundException("Produto '" + product + "' não encontrado.");
		}
		
		return distribution;
	}

}
