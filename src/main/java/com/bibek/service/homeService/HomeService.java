package com.bibek.service.homeService;

import com.bibek.model.HomeCategory;
import com.bibek.model.HomePage;

import java.util.List;

public interface HomeService {
    HomePage createHomePageData(List<HomeCategory> categories);
}
