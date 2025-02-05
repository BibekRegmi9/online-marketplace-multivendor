package com.bibek.service.homeService.impl;

import com.bibek.enums.HomeCategorySection;
import com.bibek.model.Deal;
import com.bibek.model.HomeCategory;
import com.bibek.model.HomePage;
import com.bibek.repository.DealRepository;
import com.bibek.service.homeService.HomeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {
    private final DealRepository dealRepository;

    public HomeServiceImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    public HomePage createHomePageData(List<HomeCategory> categories) {
        List<HomeCategory> gridCategories = categories.stream().filter(
                category -> category.getSection() == HomeCategorySection.GRID
        ).toList();

        List<HomeCategory> shopByCategories = categories.stream().filter(
                category -> category.getSection() == HomeCategorySection.SHOP_BY_CATEGORY
        ).toList();

        List<HomeCategory> electricCategories = categories.stream().filter(
                category -> category.getSection() == HomeCategorySection.ELECTRIC_CATEGORY
        ).toList();

        List<HomeCategory> dealCategories = categories.stream().filter(
                homeCategory -> homeCategory.getSection() == HomeCategorySection.DEALS
        ).toList();

        List<Deal> createdDeals;

        if(dealRepository.findAll().isEmpty()){
            List<Deal> deals = categories.stream()
                    .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                    .map(category -> new Deal(null, 10, category))
                    .collect(Collectors.toList());

            createdDeals = dealRepository.saveAll(deals);
        } else {
            createdDeals = dealRepository.findAll();
        }

        HomePage home = new HomePage();
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setElectricCategories(electricCategories);
        home.setDeals(createdDeals);
        home.setDealCategories(dealCategories);

        return home;
    }
}
