package com.gft.teste.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gft.teste.presenter.ErrorPresenter;
import com.gft.teste.presenter.ProductDistributionPresenter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Estoque")
@RequestMapping("/v1/inventory")
public interface InventoryController {
	
	@GetMapping(value = "/calculate-distribution/product/{product}/store-ammount/{storeAmmount}")
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 401, message = "Unauthorized", response = ErrorPresenter.class),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorPresenter.class),
			@ApiResponse(code = 404, message = "Not Found", response = ErrorPresenter.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorPresenter.class)
	})
    List<ProductDistributionPresenter> calculateDistribution(@PathVariable("product") String product, @PathVariable("storeAmmount") Long storeAmmount);

}
