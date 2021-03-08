package com.gft.teste.processor;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gft.teste.model.Inventory;
import com.gft.teste.repository.InventoryRepository;

@ExtendWith(MockitoExtension.class)
class FilterDuplicatedInSameFileItemProcessorTests {
	
	private static final String FILE_NAME_1 = "file1";
	private static final String FILE_NAME_2 = "file2";
	private static final List<String> INPUT_FILES_NAME = Arrays.asList(FILE_NAME_1, FILE_NAME_2);
	
	@Mock
	InventoryRepository inventoryRepository;
	
	@Test
	void nullItemToProcess() throws IOException {
		when(inventoryRepository.findAllByOriginFileName(FILE_NAME_1)).thenReturn(this.getInventoryListFile1(FILE_NAME_1));
		when(inventoryRepository.findAllByOriginFileName(FILE_NAME_2)).thenReturn(this.getInventoryListFile1(FILE_NAME_2));
		
		FilterDuplicatedInSameFileItemProcessor processor = new FilterDuplicatedInSameFileItemProcessor(inventoryRepository, INPUT_FILES_NAME);
		Inventory inventoryToProcess = null;
		Inventory inventoryProcessed = processor.process(inventoryToProcess);
		assertNull(inventoryProcessed);
	}
	
	@Test
	void duplicatedLineInSameFileTest() throws IOException {
		when(inventoryRepository.findAllByOriginFileName(FILE_NAME_1)).thenReturn(this.getInventoryListFile1(FILE_NAME_1));
		when(inventoryRepository.findAllByOriginFileName(FILE_NAME_2)).thenReturn(this.getInventoryListFile1(FILE_NAME_2));
		
		FilterDuplicatedInSameFileItemProcessor processor = new FilterDuplicatedInSameFileItemProcessor(inventoryRepository, INPUT_FILES_NAME);
		Inventory inventoryToProcess = new Inventory(null, "RTIX", new Integer(25), new BigDecimal("0.67"), "3XL", "Industrial Specialties", "LA", FILE_NAME_1);
		Inventory inventoryProcessed = processor.process(inventoryToProcess);
		assertNull(inventoryProcessed);
	}
	
	@Test
	void notDuplicatedLineInSameFileTest() throws IOException {
		when(inventoryRepository.findAllByOriginFileName(FILE_NAME_1)).thenReturn(this.getInventoryListFile1(FILE_NAME_1));
		when(inventoryRepository.findAllByOriginFileName(FILE_NAME_2)).thenReturn(this.getInventoryListFile1(FILE_NAME_2));
		
		FilterDuplicatedInSameFileItemProcessor processor = new FilterDuplicatedInSameFileItemProcessor(inventoryRepository, INPUT_FILES_NAME);
		Inventory inventoryToProcess = new Inventory(null, "UTX", new Integer(25), new BigDecimal("0.67"), "3XL", "Industrial Specialties", "LA", FILE_NAME_1);
		Inventory inventoryProcessed = processor.process(inventoryToProcess);
		assertNotNull(inventoryProcessed);
		assertEquals(inventoryToProcess, inventoryProcessed);
	}
	
	@Test
	void duplicatedLineInDifferentFileTest() throws IOException {
		when(inventoryRepository.findAllByOriginFileName(FILE_NAME_1)).thenReturn(this.getInventoryListFile1(FILE_NAME_1));
		when(inventoryRepository.findAllByOriginFileName(FILE_NAME_2)).thenReturn(this.getInventoryListFile1(FILE_NAME_2));
		
		FilterDuplicatedInSameFileItemProcessor processor = new FilterDuplicatedInSameFileItemProcessor(inventoryRepository, INPUT_FILES_NAME);
		Inventory inventoryToProcess = new Inventory(null, "RTIX", new Integer(25), new BigDecimal("0.67"), "3XL", "Industrial Specialties", "LA", FILE_NAME_2);
		Inventory inventoryProcessed = processor.process(inventoryToProcess);
		assertNotNull(inventoryProcessed);
		assertEquals(inventoryToProcess, inventoryProcessed);
	}
	
	
	private List<Inventory> getInventoryListFile1(String fileName) {
		return Arrays.asList(new Inventory(null, "RTIX", new Integer(25), new BigDecimal("0.67"), "3XL", "Industrial Specialties", "LA", FILE_NAME_1),
					  		 new Inventory(new Long(2), "UTX", new Integer(82), new BigDecimal("4.84"), "S", "Aerospace", "TX", FILE_NAME_1),
					  		 new Inventory(null, "MUI", new Integer(22), new BigDecimal("0.91"), "2XL", "n/a", "TX", FILE_NAME_2))
					 .stream()
					 .filter(inv -> inv.getOriginFileName().equals(fileName))
					 .collect(Collectors.toList());
	}
	
}
