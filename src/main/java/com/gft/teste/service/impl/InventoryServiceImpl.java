package com.gft.teste.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.gft.teste.presenter.InventoryPresenter;
import com.gft.teste.presenter.ProductDistributionPresenter;
import com.gft.teste.presenter.ProductPresenter;
import com.gft.teste.repository.InventoryRepository;
import com.gft.teste.service.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	private InventoryRepository inventoryRepository;

	@Override
	public List<ProductDistributionPresenter> calculateDistribution(String product, Long storeAmmount) {
		List<InventoryPresenter> inventories = inventoryRepository.findProductGroupedByPrice(product);
		if (inventories.isEmpty()) {
			return Collections.emptyList();
		}
		
		Pair<List<InventoryPresenter>, LinkedList<InventoryPresenter>> productsDividedPair = this.separateProductsEquallyAndNotEquallyDivided(inventories, storeAmmount);
		List<InventoryPresenter> productsEquallyDivided = productsDividedPair.getFirst();
		LinkedList<InventoryPresenter> productsNotEquallyDivided = productsDividedPair.getSecond();
		
		List<Map<BigDecimal, Long>> distributionToStores = new ArrayList<>();
		this.distributeToStoresProductsEquallyDivided(distributionToStores, storeAmmount, productsEquallyDivided);
		this.distributeToStoresProductsNotEquallyDivided(distributionToStores, productsNotEquallyDivided);
		
		return this.createProductDistributionPresenterList(distributionToStores, product);
	}
	
	private Pair<List<InventoryPresenter>, LinkedList<InventoryPresenter>> separateProductsEquallyAndNotEquallyDivided(List<InventoryPresenter> inventories, Long storeAmmount) {
		List<InventoryPresenter> productsEquallyDivided = new ArrayList<>();
		LinkedList<InventoryPresenter> productsNotEquallyDivided = new LinkedList<>();
		
		inventories.stream().forEach(p -> {
			Long quotient = p.getAmount() / storeAmmount;
			Long remainder = p.getAmount() % storeAmmount;
			if(quotient > 0) {
				productsEquallyDivided.add(new InventoryPresenter(p.getPrice(), quotient));
			}
			while (remainder > 0) {
				productsNotEquallyDivided.add(new InventoryPresenter(p.getPrice(), 1L));
				remainder--;
			}
		});
		
		return Pair.of(productsEquallyDivided, productsNotEquallyDivided);
	}
	
	private void distributeToStoresProductsEquallyDivided(List<Map<BigDecimal, Long>> distributionToStores, Long storeAmmount, List<InventoryPresenter> productsEquallyDivided) {
		for (int i = 0; i < storeAmmount ; i++) {
			Map<BigDecimal, Long> productsMap = new HashMap<>();
			productsEquallyDivided.stream().forEach(p -> productsMap.put(p.getPrice(), p.getAmount()));
			if (!productsMap.isEmpty()) {
				distributionToStores.add(productsMap);
			}
		}
	}
	
	private void distributeToStoresProductsNotEquallyDivided(List<Map<BigDecimal, Long>> distributionToStores, LinkedList<InventoryPresenter> productsNotEquallyDivided) {
		AtomicBoolean getDataFromHeadOfQueue = new AtomicBoolean(true);
		boolean hasProductsEquallyDivaded = !distributionToStores.isEmpty();
		while (!productsNotEquallyDivided.isEmpty()) {
			if (hasProductsEquallyDivaded) {
				distributionToStores.stream().forEach(distribution -> {
					if (!productsNotEquallyDivided.isEmpty()) {
						InventoryPresenter inventoryPresenter = getDataFromHeadOfQueue.get() ? productsNotEquallyDivided.pollFirst() : productsNotEquallyDivided.pollLast();
						
						Long amountDistributed = 0L;
						if (distribution.containsKey(inventoryPresenter.getPrice())) {
							amountDistributed = distribution.get(inventoryPresenter.getPrice());
						}
						distribution.put(inventoryPresenter.getPrice(), amountDistributed + inventoryPresenter.getAmount());
					}
				});
			} else {
				InventoryPresenter inventoryPresenter = getDataFromHeadOfQueue.get() ? productsNotEquallyDivided.pollFirst() : productsNotEquallyDivided.pollLast();
				Map<BigDecimal, Long> productsMap = new HashMap<>();
				productsMap.put(inventoryPresenter.getPrice(), inventoryPresenter.getAmount());
				distributionToStores.add(productsMap);
			}
			getDataFromHeadOfQueue.set(!getDataFromHeadOfQueue.get());
		}
	}
	
	private List<ProductDistributionPresenter> createProductDistributionPresenterList(List<Map<BigDecimal, Long>> distributionToStores, String product) {
		List<ProductDistributionPresenter> productDistribution = new ArrayList<>();
		distributionToStores.forEach(d -> {
			List<ProductPresenter> products = new ArrayList<>();
			d.forEach((k, v) -> products.add(new ProductPresenter(product, k, v, k.multiply(new BigDecimal(v)))));
			
			products.stream().map(ProductPresenter::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
			
			long totalAmount = products.stream().mapToLong(ProductPresenter::getAmount).sum();
			BigDecimal totalPrice = products.stream().map(p -> p.getPrice().multiply(new BigDecimal(p.getAmount()))).reduce(BigDecimal.ZERO, BigDecimal::add);
			
			int numeroLoja = productDistribution.size() + 1;
			productDistribution.add(new ProductDistributionPresenter("Loja " + numeroLoja, products, totalAmount, totalPrice , totalPrice.divide(new BigDecimal(totalAmount), 4, RoundingMode.HALF_EVEN)));
		});
		
		return productDistribution;
	}

}
