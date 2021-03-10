package com.gft.teste.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter 
@Setter
@ToString
@Table(name = "INVENTORY")
public class Inventory implements Serializable {

	private static final long serialVersionUID = -7496835580769020073L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "PRODUCT")
	private String product;
	
	@Column(name = "QUANTITY")
	private Long quantity;
	
	@Column(name = "PRICE")
	private BigDecimal price;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "INDUSTRY")
	private String industry;
	
	@Column(name = "ORIGIN")
	private String origin;
	
	@Column(name = "ORIGIN_FILE_NAME")
	private String originFileName;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((industry == null) ? 0 : industry.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + ((originFileName == null) ? 0 : originFileName.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Inventory other = (Inventory) obj;
		
		return Objects.equals(industry, other.industry) &&
				Objects.equals(origin, other.origin) &&
				Objects.equals(originFileName, other.originFileName) &&
				Objects.equals(price, other.price) &&
				Objects.equals(product, other.product) &&
				Objects.equals(quantity, other.quantity) &&
				Objects.equals(type, other.type);
	}
	
}
