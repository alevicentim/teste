package com.gft.teste.mapper;


import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.gft.teste.batch.mapper.InventoryJsonLineMapper;
import com.gft.teste.model.Inventory;

class InventoryJsonLineMapperTests {

	@Test
	void nullLineTest() throws Exception {
		InventoryJsonLineMapper mapper = new InventoryJsonLineMapper("NomeDoArquivoOrigem");
		Inventory inventory = mapper.mapLine(null, 1);
		
		assertNull(inventory);
	}
	
	@Test
	void mapFirstLineOfFileTest() throws Exception {
		InventoryJsonLineMapper mapper = new InventoryJsonLineMapper("NomeDoArquivoOrigem");
		Inventory inventory = mapper.mapLine("{\"data\":[{\"product\":\"RTIX\",\"quantity\":25,\"price\":\"$0.67\",\"type\":\"3XL\",\"industry\":\"Industrial Specialties\",\"origin\":\"LA\"},", 1);
		
		assertEquals("RTIX", inventory.getProduct());
		assertEquals(new Integer(25), inventory.getQuantity());
		assertEquals(new BigDecimal("0.67"), inventory.getPrice());
		assertEquals("3XL", inventory.getType());
		assertEquals("Industrial Specialties", inventory.getIndustry());
		assertEquals("LA", inventory.getOrigin());
		assertEquals("NomeDoArquivoOrigem", inventory.getOriginFileName());
		
	}
	
	@Test
	void mapNotFirstNeitherLastLineOfFileTest() throws Exception {
		InventoryJsonLineMapper mapper = new InventoryJsonLineMapper("NomeDoArquivoOrigem");
		Inventory inventory = mapper.mapLine("{\"product\":\"UTX\",\"quantity\":82,\"price\":\"$4.84\",\"type\":\"S\",\"industry\":\"Aerospace\",\"origin\":\"TX\"},", 1);
		
		assertEquals("UTX", inventory.getProduct());
		assertEquals(new Integer(82), inventory.getQuantity());
		assertEquals(new BigDecimal("4.84"), inventory.getPrice());
		assertEquals("S", inventory.getType());
		assertEquals("Aerospace", inventory.getIndustry());
		assertEquals("TX", inventory.getOrigin());
		assertEquals("NomeDoArquivoOrigem", inventory.getOriginFileName());
		
	}
	
	@Test
	void mapLastLineOfFileTest() throws Exception {
		InventoryJsonLineMapper mapper = new InventoryJsonLineMapper("NomeDoArquivoOrigem");
		Inventory inventory = mapper.mapLine("{\"product\":\"MUI\",\"quantity\":22,\"price\":\"$0.91\",\"type\":\"2XL\",\"industry\":\"n/a\",\"origin\":\"TX\"}]}", 1);
		
		assertEquals("MUI", inventory.getProduct());
		assertEquals(new Integer(22), inventory.getQuantity());
		assertEquals(new BigDecimal("0.91"), inventory.getPrice());
		assertEquals("2XL", inventory.getType());
		assertEquals("n/a", inventory.getIndustry());
		assertEquals("TX", inventory.getOrigin());
		assertEquals("NomeDoArquivoOrigem", inventory.getOriginFileName());
		
	}
	
}
