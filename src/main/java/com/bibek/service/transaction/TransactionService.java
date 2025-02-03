package com.bibek.service.transaction;

import com.bibek.model.Order;
import com.bibek.model.Seller;
import com.bibek.model.Transactions;

import java.util.List;

public interface TransactionService {
    Transactions createTransaction(Order order);
    List<Transactions> getTransactionsBySellerId(Seller seller);
    List<Transactions> getAllTransactions();
}
