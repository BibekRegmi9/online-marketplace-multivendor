package com.bibek.service.deal.impl;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.Deal;
import com.bibek.model.HomeCategory;
import com.bibek.repository.DealRepository;
import com.bibek.repository.HomeCategoryRepository;
import com.bibek.service.deal.DealService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;
    private final CustomMessageSource customMessageSource;

    public DealServiceImpl(DealRepository dealRepository, HomeCategoryRepository homeCategoryRepository, CustomMessageSource customMessageSource) {
        this.dealRepository = dealRepository;
        this.homeCategoryRepository = homeCategoryRepository;
        this.customMessageSource = customMessageSource;
    }

    @Override
    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory homeCategory = homeCategoryRepository.findById(deal.getCategory().getId()).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, customMessageSource.get(MessageConstants.DEAL))));
        Deal createDeal = new Deal();
        createDeal.setCategory(homeCategory);
        createDeal.setDiscount(deal.getDiscount());
        return dealRepository.save(createDeal);
    }

    @Override
    public Deal updateDeal(Deal deal) {
        Deal existDeal = dealRepository.findById(deal.getId()).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, customMessageSource.get(MessageConstants.DEAL))));
        HomeCategory homeCategory = homeCategoryRepository.findById(deal.getCategory().getId()).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, customMessageSource.get(MessageConstants.HOME_CATEGORY))));

        if(existDeal != null){
            if(deal.getDiscount() !=null){
                existDeal.setDiscount(deal.getDiscount());
            }
            if(homeCategory !=null){
                existDeal.setCategory(homeCategory);
            }

            return dealRepository.save(existDeal);
        }
        throw new CustomRunTimeException(customMessageSource.get(MessageConstants.NOT_EXIST, customMessageSource.get(MessageConstants.DEAL)));
    }

    @Override
    public void deleteDeal(Long id) {
        dealRepository.deleteById(id);
    }
}
