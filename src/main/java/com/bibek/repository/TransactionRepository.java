package com.bibek.repository;

import com.bibek.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findBySellerId(Long sellerId);
}
