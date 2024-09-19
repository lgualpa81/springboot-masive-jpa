package com.code.repository;

import com.code.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional //es recomendable usarlo en service
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
