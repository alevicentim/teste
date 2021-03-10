package com.gft.teste.batch.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

import com.gft.teste.model.Inventory;
import com.gft.teste.repository.InventoryRepository;

public class FilterDuplicatedInSameFileItemProcessor implements ItemProcessor<Inventory, Inventory> {
	
	private Map<String,List<Inventory>> alreadyProcessedInventory;
	
	public FilterDuplicatedInSameFileItemProcessor(InventoryRepository inventoryRepository, List<String> inputFilesName) {
		alreadyProcessedInventory = new HashMap<>();
		inputFilesName.stream().forEach(inputFileName -> alreadyProcessedInventory.put(inputFileName, inventoryRepository.findAllByOriginFileName(inputFileName)));
	}

	@Override
	public Inventory process(Inventory item) {
		if (item == null) {
			return null;
		}
		
		List<Inventory> inventories = new ArrayList<>();
		if (alreadyProcessedInventory.containsKey(item.getOriginFileName())) {
			inventories = alreadyProcessedInventory.get(item.getOriginFileName());
			if (inventories.contains(item)) {
				return null;
			}
		}
		
		inventories.add(item);
		alreadyProcessedInventory.put(item.getOriginFileName(), inventories);
		
		return item;
	}

}
