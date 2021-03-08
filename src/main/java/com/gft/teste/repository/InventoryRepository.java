package com.gft.teste.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gft.teste.model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	
	@Query(value = "SELECT inventory FROM Inventory inventory WHERE inventory.originFileName = :originFileName")
	List<Inventory> findAllByOriginFileName(String originFileName);

}
