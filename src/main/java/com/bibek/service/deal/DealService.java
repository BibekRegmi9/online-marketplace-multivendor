package com.bibek.service.deal;

import com.bibek.model.Deal;

import java.util.List;

public interface DealService {
    List<Deal> getAllDeals();
    Deal createDeal(Deal deal);
    Deal updateDeal(Deal deal);
    void deleteDeal(Long id);

}
