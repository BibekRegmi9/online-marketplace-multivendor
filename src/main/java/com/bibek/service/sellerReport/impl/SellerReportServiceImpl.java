package com.bibek.service.sellerReport.impl;

import com.bibek.model.Seller;
import com.bibek.model.SellerReport;
import com.bibek.repository.SellerReportRepository;
import com.bibek.service.sellerReport.SellerReportService;
import org.springframework.stereotype.Service;

@Service
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;

    public SellerReportServiceImpl(SellerReportRepository sellerReportRepository) {
        this.sellerReportRepository = sellerReportRepository;
    }

    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport sellerReport = sellerReportRepository.findBySellerId(seller.getId());
        if(sellerReport == null){
            SellerReport newSellerReport = new SellerReport();
            newSellerReport.setSeller(seller);
            return sellerReportRepository.save(newSellerReport);
        }
        return sellerReport;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }
}
