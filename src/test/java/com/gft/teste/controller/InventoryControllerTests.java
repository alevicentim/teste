package com.gft.teste.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.teste.presenter.ProductDistributionPresenter;
import com.gft.teste.presenter.ProductPresenter;
import com.gft.teste.service.InventoryService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.internal.DefaultsImpl;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(InventoryController.class)
@ActiveProfiles("test")
class InventoryControllerTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	InventoryService inventoryService;
	
	@BeforeEach
	public void before() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
		objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

		Configuration.setDefaults(new Configuration.Defaults() {

			private final JsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);
			private final MappingProvider mappingProvider = new JacksonMappingProvider(objectMapper);

			@Override
			public JsonProvider jsonProvider() {
				return jsonProvider;
			}

			@Override
			public MappingProvider mappingProvider() {
				return mappingProvider;
			}

			@Override
			public Set<Option> options() {
				return EnumSet.noneOf(Option.class);
			}
		});
	}
	
    @AfterEach
    public void after(){
        Configuration.setDefaults(DefaultsImpl.INSTANCE);
    }
	
	@Test
	void calculateDistributionReturnOKWithListOfDistribution() throws Exception {
		ProductPresenter product1 = new ProductPresenter("Teste", new BigDecimal("1.02"), 10L, new BigDecimal("1.02").multiply(new BigDecimal(10L)));
		ProductPresenter product2 = new ProductPresenter("Teste", new BigDecimal("1.10"), 50L, new BigDecimal("1.10").multiply(new BigDecimal(50L)));
		List<ProductPresenter> products = Arrays.asList(product1, product2);
		
		ProductDistributionPresenter distribution = new ProductDistributionPresenter("Loja 1", products, 60L, new BigDecimal("65.20"), new BigDecimal("1.0867"));
		List<ProductDistributionPresenter> distributions = Arrays.asList(distribution);
		
		when(inventoryService.calculateDistribution(Mockito.anyString(), Mockito.anyLong())).thenReturn(distributions);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/inventory/calculate-distribution/product/Teste/store-ammount/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].storeName", is(distribution.getStoreName())))
			.andExpect(jsonPath("$[0].totalAmount", is(distribution.getTotalAmount())))
			.andExpect(jsonPath("$[0].totalPrice", is(distribution.getTotalPrice())))
			.andExpect(jsonPath("$[0].avaragePrice", is(distribution.getAvaragePrice())))
			.andExpect(jsonPath("$[0].products[0].name", is(product1.getName())))
			.andExpect(jsonPath("$[0].products[0].price", is(product1.getPrice())))
			.andExpect(jsonPath("$[0].products[0].amount", is(product1.getAmount())))
			.andExpect(jsonPath("$[0].products[0].volume", is(product1.getVolume())))
			.andExpect(jsonPath("$[0].products[1].name", is(product2.getName())))
			.andExpect(jsonPath("$[0].products[1].price", is(product2.getPrice())))
			.andExpect(jsonPath("$[0].products[1].amount", is(product2.getAmount())))
			.andExpect(jsonPath("$[0].products[1].volume", is(product2.getVolume())));
	}
	
	@Test
	void calculateDistributionReturnNotFoundForEmptyList() throws Exception {
		when(inventoryService.calculateDistribution(Mockito.anyString(), Mockito.anyLong())).thenReturn(Collections.emptyList());
		String product = "Teste";
		
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/inventory/calculate-distribution/product/" + product + "/store-ammount/1"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message", is("Produto '" + product + "' não encontrado.")));
	}
	
	@Test
	void calculateDistributionReturnException() throws Exception {
		String errorMessage = "Teste exceção";
		when(inventoryService.calculateDistribution(Mockito.anyString(), Mockito.anyLong())).thenThrow(new RuntimeException(errorMessage));
		String product = "Teste";
		
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/inventory/calculate-distribution/product/" + product + "/store-ammount/1"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.message", is(errorMessage)));
	}

}
