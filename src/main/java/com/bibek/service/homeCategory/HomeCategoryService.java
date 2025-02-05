package com.bibek.service.homeCategory;

import com.bibek.model.HomeCategory;

import java.util.List;

public interface HomeCategoryService {
    HomeCategory create(HomeCategory homeCategory);
    List<HomeCategory> createCategories(List<HomeCategory> categories);
    HomeCategory update(HomeCategory homeCategory, Long id);
    List<HomeCategory> getAll();
}
