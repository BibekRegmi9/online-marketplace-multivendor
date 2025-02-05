package com.bibek.service.homeCategory.impl;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.HomeCategory;
import com.bibek.repository.HomeCategoryRepository;
import com.bibek.service.homeCategory.HomeCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeCategoryServiceImpl implements HomeCategoryService {
    private final HomeCategoryRepository homeCategoryRepository;
    private final CustomMessageSource customMessageSource;

    public HomeCategoryServiceImpl(HomeCategoryRepository homeCategoryRepository, CustomMessageSource customMessageSource) {
        this.homeCategoryRepository = homeCategoryRepository;
        this.customMessageSource = customMessageSource;
    }

    @Override
    public HomeCategory create(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> categories) {
        if(homeCategoryRepository.findAll().isEmpty()){
            return homeCategoryRepository.saveAll(categories);
        }
        return  homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory update(HomeCategory homeCategory, Long id) {
        HomeCategory existCategory = homeCategoryRepository.findById(homeCategory.getId()).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.NOT_EXIST, customMessageSource.get(MessageConstants.HOME_CATEGORY))));
        if(existCategory.getImage() != null){
            existCategory.setImage(homeCategory.getImage());
        }
        if(existCategory.getCategoryId() != null){
            existCategory.setCategoryId(homeCategory.getCategoryId());
        }
        return homeCategoryRepository.save(existCategory);
    }

    @Override
    public List<HomeCategory> getAll() {
        return homeCategoryRepository.findAll();
    }
}
