package com.gft.teste.presenter;

import java.math.BigDecimal;

import com.google.common.base.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ProductPresenter {
	
	private String name;
	private BigDecimal price;
	private Long amount;
	private BigDecimal volume;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((volume == null) ? 0 : volume.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductPresenter other = (ProductPresenter) obj;
		
		return Objects.equal(amount, other.amount) &&
				Objects.equal(name, other.name) &&
				Objects.equal(price, other.price) &&
				Objects.equal(volume, other.volume);
	}
	
	

}
