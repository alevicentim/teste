package com.gft.teste.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.gft.teste.controller.InventoryController;
import com.gft.teste.exception.NotFoundException;
import com.gft.teste.presenter.ProductDistributionPresenter;
import com.gft.teste.service.InventoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class InventoryControllerImpl implements InventoryController {
	
	@Autowired
	private InventoryService inventoryService;

	@Override
	public List<ProductDistributionPresenter> calculateDistribution(String product, Long storeAmmount) {
		log.debug("Iniciando cálculo da distribuição do produto {} para {} loja(s)", product, storeAmmount);
		
		List<ProductDistributionPresenter> distribution = inventoryService.calculateDistribution(product, storeAmmount);
		if (distribution.isEmpty()) {
			log.debug("Finalizando cálculo da distribuição do produto {} para {} loja(s) com nenhum produto encontrado", product, storeAmmount);
			throw new NotFoundException("Produto '" + product + "' não encontrado");
		}
		
		log.debug("Finalizando cálculo da distribuição do produto {} para {} loja(s) com a seguinte distribuição: {}", product, storeAmmount, distribution);
		return distribution;
	}

}
