
package com.gft.teste.batch.mapper;

import org.springframework.batch.item.file.LineMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.teste.model.Inventory;

public class InventoryJsonLineMapper implements LineMapper<Inventory> {
	
	private static final String DOLLAR_SIGN_IN_PRICE_REPLACE_FROM = "\"price\":\"$";
	private static final String DOLLAR_SIGN_IN_PRICE_REPLACE_TO = "\"price\":\"";
	
	private final ObjectMapper mapper = new ObjectMapper();
	private final String originFileName;
	
	public InventoryJsonLineMapper(String originFileName) {
		this.originFileName = originFileName;
	}
	
	@Override
	public Inventory mapLine(String line, int lineNumber) throws Exception {
		if (line == null) {
			return null;
		}
		
		StringBuilder lineAsValidJson = new StringBuilder(line);
		int dollarSignReplaceIndex = lineAsValidJson.indexOf(DOLLAR_SIGN_IN_PRICE_REPLACE_FROM);
		if (dollarSignReplaceIndex != -1) {
			lineAsValidJson.replace(dollarSignReplaceIndex, dollarSignReplaceIndex + DOLLAR_SIGN_IN_PRICE_REPLACE_FROM.length(), DOLLAR_SIGN_IN_PRICE_REPLACE_TO);
		}
		
		if (line.startsWith("{\"data\":[")) {
			lineAsValidJson.delete(0, 9);
		}
		
		if (line.endsWith(",")) {
			lineAsValidJson.deleteCharAt(lineAsValidJson.length() - 1);
		}
		
		if (line.endsWith("]}")) {
			lineAsValidJson.delete(lineAsValidJson.length() - 2, lineAsValidJson.length());
		}
		
		Inventory inventory = mapper.readValue(lineAsValidJson.toString(), Inventory.class);
		inventory.setOriginFileName(originFileName);
		
		return inventory;
	}

}
