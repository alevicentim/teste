package com.gft.teste.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gft.teste.model.Inventory;
import com.gft.teste.presenter.InventoryPresenter;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	
	@Query(value = "SELECT inventory FROM Inventory inventory WHERE inventory.originFileName = :originFileName")
	List<Inventory> findAllByOriginFileName(String originFileName);
	
	@Query(value = "SELECT new com.gft.teste.presenter.InventoryPresenter(i.price, SUM(i.quantity)) " + 
				   "FROM Inventory i " + 
				   "WHERE i.product = :product " + 
				   "GROUP BY i.price " +
				   "ORDER BY i.price")
	List<InventoryPresenter> findProductGroupedByPrice(String product);

}
