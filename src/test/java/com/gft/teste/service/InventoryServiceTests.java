package com.gft.teste.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gft.teste.presenter.InventoryPresenter;
import com.gft.teste.presenter.ProductDistributionPresenter;
import com.gft.teste.presenter.ProductPresenter;
import com.gft.teste.repository.InventoryRepository;
import com.gft.teste.service.impl.InventoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTests {
	
	@Mock
	InventoryRepository inventoryRepository;
	
	@InjectMocks
	InventoryServiceImpl inventoryService;
	
	@Test
	void calculateDistributionReturnEmptyListTest() {
		String product = "Test";
		Long storeAmmount = 2L;
		when(inventoryRepository.findProductGroupedByPrice(Mockito.anyString())).thenReturn(Collections.emptyList());
		
		List<ProductDistributionPresenter> distribution = inventoryService.calculateDistribution(product, storeAmmount);
		assertTrue(distribution.isEmpty());
	}
	
	@Test
	void calculateDistributionWithoutEqualProductDistributedToAllReturnNotEmptyListTest() {
		String product = "Test";
		Long storeAmmount = 4L;
		
		BigDecimal price1 = new BigDecimal("1.23");
		BigDecimal price2 = new BigDecimal("2.51");
		
		InventoryPresenter inventoryPresenter1 = new InventoryPresenter(price1, 2L);
		InventoryPresenter inventoryPresenter2 = new InventoryPresenter(price2, 1L);
		List<InventoryPresenter> inventories = Arrays.asList(inventoryPresenter1, inventoryPresenter2);
		
		when(inventoryRepository.findProductGroupedByPrice(Mockito.anyString())).thenReturn(inventories);
		
		ProductPresenter product1 = new ProductPresenter(product, price1, 1L, price1.multiply(new BigDecimal(1L)));
		ProductPresenter product2 = new ProductPresenter(product, price2, 1L, price2.multiply(new BigDecimal(1L)));
		
		List<ProductPresenter> products1 = Arrays.asList(product1);
		List<ProductPresenter> products2 = Arrays.asList(product2);
		List<ProductPresenter> products3 = Arrays.asList(product1);
		
		List<ProductDistributionPresenter> distribution = inventoryService.calculateDistribution(product, storeAmmount);
		assertFalse(distribution.isEmpty());
		assertEquals(3, distribution.size());
		
		assertFalse(distribution.get(0).getProducts().isEmpty());
		assertEquals(1, distribution.get(0).getProducts().size());
		assertEquals("Loja 1", distribution.get(0).getStoreName());
		assertEquals(1L, distribution.get(0).getTotalAmount());
		assertEquals(new BigDecimal("1.23"), distribution.get(0).getTotalPrice());
		assertEquals(new BigDecimal("1.2300"), distribution.get(0).getAvaragePrice());
		assertTrue(distribution.get(0).getProducts().containsAll(products1) && products1.containsAll(distribution.get(0).getProducts()));
		
		assertFalse(distribution.get(1).getProducts().isEmpty());
		assertEquals(1, distribution.get(1).getProducts().size());
		assertEquals("Loja 2", distribution.get(1).getStoreName());
		assertEquals(1L, distribution.get(1).getTotalAmount());
		assertEquals(new BigDecimal("2.51"), distribution.get(1).getTotalPrice());
		assertEquals(new BigDecimal("2.5100"), distribution.get(1).getAvaragePrice());
		assertTrue(distribution.get(1).getProducts().containsAll(products2) && products2.containsAll(distribution.get(1).getProducts()));
		
		assertFalse(distribution.get(2).getProducts().isEmpty());
		assertEquals(1, distribution.get(2).getProducts().size());
		assertEquals("Loja 3", distribution.get(2).getStoreName());
		assertEquals(1L, distribution.get(2).getTotalAmount());
		assertEquals(new BigDecimal("1.23"), distribution.get(2).getTotalPrice());
		assertEquals(new BigDecimal("1.2300"), distribution.get(2).getAvaragePrice());
		assertTrue(distribution.get(2).getProducts().containsAll(products3) && products3.containsAll(distribution.get(2).getProducts()));
	}
	
	@Test
	void calculateDistributionWithEqualProductDistributedToAllReturnNotEmptyListTest() {
		String product = "Test";
		Long storeAmmount = 3L;
		
		BigDecimal price1 = new BigDecimal("1.23");
		BigDecimal price2 = new BigDecimal("2.51");
		BigDecimal price3 = new BigDecimal("3.78");
		BigDecimal price4 = new BigDecimal("3.91");
		
		InventoryPresenter inventoryPresenter1 = new InventoryPresenter(price1, 30L);
		InventoryPresenter inventoryPresenter2 = new InventoryPresenter(price2, 10L);
		InventoryPresenter inventoryPresenter3 = new InventoryPresenter(price3, 11L);
		InventoryPresenter inventoryPresenter4 = new InventoryPresenter(price4, 59L);
		List<InventoryPresenter> inventories = Arrays.asList(inventoryPresenter1, inventoryPresenter2, 
															 inventoryPresenter3, inventoryPresenter4);
		
		when(inventoryRepository.findProductGroupedByPrice(Mockito.anyString())).thenReturn(inventories);
		
		ProductPresenter product1 = new ProductPresenter(product, price1, 10L, price1.multiply(new BigDecimal(10L)));
		ProductPresenter product2a = new ProductPresenter(product, price2, 3L, price2.multiply(new BigDecimal(3L)));
		ProductPresenter product2b = new ProductPresenter(product, price2, 4L, price2.multiply(new BigDecimal(4L)));
		ProductPresenter product3a = new ProductPresenter(product, price3, 3L, price3.multiply(new BigDecimal(3L)));
		ProductPresenter product3b = new ProductPresenter(product, price3, 4L, price3.multiply(new BigDecimal(4L)));
		ProductPresenter product4a = new ProductPresenter(product, price4, 19L, price4.multiply(new BigDecimal(19L)));
		ProductPresenter product4b = new ProductPresenter(product, price4, 20L, price4.multiply(new BigDecimal(20L)));
		
		List<ProductPresenter> products1 = Arrays.asList(product1, product2b, product3a, product4b);
		List<ProductPresenter> products2 = Arrays.asList(product1, product2a, product3b, product4b);
		List<ProductPresenter> products3 = Arrays.asList(product1, product2a, product3b, product4a);
		
		List<ProductDistributionPresenter> distribution = inventoryService.calculateDistribution(product, storeAmmount);
		assertFalse(distribution.isEmpty());
		assertEquals(3, distribution.size());
		
		assertFalse(distribution.get(0).getProducts().isEmpty());
		assertEquals(4, distribution.get(0).getProducts().size());
		assertEquals("Loja 1", distribution.get(0).getStoreName());
		assertEquals(37L, distribution.get(0).getTotalAmount());
		assertEquals(new BigDecimal("111.88"), distribution.get(0).getTotalPrice());
		assertEquals(new BigDecimal("3.0238"), distribution.get(0).getAvaragePrice());
		assertTrue(distribution.get(0).getProducts().containsAll(products1) && products1.containsAll(distribution.get(0).getProducts()));
		
		assertFalse(distribution.get(1).getProducts().isEmpty());
		assertEquals(4, distribution.get(1).getProducts().size());
		assertEquals("Loja 2", distribution.get(1).getStoreName());
		assertEquals(37L, distribution.get(1).getTotalAmount());
		assertEquals(new BigDecimal("113.15"), distribution.get(1).getTotalPrice());
		assertEquals(new BigDecimal("3.0581"), distribution.get(1).getAvaragePrice());
		assertTrue(distribution.get(1).getProducts().containsAll(products2) && products2.containsAll(distribution.get(1).getProducts()));
		
		assertFalse(distribution.get(2).getProducts().isEmpty());
		assertEquals(4, distribution.get(2).getProducts().size());
		assertEquals("Loja 3", distribution.get(2).getStoreName());
		assertEquals(36L, distribution.get(2).getTotalAmount());
		assertEquals(new BigDecimal("109.24"), distribution.get(2).getTotalPrice());
		assertEquals(new BigDecimal("3.0344"), distribution.get(2).getAvaragePrice());
		assertTrue(distribution.get(2).getProducts().containsAll(products3) && products3.containsAll(distribution.get(2).getProducts()));
	}

}
