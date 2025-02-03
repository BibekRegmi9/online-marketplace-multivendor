package com.bibek.service.transaction.impl;

import com.bibek.model.Order;
import com.bibek.model.Seller;
import com.bibek.model.Transactions;
import com.bibek.repository.SellerRepository;
import com.bibek.repository.TransactionRepository;
import com.bibek.service.transaction.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, SellerRepository sellerRepository) {
        this.transactionRepository = transactionRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public Transactions createTransaction(Order order) {
        Seller seller = sellerRepository.findById(order.getSellerId()).get();
        Transactions transaction = new Transactions();
        transaction.setSeller(seller);
        transaction.setOrder(order);
        transaction.setUser(order.getUser());
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transactions> getTransactionsBySellerId(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transactions> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
